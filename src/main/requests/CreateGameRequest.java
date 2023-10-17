package requests;

/**
 * stores request to create game
 */
public class CreateGameRequest {
    /**
     * name that user wants to give to the game they are creating
     */
    private String gameName;
    public String getGameName(){
        return gameName;
    }
}
