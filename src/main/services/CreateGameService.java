package services;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import dataAccess.Users;
import models.AuthToken;
import models.User;
import models.Game;
import dataAccess.Games;
import requests.CreateGameRequest;
import responses.CreateGameResponse;

public class CreateGameService {
    public CreateGameResponse createGame(CreateGameRequest request, String authToken) {
        CreateGameResponse response = new CreateGameResponse();
        try {
            AuthTokens.getInstance().authenticate(authToken);
            Game newGame = new Game(request.getGameName());
            Games.getInstance().insertGame(newGame);
            response.setGameID(newGame.getGameID());
        } catch (DataAccessException exception) {
            response.setMessage(exception.getMessage());
        }
        return response;
    }
}
