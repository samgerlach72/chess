package responses;
import models.Game;
import java.util.HashSet;

public class ListGamesResponse {
    private String message;
    private HashSet<Game> games;
    public String getMessage() {
        return message;
    }
    public HashSet<Game> getGames() {
        return games;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setGames(HashSet<Game> games) {
        this.games = games;
    }
}
