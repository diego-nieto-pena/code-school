package se.magnus.microservices.core.product.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.magnus.microservices.api.core.product.Product;
import se.magnus.microservices.api.core.product.ProductService;
import se.magnus.microservices.api.exceptions.InvalidInputException;
import se.magnus.microservices.api.exceptions.NotFoundException;
import se.magnus.microservices.core.product.persistence.ProductEntity;
import se.magnus.microservices.core.product.persistence.ProductRepository;
import se.magnus.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ServiceUtil serviceUtil;
  private final ProductRepository repository;
  private final ProductMapper mapper;

  @Override
  public Mono<Product> getProduct(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    return repository.findByProductId(productId)
            .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
            .log(log.getName(), FINE)
            .map(e -> mapper.entityToApi(e));

  }

  @Override
  public Mono<Product> createProduct(Product body) {
    final int productId = body.getProductId();
    try {
      ProductEntity entity = mapper.apiToEntity(body);
      Mono<Product> newProduct = repository.save(entity)
              .log(log.getName(), FINE)
              .onErrorMap(
                  DuplicateKeyException.class,
                  ex -> new InvalidInputException("Duplicate key, Product Id: " + productId))
              .map(e -> mapper.entityToApi(e));

      log.debug("createProduct: entity created for productId: {}", productId);
      return newProduct;

    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, Product Id: " + productId);
    }
  }

  @Override
  public Mono<Void> deleteProduct(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    log.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
    return repository.findByProductId(productId)
            .log(log.getName(), FINE)
            .map(e -> repository.delete(e))
            .flatMap(e -> e);
  }
}
