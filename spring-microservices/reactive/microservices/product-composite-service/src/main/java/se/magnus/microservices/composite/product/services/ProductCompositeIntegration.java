package se.magnus.microservices.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.magnus.microservices.api.core.product.Product;
import se.magnus.microservices.api.core.product.ProductService;
import se.magnus.microservices.api.core.recommendation.Recommendation;
import se.magnus.microservices.api.core.recommendation.RecommendationService;
import se.magnus.microservices.api.core.review.Review;
import se.magnus.microservices.api.core.review.ReviewService;
import se.magnus.microservices.api.event.Event;
import se.magnus.microservices.api.exceptions.InvalidInputException;
import se.magnus.microservices.api.exceptions.NotFoundException;
import se.magnus.microservices.util.http.HttpErrorInfo;

import javax.annotation.PostConstruct;
import java.io.IOException;


import static java.util.logging.Level.FINE;
import static reactor.core.publisher.Flux.empty;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

  private static final String HTTP_PREFIX = "http://";
  private final ObjectMapper mapper;
  private final WebClient webClient;
  private final StreamBridge streamBridge;
  private final Scheduler scheduler;

  private String productServiceUrl;
  private String recommendationServiceUrl;
  private String reviewServiceUrl;

  @Value("${app.product-service.host:localhost}")
  private String productServiceHost;

  @Value("${app.product-service.port:7001}")
  private int productServicePort;

  @Value("${app.recommendation-service.host:localhost}")
  private String recommendationServiceHost;

  @Value("${app.recommendation-service.port:7002}")
  private int recommendationServicePort;

  @Value("${app.review-service.host:localhost}")
  private String reviewServiceHost;

  @Value("${app.review-service.port:7003}")
  private int reviewServicePort;

  @PostConstruct
  public void init(){
    productServiceUrl = HTTP_PREFIX + productServiceHost + ":" + productServicePort + "/product/";
    recommendationServiceUrl = HTTP_PREFIX + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
    reviewServiceUrl = HTTP_PREFIX + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
  }

  public Mono<Product> getProduct(int productId) {

    String url = productServiceUrl + productId;
    log.debug("Will call getProduct API on URL: {}", url);

    return webClient.get().
            uri(url)
            .retrieve()
            .bodyToMono(Product.class)
            .log(log.getName(), FINE)
            .onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
  }

  @Override
  public Mono<Product> createProduct(Product body) {
      return Mono.fromCallable(() -> {
          sendMessage("product-out-0", new Event(Event.Type.CREATE, body.getProductId(), body));
          return body;
      }).subscribeOn(scheduler);
  }

  @Override
  public Mono<Void> deleteProduct(int productId) {
      return Mono.fromRunnable(() -> sendMessage(
              "products-out-0",
                          new Event(Event.Type.DELETE, productId, null)))
              .subscribeOn(scheduler).then();
  }

  @Override
  public Mono<Recommendation> createRecommendation(Recommendation body) {
    return Mono.fromCallable(() -> {
        sendMessage("recommendations-out-0", new Event(Event.Type.CREATE, body.getProductId(), body));
        return body;
    }).subscribeOn(scheduler);
  }

  public Flux<Recommendation> getRecommendations(int productId) {
      String url = recommendationServiceUrl + "/recommendation?productId=" + productId;
      log.debug("Will call the getRecommendations API on URL: {}", url);
      // Return an empty result if something goes wrong to make it possible for the composite service to return partial responses
      return webClient
              .get()
              .uri(url)
              .retrieve()
              .bodyToFlux(Recommendation.class)
              .log(log.getName(), FINE)
              .onErrorResume(error -> empty());
  }

  @Override
  public Mono<Void> deleteRecommendations(int productId) {
      return Mono.fromRunnable(() -> sendMessage("recommendations-out-0", new Event(Event.Type.DELETE, productId, null)))
              .subscribeOn(scheduler).then();
  }

  @Override
  public Mono<Review> createReview(Review body) {
      return Mono.fromCallable(() -> {
        sendMessage("reviews-out-0", new Event(Event.Type.CREATE, body.getProductId(), body));
        return body;
      }).subscribeOn(scheduler);
  }

  public Flux<Review> getReviews(int productId) {

    String url = reviewServiceUrl + productId;
    log.debug("Will call getReviews API on URL: {}", url);

    // Return an empty result if something goes wrong to make it possible for the composite service to return partial responses
    return webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Review.class)
            .log(log.getName(), FINE)
            .onErrorResume(error -> empty());
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
      return Mono.fromRunnable(() -> sendMessage("reviews-out-0", new Event(Event.Type.DELETE, productId, null)))
              .subscribeOn(scheduler).then();
  }

  public Mono<Health> getProductHealth() {
    return getHealth(productServiceUrl);
  }

  public Mono<Health> getRecommendationHealth() {
    return getHealth(recommendationServiceUrl);
  }

  public Mono<Health> getReviewHealth() {
    return getHealth(reviewServiceUrl);
  }

  private Mono<Health> getHealth(String url) {
    url += "/actuator/health";
    log.debug("Will call the Health API on URL: {}", url);
    return webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(String.class)
            .map(s -> new Health.Builder().up().build())
            .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
            .log(log.getName(), FINE);
  }

  private Throwable handleException(Throwable ex) {
    if(!(ex instanceof WebClientResponseException)) {
      log.warn("Got an unexpected error: {}, will rethrow it", ex.toString());
    }

    WebClientResponseException wcre = (WebClientResponseException) ex;

    switch(wcre.getStatusCode()) {
      case NOT_FOUND:
        return new NotFoundException(getErrorMessage(wcre));
      case UNPROCESSABLE_ENTITY:
        return new InvalidInputException(getErrorMessage(wcre));
      default:
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
        log.warn("Error body: {}", wcre.getResponseBodyAsString());
        return ex;
    }
  }

  private String getErrorMessage(WebClientResponseException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioex) {
      return ex.getMessage();
    }
  }

  private void sendMessage(String bindingName, Event event) {
      log.debug("Sending a {} message to {}", event.getEventType(), bindingName);
      Message message = MessageBuilder.withPayload(event)
              .setHeader("partitionKey", event.getKey())
              .build();
      streamBridge.send(bindingName, message);
  }
}
