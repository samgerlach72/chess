package handlers;

import com.google.gson.Gson;
import requests.CreateGameRequest;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.RegisterResponse;
import services.CreateGameService;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    public static String createGame(Request req, Response res) {
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("Authorization");
        CreateGameResponse createGameResponse = new CreateGameService().createGame(createGameRequest, authToken);
        if(createGameResponse.getMessage() == null){
            res.status(200);
        }
        else if(createGameResponse.getMessage().equals("Error: bad request")){
            res.status(400);
        }
        else if(createGameResponse.getMessage().equals("Error: unauthorized")){
            res.status(401);
        }
        else{
            res.status(500);
        }
        res.type("application/json");
        return new Gson().toJson(createGameResponse);
    }
}
