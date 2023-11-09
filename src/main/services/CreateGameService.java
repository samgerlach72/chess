package services;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import models.Game;
import dataAccess.Games;
import requests.CreateGameRequest;
import responses.CreateGameResponse;

public class CreateGameService {
    public static CreateGameResponse createGame(CreateGameRequest request, String authToken) {
        CreateGameResponse response = new CreateGameResponse();
        try {
            AuthTokens.authenticate(authToken);
            Game newGame = new Game(request.getGameName());
            Games.add(newGame);
            response.setGameID(newGame.getGameID());
        } catch (DataAccessException exception) {
            response.setMessage(exception.getMessage());
        }
        return response;
    }
}
