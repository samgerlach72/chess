package dataAccess;

import models.AuthToken;
import java.util.HashSet;

public class AuthTokens {
    //singleton instance
    private static AuthTokens instance;
    //Private constructor to prevent external instantiation
    private AuthTokens(){}
    //Public method to access the singleton instance
    public static AuthTokens getInstance() {
        if (instance == null) {
            instance = new AuthTokens();
        }
        return instance;
    }
    private HashSet<AuthToken> authTokens;
    public AuthToken findToken(AuthToken authToken){
        return null;
    }
    public void removeToken(AuthToken authToken){}
    public void addToken(AuthToken authToken){

    }
    public void clearTokens(){}
}
