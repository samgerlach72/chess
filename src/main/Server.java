import handlers.*;
import spark.*;

public class Server {
    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("path/to/web/folder");

        //clears entire application database
        Spark.delete("/db", ClearApplicationHandler::new);
        //registers new user
        Spark.post("/user", RegisterHandler::new);
        //logs in user
        Spark.post("/session", LoginHandler::new);
        //logs out user
        Spark.delete("/session", LogoutHandler::new);
        //lists games
        Spark.get("/game", ListGamesHandler::new);
        //creates a new game
        Spark.post("/game", CreateGameHandler::new);
        //joins user to game
        Spark.put("/game", JoinGameHandler::new);
    }
}
