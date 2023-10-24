package handlers;
import com.google.gson.Gson;
import requests.JoinGameRequest;
import responses.JoinGameResponse;
import services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    public static String joinGame(Request req, Response res) {
        JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
        String authToken = req.headers("Authorization");
        JoinGameResponse joinGameResponse = JoinGameService.joinGame(joinGameRequest, authToken);
        res.status(GetResponseStatus.getResponseStatus(joinGameResponse.getMessage()));
        res.type("application/json");
        return new Gson().toJson(joinGameResponse);
    }
}
