package se.magnus.microservices.core.recommendation.services;

import java.util.logging.Level;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.magnus.microservices.api.core.recommendation.Recommendation;
import se.magnus.microservices.api.core.recommendation.RecommendationService;
import se.magnus.microservices.api.exceptions.InvalidInputException;
import se.magnus.microservices.core.recommendation.persistence.RecommendationEntity;
import se.magnus.microservices.core.recommendation.persistence.RecommendationRepository;
import se.magnus.microservices.util.http.ServiceUtil;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

  private final RecommendationRepository repository;

  private final RecommendationMapper mapper;

  private final ServiceUtil serviceUtil;

  @Override
  public Mono<Recommendation> createRecommendation(Recommendation body) {
    try {
      RecommendationEntity entity = mapper.apiToEntity(body);
      Mono<Recommendation> newRecommendation = repository.save(entity)
              .log(log.getName(), Level.FINE)
              .onErrorMap(DuplicateKeyException.class, ex ->
                      new InvalidInputException("Duplicate key, Product Id: "
                          + body.getProductId() + ", Recommendation Id:" + body.getRecommendationId()))
              .map(e -> mapper.entityToApi(e));

      log.debug("createRecommendation: created a recommendation entity: {}/{}", body.getProductId(), body.getRecommendationId());
      return newRecommendation;

    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Recommendation Id:" + body.getRecommendationId());
    }
  }

  @Override
  public Flux<Recommendation> getRecommendations(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    return repository.findByProductId(productId)
            .log(log.getName(), Level.FINE)
            .map(e -> mapper.entityToApi(e));
  }

  @Override
  public Mono<Void> deleteRecommendations(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    log.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);

    return repository.deleteAll(repository.findByProductId(productId));
  }
}