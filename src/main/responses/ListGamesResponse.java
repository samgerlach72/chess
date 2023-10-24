package responses;
import models.Game;
import java.util.Collection;
import java.util.HashSet;

/**
 * stores response to list game
 */
public class ListGamesResponse {
    private String message;
    private HashSet<Game> games;
    public String getMessage() {
        return message;
    }
    public HashSet<Game> getGames() {
        return games;
    }
}
