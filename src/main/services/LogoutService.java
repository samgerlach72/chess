package services;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import dataAccess.Users;
import models.AuthToken;
import models.User;
import responses.LoginResponse;
import responses.LogoutResponse;

public class LogoutService {
    public LogoutResponse logout(String authToken) {
        LogoutResponse response = new LogoutResponse();
        try {
            AuthTokens.getInstance().removeToken(authToken);
        } catch (DataAccessException exception) {
            response.setMessage(exception.getMessage());
        }
        return response;
    }
}
