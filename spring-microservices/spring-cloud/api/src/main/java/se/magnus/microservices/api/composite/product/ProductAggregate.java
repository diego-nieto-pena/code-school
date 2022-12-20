package se.magnus.microservices.api.composite.product;

import lombok.*;

import java.util.List;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAggregate {
  private int productId;
  private String name;
  private int weight;
  private List<RecommendationSummary> recommendations;
  private List<ReviewSummary> reviews;
  private ServiceAddresses serviceAddresses;
}
