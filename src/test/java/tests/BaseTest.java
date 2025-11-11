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
        // Priority 1: Try environment variable (for GitHub Actions)
        String token = System.getenv("GOREST_TOKEN");
        
        // Priority 2: Load from properties file (for local)
        if (token == null || token.isEmpty() || token.equals("null")) {
            Properties props = new Properties();
            
            // Try local properties first
            try (InputStream localInput = getClass().getClassLoader().getResourceAsStream("application-local.properties")) {
                if (localInput != null) {
                    props.load(localInput);
                    token = props.getProperty("api.auth.token");
                }
            } catch (IOException e) {
                // Local properties not found, try main properties
            }
            
            // If still no token, try main properties
            if (token == null || token.isEmpty()) {
                try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                    if (input != null) {
                        props.load(input);
                        token = props.getProperty("api.auth.token");
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load properties", e);
                }
            }
        }
        
        // Debug: Print token status (first 10 chars only for security)
        if (token != null && token.length() > 10) {
            System.out.println("✅ Token loaded: " + token.substring(0, 10) + "...");
        } else {
            System.out.println("⚠️ Token not loaded properly: " + token);
        }
        
        // Load other properties
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
        
        String baseUrl = props.getProperty("api.base.url");
        String apiVersion = props.getProperty("api.version");
        
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
