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
        JoinGameResponse joinGameResponse = new JoinGameService().joinGame(joinGameRequest, authToken);
        if(joinGameResponse.getMessage() == null){
            res.status(200);
        }
        else if(joinGameResponse.getMessage().equals("Error: bad request")){
            res.status(400);
        }
        else if(joinGameResponse.getMessage().equals("Error: unauthorized")){
            res.status(401);
        }
        else if(joinGameResponse.getMessage().equals("Error: already taken")){
            res.status(403);
        }
        else{
            res.status(500);
        }
        res.type("application/json");
        return new Gson().toJson(joinGameResponse);
    }
}
