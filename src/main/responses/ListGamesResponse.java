package responses;
import models.Game;
import java.util.Collection;
import java.util.HashSet;

/**
 * stores response to list game
 */
public class ListGamesResponse {
    /**
     * error message if there is an error
     */
    private String message;
    /**
     * List of all the games in the DAO
     */
    private HashSet<Game> games;

    public String getMessage() {
        return message;
    }

    public HashSet<Game> getGames() {
        return games;
    }
}
