package se.magnus.microservices.composite.product.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import se.magnus.microservices.api.exceptions.InvalidInputException;
import se.magnus.microservices.api.exceptions.NotFoundException;
import se.magnus.microservices.util.http.HttpErrorInfo;

import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCompositeUtil {

    public static String getErrorMessage(HttpClientErrorException ex, ObjectMapper mapper) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch(IOException ioex) {
            return ioex.getMessage();
        }
    }

    public static RuntimeException handleHttpClientException(HttpClientErrorException ex, ObjectMapper mapper) {
        switch(ex.getStatusCode()) {
            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex, mapper));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(ex, mapper));

            default:
                log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                log.warn("Error body {}", ex.getResponseBodyAsString());
                return ex;
        }
    }
}
