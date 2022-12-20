package se.magnus.microservices.api.composite.product;

import lombok.*;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationSummary {

  private int recommendationId;
  private String author;
  private int rate;
  private String content;

}