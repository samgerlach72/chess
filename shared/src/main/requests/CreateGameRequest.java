package requests;

public class CreateGameRequest {
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }
    private String gameName;
    public String getGameName(){
        return gameName;
    }
}
