package server.websocket;

import chess.*;
import dataAccess.AuthTokens;
import dataAccess.DataAccessException;
import dataAccess.Games;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException{
        UserGameCommand userGameCommand = Deserializer.createGsonDeserializer().fromJson(message, UserGameCommand.class);
        String username;
        try {
            username = AuthTokens.authenticate(userGameCommand.getAuthString());
        } catch (DataAccessException e) {
            connections.add(userGameCommand.getAuthString(), session, userGameCommand.getGameID());
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Authentication failed"));
            connections.remove(userGameCommand.getAuthString());
            return;
        }
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(userGameCommand, username, session);
            case JOIN_OBSERVER -> joinObserver(userGameCommand, username, session);
            case MAKE_MOVE -> makeMove(userGameCommand, username);
            case LEAVE -> leave(userGameCommand, username);
            case RESIGN -> resign(userGameCommand, username);
        }
    }

    private void joinPlayer(UserGameCommand userGameCommand, String username, Session session) throws IOException{
        connections.add(userGameCommand.getAuthString(), session, userGameCommand.getGameID());
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            if(userGameCommand.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                if (game.getBlackUsername() == null || !game.getBlackUsername().equals(username)) {
                    throw new DataAccessException("Error: Join failed because color is null or taken.");
                }
            }
            else if(userGameCommand.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                if (game.getWhiteUsername() == null || !game.getWhiteUsername().equals(username)) {
                    throw new DataAccessException("Error: Join failed because color is null or taken.");
                }
            }
            else if(userGameCommand.getPlayerColor() == null) {
                throw new DataAccessException("Error: No color specified for join.");
            }
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game));
            String message = username + " joined game as " + userGameCommand.getPlayerColor() + " player.";
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message), userGameCommand.getGameID());
        } catch (DataAccessException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
            connections.remove(userGameCommand.getAuthString());
        }
    }
    private void joinObserver(UserGameCommand userGameCommand, String username, Session session) throws IOException{
        connections.add(userGameCommand.getAuthString(), session, userGameCommand.getGameID());
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game));
            String message = username + " joined game as observer.";
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message), userGameCommand.getGameID());
        } catch (DataAccessException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }
    private void makeMove(UserGameCommand userGameCommand, String username) throws IOException{
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            ChessPiece piece = game.getChessGame().getBoard().getPiece(userGameCommand.getMove().getStartPosition());
            if(piece == null){
                throw new InvalidMoveException("Error: No piece there.");
            }
            if((game.getChessGame().getTeamTurn().equals(ChessGame.TeamColor.WHITE) && game.getWhiteUsername().equals(username)) || (game.getChessGame().getTeamTurn().equals(ChessGame.TeamColor.BLACK) && game.getBlackUsername().equals(username))){
                game.getChessGame().makeMove(userGameCommand.getMove());
            }
            else{
                throw new InvalidMoveException("Error: It's " + game.getChessGame().getTeamTurn() + "'s turn.");
            }
            Games.updateGame(game);
            connections.broadcast(null, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game), userGameCommand.getGameID());
            String message = username + " moved " + piece.getPieceType() + ".";
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message), userGameCommand.getGameID());
        } catch (InvalidMoveException | DataAccessException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }

    private void leave(UserGameCommand userGameCommand, String username) throws IOException{
        connections.remove(userGameCommand.getAuthString());
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            if(Objects.equals(game.getBlackUsername(), username)) {
                game.setBlackUsername(null);
                Games.updateGame(game);
            }
            else if(Objects.equals(game.getWhiteUsername(), username)) {
                game.setWhiteUsername(null);
                Games.updateGame(game);
            }
            String message = username + " has left the game.";
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message), userGameCommand.getGameID());
        } catch (DataAccessException ignored) {
        }
    }

    private void resign(UserGameCommand userGameCommand, String username) throws IOException{
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            if(!(game.getWhiteUsername().equals(username) || game.getBlackUsername().equals(username))){
                connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Cannot resign as observer."));
                return;
            }
            if(game.getChessGame().resignGame()){
                Games.updateGame(game);
                String message = username + " has resigned the game.";
                connections.broadcast(null, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message), userGameCommand.getGameID());
            }
            else{
                connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Cannot resign game is already over."));
            }
        } catch (DataAccessException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }
}

