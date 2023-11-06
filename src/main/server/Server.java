package server;
import dataAccess.DataAccessException;
import handlers.*;
import spark.*;

import java.sql.SQLException;
import dataAccess.Database;

public class Server {
    public static void main(String[] args) {
        try{
            configureDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);  //fixme
        }
        new Server().run();
    }
    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");

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

    static void configureDatabase() throws SQLException {
        try (var conn = Database.getConnection()) {
            try(var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess")){
                createDbStatement.executeUpdate();
            }
            conn.setCatalog("chess");
            var createAuthTokensTable = """
            CREATE TABLE IF NOT EXISTS AuthTokens (
                username TEXT NOT NULL,
                authToken VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            )""";
            try (var createTableStatement = conn.prepareStatement(createAuthTokensTable)) {
                createTableStatement.executeUpdate();
            }


            //create tables for Games and Users
//            var createUsersTable = """
//            CREATE TABLE IF NOT EXISTS Users (
//                username TEXT NOT NULL,
//                authToken VARCHAR(255) NOT NULL,
//                PRIMARY KEY (authToken)
//            )""";
//            try (var createTableStatement = conn.prepareStatement(createAuthTokensTable)) {
//                createTableStatement.executeUpdate();
//            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);  //fixme
        }
    }
}
