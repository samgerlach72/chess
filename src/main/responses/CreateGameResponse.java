package responses;

public class CreateGameResponse {
    private String message;
    private Integer gameID = null;
    public String getMessage() {
        return message;
    }
    public int getGameID() {
        return gameID;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
