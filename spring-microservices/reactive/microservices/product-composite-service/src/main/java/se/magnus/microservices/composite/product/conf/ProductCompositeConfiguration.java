package se.magnus.microservices.composite.product.conf;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import se.magnus.microservices.composite.product.services.ProductCompositeIntegration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ProductCompositeConfiguration {
    @Value("${api.common.version}")
    private String apiVersion;

    @Value("${api.common.title}")
    private String apiTitle;

    @Value("${api.common.description}")
    private String apiDescription;

    @Value("${api.common.termsOfService}")
    private String apiTermsOfService;

    @Value("${api.common.license}")
    private String apiLicense;

    @Value("${api.common.licenseUrl}")
    private String apiLicenseUrl;

    @Value("${api.common.externalDocDesc}")
    private String apiExternalDocDesc;

    @Value("${api.common.externalDocUrl}")
    private String apiExternalDocUrl;

    @Value("${api.common.contact.name}")
    private String apiContactName;

    @Value("${api.common.contact.email}")
    private String apiContactEmail;

    @Value("${api.common.contact.url}")
    private String apiContactUrl;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
                .info(
                        new Info().title(apiTitle)
                                .description(apiDescription)
                                .version(apiVersion)
                                .contact(
                                        new Contact()
                                                .name(apiContactName)
                                                .email(apiContactEmail)
                                                .url(apiContactUrl)
                                )
                                .termsOfService(apiTermsOfService)
                                .license(
                                        new License()
                                                .name(apiLicense)
                                                .url(apiLicenseUrl)
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description(apiExternalDocDesc)
                                .url(apiExternalDocUrl)
                );
    }

    @Autowired
    ProductCompositeIntegration integration;

    @Bean
    ReactiveHealthContributor coreServices() {

        final Map<String, ReactiveHealthIndicator> registry = new LinkedHashMap<>();

        registry.put("product", () -> integration.getProductHealth());
        registry.put("recommendation", () -> integration.getRecommendationHealth());
        registry.put("review", () -> integration.getReviewHealth());

        return CompositeReactiveHealthContributor.fromMap(registry);
    }
}
