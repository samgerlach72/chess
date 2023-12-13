package ui;

import chess.ChessGame;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import models.Game;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import java.util.Scanner;

public class GameUI {
    private String authToken;
    private int gameID;
    private Game game;
    private ChessGame.TeamColor teamColor = null;
    private WebSocketFacade ws = null;
    GameUI(String authToken, int gameID, String playerColor){
        this.authToken = authToken;
        this.gameID = gameID;
        UserGameCommand joinPlayer = null;
        if (playerColor.equals("WHITE")){
            teamColor = ChessGame.TeamColor.WHITE;
            joinPlayer = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.JOIN_PLAYER, teamColor);
        }
        else if (playerColor.equals("BLACK")){
            teamColor = ChessGame.TeamColor.BLACK;
            joinPlayer = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.JOIN_PLAYER, teamColor);
        }
        else{
            joinPlayer = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.JOIN_OBSERVER);
        }
        try {
            this.ws = new WebSocketFacade("http://localhost:8080", this);
            this.ws.sendMessage(joinPlayer);
            //fixme wait to receive message.
        } catch (ResponseException e) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + e);
        }
//        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "You have joined game.");
        game();
    }

    private void game() {
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "[IN_GAME] >>> ");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            String userInput = scanner.nextLine();
            String[] inputComponents = userInput.split("\\s+");
            if(inputComponents[0].equalsIgnoreCase("help")){
                System.out.print("help" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - with possible commands" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "redraw" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - chess board" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "leave" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - chess game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "move <char> <int> to <char> <int>" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - a piece" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "resign" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - chess game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "quit" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - playing chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "highlight" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - all possible moves\n");
            }
            else if(inputComponents[0].equalsIgnoreCase("redraw")){
                redraw();
            }
//            else if(inputComponents[0].equalsIgnoreCase("leave")){
//                leave();
//            }
//            else if(inputComponents[0].equalsIgnoreCase("move")){
//                join(inputComponents);
//            }
//            else if(inputComponents[0].equalsIgnoreCase("resign")){
//                join(inputComponents);
//            }
            else if(inputComponents[0].equalsIgnoreCase("quit")){
                System.exit(0);
            }
//            else if(inputComponents[0].equalsIgnoreCase("highlight")){
//                if(logout(authToken)){
//                    return;     //returns back to PreLoginUI
//                }
//            }
            else{
                System.out.print("invalid input\n");
            }
        }
    }

    private void redraw(){
        if(teamColor == ChessGame.TeamColor.BLACK){
            PostLoginUI.printBlackBottom(this.game);
        }
        else{
            PostLoginUI.printWhiteBottom(this.game);
        }
    }
    public void notify(ServerMessage serverMessage){
//        System.out.println("in notify");
//        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + serverMessage.getMessage());
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                this.game = serverMessage.getGame();
                redraw();
            }
            case ERROR -> {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + serverMessage.getMessage());
            }
            case NOTIFICATION -> {
                System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + serverMessage.getMessage());
            }
        }
    }
}
