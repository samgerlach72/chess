package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private final String authToken;

    public String getAuthString() {
        return authToken;
    }
    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }


    //self-defined
    //for JOIN_OBSERVER, LEAVE, RESIGN
    public int getGameID() {
        return gameID;
    }
    private Integer gameID = null;
    public UserGameCommand(String authToken, int gameID, CommandType commandType) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.commandType = commandType;
    }

    //for JOIN_PLAYER
    private ChessGame.TeamColor playerColor = null;
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
    public UserGameCommand(String authToken, int gameID, CommandType commandType, ChessGame.TeamColor playerColor) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.commandType = commandType;
        this.playerColor = playerColor;
    }

    //for MAKE_MOVE
    private ChessMove move;
    public ChessMove getMove() {
        return move;
    }
    public UserGameCommand(String authToken, int gameID, CommandType commandType, ChessGame.TeamColor playerColor, ChessMove move) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.commandType = commandType;
        this.playerColor = playerColor;
        this.move = move;
    }
}
