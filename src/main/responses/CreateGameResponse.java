package responses;

/**
 * stores response to create game
 */
public class CreateGameResponse {
    /**
     * error message if there is an error
     */
    private String message;
    /**
     * gameID of new game that has been created
     */
    private int gameID;

    public String getMessage() {
        return message;
    }

    public int getGameID() {
        return gameID;
    }
}
