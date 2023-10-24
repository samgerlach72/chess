package models;
import chess.ChessGame;
import chess.ChessGameImpl;
import dataAccess.Games;

import java.util.HashSet;
import java.util.Objects;

public class Game {
    private ChessGame chessGame = new ChessGameImpl();
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private HashSet<String> observers = new HashSet<>();
    public Game(String gameName){
        this.gameID = Games.getInstance().getNumGames() + 1;
        this.gameName = gameName;
    }
    public void addObserver(String username){
        observers.add(username);
    }
    public int getGameID(){
        return gameID;
    }
    public ChessGame getChessGame() {
        return chessGame;
    }
    public String getWhiteUsername() {
        return whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public String getGameName() {
        return gameName;
    }
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }
    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameID == game.gameID && Objects.equals(chessGame, game.chessGame) && Objects.equals(whiteUsername, game.whiteUsername) && Objects.equals(blackUsername, game.blackUsername) && Objects.equals(gameName, game.gameName);
    }
    @Override
    public int hashCode() {
        return Objects.hash(chessGame, gameID, whiteUsername, blackUsername, gameName);
    }
}
