package se.magnus.microservices.api.composite.product;

import lombok.*;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAddresses {
  private String cmp;
  private String pro;
  private String rev;
  private String rec;
}
