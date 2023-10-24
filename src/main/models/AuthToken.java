package models;
import dataAccess.AuthTokens;

import java.util.Objects;
import java.util.Random;

public class AuthToken {
    private String authToken;
    private final String username;
    public AuthToken(String username){
        this.username = username;
        while(true){
            this.authToken = generateRandomToken();
            AuthTokens authTokens = AuthTokens.getInstance();
            if(!authTokens.find(this)){
                return;
            }
        }
    }
    private String generateRandomToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 16;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
    public String getAuthToken() {
        return authToken;
    }
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(authToken, authToken1.authToken) && Objects.equals(username, authToken1.username);
    }
    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }
}
