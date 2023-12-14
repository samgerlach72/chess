package client.websocket;

import chess.ChessPiece;
import client.ServerFacade;
import com.google.gson.*;
import ui.GameUI;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import exception.ResponseException;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    GameUI gameUI;

    public WebSocketFacade(String url, GameUI gameUI) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.gameUI = gameUI;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(ChessPiece.class, new ServerFacade.ChessPieceAdapter())
                            .create();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    gameUI.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
    public void sendMessage(UserGameCommand action) throws ResponseException {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void close(){
        try {
            this.session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

