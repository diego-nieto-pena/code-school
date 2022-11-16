package se.magnus.microservices.api.composite.product;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReviewSummary {

  private final int reviewId;
  private final String author;
  private final String subject;
}
