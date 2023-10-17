package responses;

/**
 * stores response to register
 */
public class RegisterResponse {
    /**
     * error message if there is an error
     */
    private String message;
    /**
     * authToken so that registered user doesn't have to sign in after registering
     */
    private String authToken;
    /**
     * username of user that just registered
     */
    private String username;
}
