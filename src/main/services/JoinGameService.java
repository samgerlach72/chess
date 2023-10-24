package services;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import dataAccess.Games;
import requests.JoinGameRequest;
import responses.JoinGameResponse;

public class JoinGameService {
    public static JoinGameResponse joinGame(JoinGameRequest request, String authToken) {
        JoinGameResponse response = new JoinGameResponse();
        try {
            String username = AuthTokens.getInstance().authenticate(authToken);
            Games.getInstance().claimSpot(username, request.getGameID(), request.getPlayerColor());
        } catch (DataAccessException exception) {
            response.setMessage(exception.getMessage());
        }
        return response;
    }
}
