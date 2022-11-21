package se.magnus.microservices.core.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan("se.magnus")
public class ReviewServiceApplication {

  public static void main(String[] args) {
    final ConfigurableApplicationContext context = SpringApplication.run(ReviewServiceApplication.class, args);
    String dbHost = context.getEnvironment().getProperty("spring.datasource.url");
    log.info("Connected to MySQL : {}", dbHost);
  }
}
