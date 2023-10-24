package handlers;
import com.google.gson.Gson;
import requests.LoginRequest;
import responses.LoginResponse;
import services.LoginService;
import spark.*;

public class LoginHandler {
    public LoginHandler(Request req, Response res) {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResponse loginResponse = new LoginService().login(loginRequest);
        if(loginResponse.getMessage() == null){
            res.status(200);
        }
        else if(loginResponse.getMessage().equals("unauthorized")){
            res.status(401);
        }
        else{
            res.status(500);
        }
        res.type("application/json");
        res.body(new Gson().toJson(loginResponse));
    }
}