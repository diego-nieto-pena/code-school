package se.magnus.microservices.core.review.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.magnus.microservices.api.core.review.Review;
import se.magnus.microservices.api.core.review.ReviewService;
import se.magnus.microservices.api.event.Event;
import se.magnus.microservices.api.exceptions.EventProcessingException;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MessageProcessorConfig {

  private final ReviewService reviewService;

  @Bean
  public Consumer<Event<Integer, Review>> messageProcessor() {
    return event -> {
      log.info("Process message created at {}...", event.getEventCreatedAt());

      switch (event.getEventType()) {

        case CREATE:
          Review review = event.getData();
          log.info("Create review with ID: {}/{}", review.getProductId(), review.getReviewId());
          reviewService.createReview(review).block();
          break;

        case DELETE:
          int productId = event.getKey();
          log.info("Delete reviews with ProductID: {}", productId);
          reviewService.deleteReviews(productId).block();
          break;

        default:
          String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
          log.warn(errorMessage);
          throw new EventProcessingException(errorMessage);
      }

      log.info("Message processing done!");
    };
  }
}