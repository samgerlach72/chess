package requests;


public class JoinGameRequest {
    public JoinGameRequest(String playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
    private String playerColor;
    private int gameID;
    public String getPlayerColor() {
        return playerColor;
    }
    public int getGameID() {
        return gameID;
    }
}
