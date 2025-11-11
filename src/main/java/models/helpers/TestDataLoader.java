package models.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// Utility class to load test data from JSON files in resources/testdata folder
public class TestDataLoader {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDataLoader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_DATA_PATH = "/testdata/";
    
    // Load UserDTO from JSON file
    public static UserDTO loadUser(String fileName) {
        try {
            String jsonContent = loadJsonFile(fileName);
            UserDTO user = objectMapper.readValue(jsonContent, UserDTO.class);
            logger.info("Successfully loaded user data from: {}", fileName);
            return user;
        } catch (IOException e) {
            logger.error("Failed to load user data from: {}", fileName, e);
            throw new RuntimeException("Failed to load user test data: " + fileName, e);
        }
    }
    
    // Load Map<String, Object> from JSON file (useful for partial updates)
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadAsMap(String fileName) {
        try {
            String jsonContent = loadJsonFile(fileName);
            Map<String, Object> map = objectMapper.readValue(jsonContent, Map.class);
            logger.info("Successfully loaded map data from: {}", fileName);
            return map;
        } catch (IOException e) {
            logger.error("Failed to load map data from: {}", fileName, e);
            throw new RuntimeException("Failed to load map from: " + fileName, e);
        }
    }
    
    // Load raw JSON string from file
    public static String loadJsonFile(String fileName) {
        try (InputStream inputStream = TestDataLoader.class.getResourceAsStream(TEST_DATA_PATH + fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Test data file not found: " + TEST_DATA_PATH + fileName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", fileName, e);
            throw new RuntimeException("Failed to read test data file: " + fileName, e);
        }
    }
    
    // Get default user data
    public static UserDTO getDefaultUser() {
        return loadUser("user-john-doe.json");
    }
}
