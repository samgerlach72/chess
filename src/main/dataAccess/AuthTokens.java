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
    private HashSet<AuthToken> authTokens = new HashSet<>();
    public void authenticate(AuthToken authToken) throws DataAccessException{
        if(!authTokens.contains(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
    }
    public void removeToken(String authTokenString) throws DataAccessException{
        for(AuthToken authToken: authTokens){
            if(authToken.getAuthToken().equals(authTokenString)){
                authTokens.remove(authToken);
                return;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }
    public boolean add(AuthToken authToken){
        return authTokens.add(authToken);
    }
    public void clearTokens(){
        authTokens.clear();
    }

    //used to generate unique token. Not used to authenticate.
    public boolean find(AuthToken authToken){
        return authTokens.contains(authToken);
    }
}
