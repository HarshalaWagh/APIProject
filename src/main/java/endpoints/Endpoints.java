package endpoints;

public class Endpoints {
    
    // User Endpoints
    public static final String USERS = "/users";
    public static final String USER_BY_ID = "/users/{id}";
    
    // GET /users - Get all users
    public static String getUsersUrl() {
        return USERS;
    }
    
    // GET /users/{id} - Get user by ID
    public static String getUserUrlById(int userId) {
        return USER_BY_ID.replace("{id}", String.valueOf(userId));
    }
    
    // POST /users - Create new user
    public static String createUserUrl() {
        return USERS;
    }
    
    // PUT /users/{id} - Update user
    public static String updateUserUrl(int userId) {
        return USER_BY_ID.replace("{id}", String.valueOf(userId));
    }
    
    // DELETE /users/{id} - Delete user
    public static String deleteUserUrl(int userId) {
        return USER_BY_ID.replace("{id}", String.valueOf(userId));
    }
}
