package se.magnus.microservices.api.composite.product;

import lombok.*;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummary {

  private int reviewId;
  private String author;
  private String subject;
  private String content;
}
