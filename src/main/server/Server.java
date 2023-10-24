package server;
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
        Spark.delete("/db", ClearApplicationHandler::clearApplication);
        //registers new user
        Spark.post("/user", RegisterHandler::register);
        //logs in user
        Spark.post("/session", LoginHandler::login);
        //logs out user
        Spark.delete("/session", LogoutHandler::logout);
        //lists games
        Spark.get("/game", ListGamesHandler::listGames);
        //creates a new game
        Spark.post("/game", CreateGameHandler::createGame);
        //joins user to game
        Spark.put("/game", JoinGameHandler::joinGame);
    }
}
