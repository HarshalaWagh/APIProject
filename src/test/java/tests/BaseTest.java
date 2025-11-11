package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    private static final Random random = new Random();
    protected RequestSpecification requestSpec;
    protected Integer createdUserId;

    @BeforeAll
    public void setup() {
        Properties props = new Properties();
        
        // Load application.properties
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
        
        // Load application-local.properties if exists (overrides main properties)
        try (InputStream localInput = getClass().getClassLoader().getResourceAsStream("application-local.properties")) {
            if (localInput != null) {
                props.load(localInput);
            }
        } catch (IOException e) {
            System.out.println("Local properties file not found, using default configuration");
        }
        
        // Get configuration (environment variable takes priority)
        String baseUrl = props.getProperty("api.base.url");
        String apiVersion = props.getProperty("api.version");
        String token = System.getenv("GOREST_TOKEN");
        if (token == null || token.isEmpty()) {
            token = props.getProperty("api.auth.token");
        }
        
        // Create RequestSpecification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl + apiVersion)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    protected RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpec);
    }

    // Generate unique email to avoid conflicts
    protected String generateUniqueEmail() {
        return "testuser" + System.currentTimeMillis() + random.nextInt(1000) + "@example.com";
    }
}
