package handlers;

import com.google.gson.Gson;
import responses.ListGamesResponse;
import services.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    public static String listGames(Request req, Response res) {
        String authToken = req.headers("Authorization");
        ListGamesResponse listGamesResponse = new ListGamesService().listGames(authToken);
        if(listGamesResponse.getMessage() == null){
            res.status(200);
        }
        else if(listGamesResponse.getMessage().equals("Error: unauthorized")){
            res.status(401);
        }
        else{
            res.status(500);
        }
        res.type("application/json");
        return new Gson().toJson(listGamesResponse);
    }
}
