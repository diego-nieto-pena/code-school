package se.magnus.microservices.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.magnus.microservices.api.core.product.Product;
import se.magnus.microservices.api.core.product.ProductService;
import se.magnus.microservices.api.core.recommendation.Recommendation;
import se.magnus.microservices.api.core.recommendation.RecommendationService;
import se.magnus.microservices.api.core.review.Review;
import se.magnus.microservices.api.core.review.ReviewService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpMethod.GET;
import static se.magnus.microservices.composite.product.util.ProductCompositeUtil.handleHttpClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

  private static final String HTTP_PREFIX = "http://";
  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

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

  public Product getProduct(int productId) {

    try {
      String url = productServiceUrl + productId;
      log.debug("Will call getProduct API on URL: {}", url);

      Product product = restTemplate.getForObject(url, Product.class);
      if(Objects.nonNull(product))
          log.debug("Found a product with id: {}", product.getProductId());

      return product;

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex, mapper);
    }
  }

  @Override
  public Product createProduct(Product body) {
      try {
        log.info("Will post a new product to URL: {}", productServiceUrl);

        final Product product = restTemplate.postForObject(productServiceUrl, body, Product.class);

        if(Objects.nonNull(product))
            log.info("Created product with  id {}", product.getProductId());

        return product;
      } catch(HttpClientErrorException ex) {
        throw handleHttpClientException(ex, mapper);
      }
  }

  @Override
  public void deleteProduct(int productId) {
      try {
        String url = productServiceUrl + "/" + productId;
        log.debug("Will call the deleteProduct API on URL {}", url);

        restTemplate.delete(url);
      } catch(HttpClientErrorException ex) {
        throw handleHttpClientException(ex, mapper);
      }
  }

  @Override
  public Recommendation createRecommendation(Recommendation body) {
    try {
      log.debug("Will post a new recommendation to URL {}", recommendationServiceUrl);
      final Recommendation recommendation = restTemplate.postForObject(recommendationServiceUrl, body, Recommendation.class);

      if(Objects.nonNull(recommendation))
          log.debug("Created recommendation with id {}", recommendation.getRecommendationId());
      return recommendation;
    } catch(HttpClientErrorException ex) {
      throw handleHttpClientException(ex, mapper);
    }
  }

  public List<Recommendation> getRecommendations(int productId) {

    try {
      String url = recommendationServiceUrl + productId;

      log.debug("Will call getRecommendations API on URL: {}", url);
      List<Recommendation> recommendations = restTemplate
        .exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() {}).getBody();

      if(Objects.nonNull(recommendations))
          log.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);
      return recommendations;

    } catch (Exception ex) {
      log.warn("Got an exception while requesting recommendations, return zero recommendations: {}", ex.getMessage());
      return new ArrayList<>();
    }
  }
  @Override
  public void deleteRecommendations(int productId) {
      try {
        String url = recommendationServiceUrl + "?productId=" + productId;
        log.debug("Will call the deleteRecommendations API on URL: {}", url);
        restTemplate.delete(url);
      } catch(HttpClientErrorException ex) {
        throw handleHttpClientException(ex, mapper);
      }
  }

  @Override
  public Review createReview(Review body) {
    try {
      String url = reviewServiceUrl;
      log.debug("Will post a new review to URL: {}", url);

      Review review = restTemplate.postForObject(url, body, Review.class);
      if(Objects.nonNull(review))
          log.debug("Created a review with id: {}", review.getProductId());

      return review;

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex, mapper);
    }
  }

  public List<Review> getReviews(int productId) {

    try {
      String url = reviewServiceUrl + productId;

      log.debug("Will call getReviews API on URL: {}", url);
      List<Review> reviews = restTemplate
        .exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>() {})
        .getBody();

      if(Objects.nonNull(reviews))
          log.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
      return reviews;

    } catch (Exception ex) {
      log.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public void deleteReviews(int productId) {
    try {
      String url = reviewServiceUrl + "?productId=" + productId;
      log.debug("Will call the deleteReviews API on URL: {}", url);

      restTemplate.delete(url);

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex, mapper);
    }
  }


}
