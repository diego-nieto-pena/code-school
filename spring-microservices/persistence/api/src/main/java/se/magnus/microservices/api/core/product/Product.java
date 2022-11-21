package se.magnus.microservices.api.core.product;

import lombok.*;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  private int productId;
  private String name;
  private int weight;
  private String serviceAddress;

}