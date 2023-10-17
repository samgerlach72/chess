package requests;

/**
 * stores request to join game
 */
public class JoinGameRequest {
    /**
     * requested color of the player
     */
    private String playerColor;
    /**
     * requested gameID to join
     */
    private int gameID;
    public String getPlayerColor() {
        return playerColor;
    }
    public int getGameID() {
        return gameID;
    }
}
