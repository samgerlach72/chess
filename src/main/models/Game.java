package models;

import chess.ChessGame;
import chess.ChessGameImpl;

/**
 * stores and manipulates game information
 */
public class Game {
    /**
     * stores the actual game of chess with all of its logic
     */
    ChessGame chessGame = new ChessGameImpl();
    /**
     * stores ID of game
     */
    private int gameID;
    /**
     * stores username of white player
     */
    private String whiteUsername;
    /**
     * stores username of black player
     */
    private String blackUsername;
    /**
     * stores name of game
     */
    private String gameName;
    /**
     * constructor of Game
     *
     * @param gameID init gameID field
     * @param blackUsername init blackUserName field
     * @param gameName init gameName field
     * @param whiteUsername init whiteUsername field
     */
    public Game(int gameID, String whiteUsername, String blackUsername, String gameName){}
    public int getGameID(){
        return gameID;
    }

}
