package requests;

/**
 * stores request to login
 */
public class LoginRequest {
    /**
     * username of player trying to login
     */
    private String username;
    /**
     * password that the user entered
     */
    private String password;
    private String getUsername(){
        return username;
    }
    private String getPassword(){
        return password;
    }
}
