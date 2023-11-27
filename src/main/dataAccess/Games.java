package dataAccess;

import chess.ChessGameImpl;
import chess.ChessPiece;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.Game;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashSet;

public class Games {
    public static void add(Game gameToAdd) throws DataAccessException{
        if(gameToAdd.getGameName() == null){
            throw new DataAccessException("Error: bad request");
        }
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("INSERT INTO Games (gameID, gameName, observers, chessGame) VALUES(?, ?, ?, ?)")){
                gameToAdd.setGameID(Games.getNumGames() + 1);
                preparedStatement.setInt(1, gameToAdd.getGameID());
                preparedStatement.setString(2, gameToAdd.getGameName());
                preparedStatement.setString(3, new Gson().toJson(gameToAdd.getObservers()));
                preparedStatement.setString(4, new Gson().toJson(gameToAdd.getChessGame()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private static Game findGame(int gameID) throws DataAccessException{
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM Games WHERE gameID=?")){
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if(rs.next()) {
                        Game foundGame = new Game();
                        foundGame.setGameID(rs.getInt("gameID"));
                        foundGame.setWhiteUsername(rs.getString("whiteUsername"));
                        foundGame.setBlackUsername(rs.getString("blackUsername"));
                        foundGame.setGameName(rs.getString("gameName"));
                        foundGame.setChessGame(deserializeGame(rs.getString("chessGame")));
//                        foundGame.setChessGame(new Gson().fromJson(rs.getString("chessGame"), ChessGameImpl.class));
                        foundGame.setObservers(new Gson().fromJson(rs.getString("observers"), new TypeToken<HashSet<String>>(){}.getType()));
                        return foundGame;
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        throw new DataAccessException("Error: bad request");   //if rs returns empty, no matching game in db
    }
    public static HashSet<Game> getAllGames(){
        HashSet<Game> allGames = new HashSet<>();
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM Games")){
                try (var rs = preparedStatement.executeQuery()) {
                    while(rs.next()) {
                        Game foundGame = new Game();
                        foundGame.setGameID(rs.getInt("gameID"));
                        foundGame.setWhiteUsername(rs.getString("whiteUsername"));
                        foundGame.setBlackUsername(rs.getString("blackUsername"));
                        foundGame.setGameName(rs.getString("gameName"));
                        foundGame.setChessGame(deserializeGame(rs.getString("chessGame")));
//                        foundGame.setChessGame(new Gson().fromJson(rs.getString("chessGame"), ChessGameImpl.class));
                        foundGame.setObservers(new Gson().fromJson(rs.getString("observers"), new TypeToken<HashSet<String>>(){}.getType()));
                        allGames.add(foundGame);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return allGames;
    }
    public static void claimSpot(String username, int gameID, String playerColor) throws DataAccessException{
        Game game = findGame(gameID);
        if(playerColor == null){
            game.addObserver(username);
        }
        else if(playerColor.equals("WHITE")){
            if(game.getWhiteUsername() == null){
                game.setWhiteUsername(username);
            }
            else{
                throw new DataAccessException("Error: already taken");
            }
        }
        else if(playerColor.equals("BLACK")){
            if(game.getBlackUsername() == null){
                game.setBlackUsername(username);
            }
            else{
                throw new DataAccessException("Error: already taken");
            }
        }
        else{
            throw new DataAccessException("Error: bad request");
        }
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("UPDATE Games Set whiteUsername=?, blackUsername=?, observers=? WHERE gameID=?")){
                preparedStatement.setString(1, game.getWhiteUsername());
                preparedStatement.setString(2, game.getBlackUsername());
                preparedStatement.setString(3, new Gson().toJson(game.getObservers()));
                preparedStatement.setInt(4, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static void clearGames(){
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Games")){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static int getNumGames(){
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM Games")){
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    private static class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
        public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            switch(el.getAsJsonObject().get("pieceType").getAsString()){
                case "ROOK" -> {
                    return new Gson().fromJson(el, chess.ChessPieceImpl.Rook.class);
                }
                case "QUEEN" -> {
                    return new Gson().fromJson(el, chess.ChessPieceImpl.Queen.class);
                }
                case "PAWN" -> {
                    return new Gson().fromJson(el, chess.ChessPieceImpl.Pawn.class);
                }
                case "KNIGHT" -> {
                    return new Gson().fromJson(el, chess.ChessPieceImpl.Knight.class);
                }
                case "KING" -> {
                    return new Gson().fromJson(el, chess.ChessPieceImpl.King.class);
                }
                case "BISHOP" -> {
                    return new Gson().fromJson(el, chess.ChessPieceImpl.Bishop.class);
                }
                default -> throw new IllegalStateException("Unexpected value: " + el.getAsJsonObject().get("pieceType"));
            }
        }
    }
    private static ChessGameImpl deserializeGame(String gameString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        return gsonBuilder.create().fromJson(gameString, ChessGameImpl.class);
    }
}
