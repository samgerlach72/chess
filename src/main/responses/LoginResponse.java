package responses;

/**
 * stores response to login
 */
public class LoginResponse {
    /**
     * error message if there is an error
     */
    private String message;
    /**
     * authToken for user to do additional operations
     */
    private String authToken;
    /**
     * username of user that just logged in
     */
    private String username;

    public String getMessage() {
        return message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
