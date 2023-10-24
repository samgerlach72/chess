package handlers;

import com.google.gson.Gson;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;
import services.LoginService;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public RegisterHandler(Request req, Response res) {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResponse registerResponse = new RegisterService().register(registerRequest);
        if(registerResponse.getMessage() == null){
            res.status(200);
        }
        else if(registerResponse.getMessage().equals("bad request")){
            res.status(400);
        }
        else if(registerResponse.getMessage().equals("already taken")){
            res.status(403);
        }
        else{
            res.status(500);
        }
        res.type("application/json");
        res.body(new Gson().toJson(registerResponse));
    }
}
