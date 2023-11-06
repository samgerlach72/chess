package services;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import dataAccess.Games;
import responses.ListGamesResponse;

public class ListGamesService {
    public static ListGamesResponse listGames(String authToken) {
        ListGamesResponse response = new ListGamesResponse();
        try {
            AuthTokens.authenticate(authToken);
            response.setGames(Games.getInstance().getAllGames());
        } catch (DataAccessException exception) {
            response.setMessage(exception.getMessage());
        }
        return response;
    }
}
