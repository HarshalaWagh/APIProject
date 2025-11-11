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
        // Load properties
        Properties props = new Properties();
        
        // Try to load local properties first (for local dev)
        try (InputStream localInput = getClass().getClassLoader().getResourceAsStream("application-local.properties")) {
            if (localInput != null) {
                props.load(localInput);
            }
        } catch (IOException e) {
            // Local properties not found, continue
        }
        
        // Load main properties (will not override existing keys)
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties mainProps = new Properties();
            mainProps.load(input);
            // Only add properties that don't exist
            mainProps.forEach((key, value) -> props.putIfAbsent(key, value));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
        
        // Get configuration (prioritize environment variable for GitHub Actions)
        String baseUrl = props.getProperty("api.base.url");
        String apiVersion = props.getProperty("api.version");
        String token = System.getenv("GOREST_TOKEN"); // GitHub Actions
        if (token == null || token.isEmpty()) {
            token = props.getProperty("api.auth.token"); // Local
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
