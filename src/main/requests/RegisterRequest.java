package requests;

/**
 * stores request to register
 */
public class RegisterRequest {
    /**
     * requested username
     */
    private String username;
    /**
     * requested password
     */
    private String password;
    /**
     * requested email
     */
    private String email;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
