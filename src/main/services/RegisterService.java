package services;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import dataAccess.Users;
import models.AuthToken;
import models.User;
import requests.RegisterRequest;
import responses.RegisterResponse;

public class RegisterService {
    public static RegisterResponse register(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();
        User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail());
        try {
            Users.add(newUser);
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
