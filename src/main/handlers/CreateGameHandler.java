package handlers;

import com.google.gson.Gson;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import services.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    public static String createGame(Request req, Response res) {
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("Authorization");
        CreateGameResponse createGameResponse = CreateGameService.createGame(createGameRequest, authToken);
        res.status(GetResponseStatus.getResponseStatus(createGameResponse.getMessage()));
        res.type("application/json");
        return new Gson().toJson(createGameResponse);
    }
}
