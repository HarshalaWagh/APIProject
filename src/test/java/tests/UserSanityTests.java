package tests;

import constants.APIConstants;
import endpoints.Endpoints;
import io.restassured.response.Response;
import models.dto.UserDTO;
import models.helpers.TestDataLoader;
import org.junit.jupiter.api.*;

import java.util.Map;

import static constants.UserFieldConstants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Sanity")
@Tag("CRUD Operations")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserSanityTests extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Get all users")
    public void getAllUsers() {
        String url = Endpoints.getUsersUrl();
        
        Response response = getRequestSpec().get(url);
        assertEquals(APIConstants.STATUS_CODE_OK, response.getStatusCode());
        
        response.then()
                .statusCode(APIConstants.STATUS_CODE_OK)
                .body("$", not(empty()));
    }
    
    @Test
    @Order(2)
    @DisplayName("Create a new user")
    public void createUser() {
        String url = Endpoints.createUserUrl();
        UserDTO user = TestDataLoader.loadUser("user-john-doe.json");
        
        // Use unique email 
        user.setEmail(generateUniqueEmail());
        
        Response response = getRequestSpec()
                .body(user)
                .post(url);
        assertEquals(APIConstants.STATUS_CODE_CREATED, response.getStatusCode());
        
        response.then()
                .statusCode(APIConstants.STATUS_CODE_CREATED)
                .body(ID, notNullValue())
                .body(NAME, equalTo(user.getName()))
                .body(EMAIL, equalTo(user.getEmail()))
                .body(GENDER, equalTo(user.getGender()))
                .body(STATUS, equalTo(user.getStatus()));
        
        createdUserId = response.jsonPath().getInt(ID);
        System.out.println("Created user with ID: " + createdUserId);
    }
    
    @Test
    @Order(3)
    @DisplayName("Get user by ID")
    public void getUserById() {
        String url = Endpoints.getUserUrlById(createdUserId);
        
        Response response = getRequestSpec().get(url);
        assertEquals(APIConstants.STATUS_CODE_OK, response.getStatusCode());
        
        response.then()
                .statusCode(APIConstants.STATUS_CODE_OK)
                .body(ID, equalTo(createdUserId))
                .body(NAME, notNullValue())
                .body(EMAIL, notNullValue())
                .body(GENDER, notNullValue())
                .body(STATUS, notNullValue());
    }
    
    @Test
    @Order(4)
    @DisplayName("Update user completely")
    public void updateUser() {
        String url = Endpoints.updateUserUrl(createdUserId);
        UserDTO updatedUser = TestDataLoader.loadUser("user-jane-smith.json");
        
        // Use unique email
        updatedUser.setEmail(generateUniqueEmail());
        
        Response response = getRequestSpec()
                .body(updatedUser)
                .put(url);
        assertEquals(APIConstants.STATUS_CODE_OK, response.getStatusCode());
        
        response.then()
                .statusCode(APIConstants.STATUS_CODE_OK)
                .body(ID, equalTo(createdUserId))
                .body(NAME, equalTo(updatedUser.getName()))
                .body(EMAIL, equalTo(updatedUser.getEmail()))
                .body(GENDER, equalTo(updatedUser.getGender()))
                .body(STATUS, equalTo(updatedUser.getStatus()));
    }
    
    @Test
    @Order(5)
    @DisplayName("Partial update user")
    public void partialUpdateUser() {
        String url = Endpoints.updateUserUrl(createdUserId);
        Map<String, Object> updates = TestDataLoader.loadAsMap("user-partial-update.json");
        
        Response response = getRequestSpec()
                .body(updates)
                .patch(url);
        assertEquals(APIConstants.STATUS_CODE_OK, response.getStatusCode());
        
        response.then()
                .statusCode(APIConstants.STATUS_CODE_OK)
                .body(ID, equalTo(createdUserId))
                .body(NAME, equalTo(updates.get(NAME)))
                .body(STATUS, equalTo(updates.get(STATUS)));
    }
    
    @Test
    @Order(6)
    @DisplayName("Delete user")
    public void deleteUser() {
        String url = Endpoints.deleteUserUrl(createdUserId);
        
        Response response = getRequestSpec().delete(url);
        assertEquals(APIConstants.STATUS_CODE_NO_CONTENT, response.getStatusCode());
    }
    
    @Test
    @Order(7)
    @DisplayName("Complete user lifecycle - Create, Read, Update, Delete")
    public void completeUserLifecycle() {
        // CREATE
        UserDTO user = TestDataLoader.loadUser("user-john-doe.json");
        user.setEmail(generateUniqueEmail());
        String createUrl = Endpoints.createUserUrl();
        
        Response createResponse = getRequestSpec()
                .body(user)
                .post(createUrl);
        
        assertEquals(APIConstants.STATUS_CODE_CREATED, createResponse.getStatusCode());
        
        createResponse.then()
                .statusCode(APIConstants.STATUS_CODE_CREATED)
                .body(ID, notNullValue())
                .body(NAME, equalTo(user.getName()))
                .body(EMAIL, equalTo(user.getEmail()));
        
        int newUserId = createResponse.jsonPath().getInt(ID);
        System.out.println("Lifecycle test - Created user with ID: " + newUserId);

        // READ
        String getUrl = Endpoints.getUserUrlById(newUserId);
        
        Response getResponse = getRequestSpec().get(getUrl);
        assertEquals(APIConstants.STATUS_CODE_OK, getResponse.getStatusCode());
        
        getResponse.then()
                .statusCode(APIConstants.STATUS_CODE_OK)
                .body(ID, equalTo(newUserId))
                .body(NAME, equalTo(user.getName()))
                .body(EMAIL, equalTo(user.getEmail()));

        // UPDATE
        Map<String, Object> updates = TestDataLoader.loadAsMap("user-partial-update.json");
        String updateUrl = Endpoints.updateUserUrl(newUserId);
        
        Response updateResponse = getRequestSpec()
                .body(updates)
                .patch(updateUrl);
        
        assertEquals(APIConstants.STATUS_CODE_OK, updateResponse.getStatusCode());
        
        updateResponse.then()
                .statusCode(APIConstants.STATUS_CODE_OK)
                .body(NAME, equalTo(updates.get(NAME)))
                .body(STATUS, equalTo(updates.get(STATUS)));

        // DELETE
        String deleteUrl = Endpoints.deleteUserUrl(newUserId);
        
        Response deleteResponse = getRequestSpec().delete(deleteUrl);
        assertEquals(APIConstants.STATUS_CODE_NO_CONTENT, deleteResponse.getStatusCode());
        
        System.out.println("Lifecycle test - Successfully deleted user with ID: " + newUserId);
    }
}

