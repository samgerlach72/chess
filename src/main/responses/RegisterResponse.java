package responses;

/**
 * stores response to register
 */
public class RegisterResponse {
    private String message;
    private String authToken;
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
    public void setMessage(String message) {
        this.message = message;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
