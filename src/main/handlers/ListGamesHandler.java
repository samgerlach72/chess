package handlers;

import com.google.gson.Gson;
import responses.ListGamesResponse;
import services.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    public static String listGames(Request req, Response res) {
        String authToken = req.headers("Authorization");
        ListGamesResponse listGamesResponse = ListGamesService.listGames(authToken);
        res.status(GetResponseStatus.getResponseStatus(listGamesResponse.getMessage()));
        res.type("application/json");
        return new Gson().toJson(listGamesResponse);
    }
}
