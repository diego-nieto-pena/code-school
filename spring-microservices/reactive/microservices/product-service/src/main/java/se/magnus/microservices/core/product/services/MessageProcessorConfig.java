package se.magnus.microservices.core.product.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.magnus.microservices.api.core.product.Product;
import se.magnus.microservices.api.core.product.ProductService;
import se.magnus.microservices.api.event.Event;
import se.magnus.microservices.api.exceptions.EventProcessingException;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MessageProcessorConfig {
    private final ProductService productService;

    @Bean
    public Consumer<Event<Integer, Product>> messageProcessor() {
        return event -> {
            log.info("Process message created at {}", event.getEventCreatedAt());

            switch(event.getEventType()) {
                case CREATE:
                    Product product = event.getData();
                    log.info("Create product with ID: {}", product.getProductId());
                    productService.createProduct(product).block();
                    break;

                case DELETE:
                     int productId = event.getKey();
                     log.info("Delete recommendations with ProductID: {}", productId);
                     productService.deleteProduct(productId).block();
                     break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                    log.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }
            log.info("Message processing done!");
        };

    }
}
