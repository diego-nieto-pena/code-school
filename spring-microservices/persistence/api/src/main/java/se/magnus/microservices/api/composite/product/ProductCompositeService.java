package se.magnus.microservices.api.composite.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ProductComposite", description = "REST API for composite product information.")
public interface ProductCompositeService {

  /**
   * Sample usage: "curl $HOST:$PORT/product-composite/1".
   *
   * @param productId Id of the product
   * @return the composite product info, if found, else null
   */

  @Operation(
          summary = "${api.product-composite.get-composite-product.summary}",
          description = "${api.product-composite.get-composite-product.description}")
  @ApiResponses(
          value = {
              @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok}"),
              @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest}"),
              @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound}"),
              @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity}")
          })
  @GetMapping(
    value = "/product-composite/{productId}",
    produces = "application/json")
  ProductAggregate getProduct(@PathVariable int productId);

  @Operation(
          summary = "${api.product-composite.create-composite-product.summary}",
          description = "${api.product-composite.create-composite-product.description}"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest}"),
          @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity}")
  })
  @PostMapping(value = "/product-composite", consumes = "application/json")
  void createProduct(@RequestBody ProductAggregate body);

  @Operation(
          summary = "${api.product-composite.delete-composite-product.summary}",
          description = "${api.product-composite.delete-composite-product.description}"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest}"),
          @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity}")
  })
  @DeleteMapping("/product-composite/{productId}")
  void deleteProduct(@PathVariable int productId);
}