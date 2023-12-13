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
            game.getChessGame().makeMove(userGameCommand.getMove());
            Games.updateGame(game);
            connections.broadcast(null, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game));
            String message = username + " moved " + piece + " from " + userGameCommand.getMove().getStartPosition() + " to " + userGameCommand.getMove().getEndPosition() + ".";
            connections.broadcast(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
        } catch (InvalidMoveException e) {
            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid move. Enter a valid move."));
        } catch (DataAccessException e) {
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
            if(game.getChessGame().resignGame()){
                Games.updateGame(game);
                String message = username + " has resigned the game.";
                connections.broadcast(null, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
            }
            else{
                connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: cannot resign. Game is already over."));
            }
        } catch (DataAccessException e) {
//            connections.sendToRoot(userGameCommand.getAuthString(), new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }

//    private static class ChessMoveAdapter implements JsonDeserializer<ChessMove> {
//        public ChessMove deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
//            el.getAsJsonObject().get("pieceType").getAsString()
//        }
//    }

//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}

