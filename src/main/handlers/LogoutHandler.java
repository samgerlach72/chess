package handlers;

import com.google.gson.Gson;
import requests.LoginRequest;
import responses.LoginResponse;
import responses.LogoutResponse;
import services.LoginService;
import services.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    public static String logout(Request req, Response res) {
        String authToken = req.headers("Authorization");
        LogoutResponse logoutResponse = new LogoutService().logout(authToken);
        if(logoutResponse.getMessage() == null){
            res.status(200);
        }
        else if(logoutResponse.getMessage().equals("Error: unauthorized")){
            res.status(401);
        }
        else{
            res.status(500);
        }
        res.type("application/json");
        return new Gson().toJson(logoutResponse);
    }
}
