package se.magnus.microservices.composite.product.services;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import se.magnus.microservices.api.composite.product.*;
import se.magnus.microservices.api.core.product.Product;
import se.magnus.microservices.api.core.recommendation.Recommendation;
import se.magnus.microservices.api.core.review.Review;
import se.magnus.microservices.api.exceptions.NotFoundException;
import se.magnus.microservices.util.http.ServiceUtil;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProductCompositeServiceImpl implements ProductCompositeService {

  private final ServiceUtil serviceUtil;
  private final ProductCompositeIntegration integration;

  @Override
  public ProductAggregate getProduct(int productId) {

    Product product = integration.getProduct(productId);
    if (product == null) {
      throw new NotFoundException("No product found for productId: " + productId);
    }

    List<Recommendation> recommendations = integration.getRecommendations(productId);

    List<Review> reviews = integration.getReviews(productId);

    return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
  }

  private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations, List<Review> reviews,
    String serviceAddress) {

    // 1. Setup product info
    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    // 2. Copy summary recommendation info, if available
    List<RecommendationSummary> recommendationSummaries =
      (recommendations == null) ? null : recommendations.stream()
        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
        .collect(Collectors.toList());

    // 3. Copy summary review info, if available
    List<ReviewSummary> reviewSummaries =
      (reviews == null) ? null : reviews.stream()
        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
        .collect(Collectors.toList());

    // 4. Create info regarding the involved microservices addresses
    String productAddress = product.getServiceAddress();
    String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
    String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
  }
}