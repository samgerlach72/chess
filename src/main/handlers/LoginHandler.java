package handlers;
import com.google.gson.Gson;
import requests.LoginRequest;
import responses.LoginResponse;
import services.LoginService;
import spark.*;

public class LoginHandler {
    public static String login(Request req, Response res) {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResponse loginResponse = LoginService.login(loginRequest);
        res.status(GetResponseStatus.getResponseStatus(loginResponse.getMessage()));
        res.type("application/json");
        return new Gson().toJson(loginResponse);
    }
}
