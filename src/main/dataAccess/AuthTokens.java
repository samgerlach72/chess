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
    public void authenticate(AuthToken authToken) throws DataAccessException{
        if(!authTokens.contains(authToken)){
            throw new DataAccessException("unauthorized");
        }
    }
    public void removeToken(AuthToken authToken){
        authTokens.remove(authToken);
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
