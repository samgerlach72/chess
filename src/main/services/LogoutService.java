package services;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import responses.LogoutResponse;

public class LogoutService {
    public static LogoutResponse logout(String authToken) {
        LogoutResponse response = new LogoutResponse();
        try {
            AuthTokens.getInstance().removeToken(authToken);
        } catch (DataAccessException exception) {
            response.setMessage(exception.getMessage());
        }
        return response;
    }
}
