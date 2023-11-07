package services;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import dataAccess.Users;
import models.AuthToken;
import models.User;
import responses.LoginResponse;
import requests.LoginRequest;

public class LoginService {
    public static LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        User newUser = new User(request.getUsername(), request.getPassword(), null);
        try {
            Users.authenticateUser(newUser);
            response.setUsername(request.getUsername());
            AuthToken authToken = new AuthToken(request.getUsername());
            AuthTokens.add(authToken);
            response.setAuthToken(authToken.getAuthToken());
        } catch (DataAccessException exception) {
            response.setMessage(exception.getMessage());
        }
        return response;
    }
}
