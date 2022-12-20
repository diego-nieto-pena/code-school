package se.magnus.microservices.core.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import se.magnus.microservices.core.product.persistence.ProductEntity;

@Slf4j
@SpringBootApplication
@ComponentScan("se.magnus")
public class ProductServiceApplication {

  @Autowired
  private MongoOperations mongoTemplate;

  public static void main(String[] args) {
    final ConfigurableApplicationContext context = SpringApplication.run(ProductServiceApplication.class, args);
    String dbHost = context.getEnvironment().getProperty("spring.data.mongodb.host");
    String port = context.getEnvironment().getProperty("spring.data.mongodb.port");
    log.info("Connected to MongoDB : {}:{}", dbHost, port);
  }


  @EventListener(ContextRefreshedEvent.class)
  public void initIndicesAfterStartup() {

    MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoTemplate.getConverter().getMappingContext();
    IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);

    IndexOperations indexOps = mongoTemplate.indexOps(ProductEntity.class);
    resolver.resolveIndexFor(ProductEntity.class).forEach(e -> indexOps.ensureIndex(e));
  }
}