package server.websocket;

import chess.*;
import com.google.gson.*;
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
            connections.add(userGameCommand.getAuthString(), session);
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
        connections.add(userGameCommand.getAuthString(), session);
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
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
        } catch (DataAccessException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
            connections.remove(userGameCommand.getAuthString());
        }
    }
    private void joinObserver(UserGameCommand userGameCommand, String username, Session session) throws IOException{
        connections.add(userGameCommand.getAuthString(), session);
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game));
            String message = username + " joined game as observer.";
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
        } catch (DataAccessException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }
    private void makeMove(UserGameCommand userGameCommand, String username) throws IOException{
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            ChessPiece.PieceType piece = game.getChessGame().getBoard().getPiece(userGameCommand.getMove().getStartPosition()).getPieceType();
            if((game.getChessGame().getTeamTurn().equals(ChessGame.TeamColor.WHITE) && game.getWhiteUsername().equals(username)) || (game.getChessGame().getTeamTurn().equals(ChessGame.TeamColor.BLACK) && game.getBlackUsername().equals(username))){
                game.getChessGame().makeMove(userGameCommand.getMove());
            }
            else{
                throw new InvalidMoveException("Error: It's " + game.getChessGame().getTeamTurn() + "'s turn.");
            }
            Games.updateGame(game);
            connections.broadcast(null, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game));
            String message = username + " moved " + piece + " from " + userGameCommand.getMove().getStartPosition() + " to " + userGameCommand.getMove().getEndPosition() + ".";
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
        } catch (InvalidMoveException | DataAccessException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }

    private void leave(UserGameCommand userGameCommand, String username) throws IOException{
        connections.remove(userGameCommand.getAuthString());
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            if(userGameCommand.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                game.setBlackUsername(null);
                Games.updateGame(game);
            }
            else if(userGameCommand.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                game.setWhiteUsername(null);
                Games.updateGame(game);
            }
            String message = username + " has left the game.";
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
        } catch (DataAccessException e) {
//            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }

    private void resign(UserGameCommand userGameCommand, String username) throws IOException{
//        connections.remove(userGameCommand.getAuthString());
        try {
            Game game = Games.findGame(userGameCommand.getGameID());
            if(!(game.getWhiteUsername().equals(username) || game.getBlackUsername().equals(username))){
                connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: cannot resign as observer."));
                return;
            }
            if(game.getChessGame().resignGame()){
                Games.updateGame(game);
                String message = username + " has resigned the game.";
                connections.broadcast(null, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
            }
            else{
                connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: cannot resign game is already over."));
            }
        } catch (DataAccessException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }
}

