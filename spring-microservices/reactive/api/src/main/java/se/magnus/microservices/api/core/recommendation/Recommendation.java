package se.magnus.microservices.api.core.recommendation;

import lombok.*;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {
  private int productId;
  private int recommendationId;
  private String author;
  private int rate;
  private String content;
  private String serviceAddress;


}