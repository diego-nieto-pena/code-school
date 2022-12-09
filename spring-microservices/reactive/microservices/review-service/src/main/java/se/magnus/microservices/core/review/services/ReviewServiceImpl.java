package se.magnus.microservices.core.review.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.magnus.microservices.api.core.review.Review;
import se.magnus.microservices.api.core.review.ReviewService;
import se.magnus.microservices.api.exceptions.InvalidInputException;
import se.magnus.microservices.core.review.persistence.ReviewEntity;
import se.magnus.microservices.core.review.persistence.ReviewRepository;
import se.magnus.microservices.util.http.ServiceUtil;

import java.util.List;
import java.util.logging.Level;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository repository;

  private final ReviewMapper mapper;

  private final ServiceUtil serviceUtil;

  private final Scheduler jdbcScheduler;

  @Override
  public Mono<Review> createReview(Review body) {

    if(body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }

    return Mono.fromCallable(() -> internalCreateReview(body)).subscribeOn(jdbcScheduler);
  }

  private Review internalCreateReview(Review body) {
    try {
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);

      log.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
    }
  }

  @Override
  public Flux<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    return Mono.fromCallable(() -> internalGetReviews(productId))
            .flatMapMany(Flux::fromIterable)
            .log(log.getName(), Level.FINE)
            .subscribeOn(jdbcScheduler);
  }

  public List<Review> internalGetReviews(int productId) {
    List<ReviewEntity> entityList = repository.findByProductId(productId);
    List<Review> list = mapper.entityListToApiList(entityList);

    log.debug("getReviews: response size: {}", list.size());

    return list;
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    return Mono.fromRunnable(() -> internalDeleteReviews(productId)).subscribeOn(jdbcScheduler).then();
  }

  private void internalDeleteReviews(int productId) {
    log.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }
}