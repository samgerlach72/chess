package ui;

import chess.*;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import models.Game;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;


public class GameUI {
    private final String authToken;
    private final int gameID;
    private Game game;
    private ChessGame.TeamColor teamColor = null;
    private WebSocketFacade ws = null;

    GameUI(String authToken, int gameID, String playerColor){
        this.authToken = authToken;
        this.gameID = gameID;
        UserGameCommand joinPlayer;
        if (playerColor == null){
            joinPlayer = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.JOIN_OBSERVER);
        }
        else if (playerColor.equals("WHITE")){
            teamColor = ChessGame.TeamColor.WHITE;
            joinPlayer = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.JOIN_PLAYER, teamColor);
        }
        else if (playerColor.equals("BLACK")){
            teamColor = ChessGame.TeamColor.BLACK;
            joinPlayer = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.JOIN_PLAYER, teamColor);
        } else {
            joinPlayer = null;
        }
        try {
            this.ws = new WebSocketFacade("http://localhost:8080", this);
            this.ws.sendMessage(joinPlayer);
            game();
        } catch (ResponseException e) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + e);
        }
    }

    private void game() {
        while(true){
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            String[] inputComponents = userInput.split("\\s+");
            if(inputComponents[0].equalsIgnoreCase("help")){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "help" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - with possible commands" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "redraw" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - chess board" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "leave" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - chess game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "quit" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - playing chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n");
                if(teamColor != null){
                    System.out.print("highlight <column> <row>" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - all legal moves for piece" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                            + "move <column1> <row1> to <column2> <row2> [promotionPiece]" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - a piece" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                            + "resign" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - chess game\n");
                }
            }
            else if(inputComponents[0].equalsIgnoreCase("redraw")){
                redraw(null, null);
            }
            else if(inputComponents[0].equalsIgnoreCase("leave")){
                if(leave()){
                    return;
                }
            }
            else if(inputComponents[0].equalsIgnoreCase("move")){
                move(inputComponents);
            }
            else if(inputComponents[0].equalsIgnoreCase("resign")){
                resign();
            }
            else if(inputComponents[0].equalsIgnoreCase("quit")){
                ws.close();
                System.exit(0);
            }
            else if(inputComponents[0].equalsIgnoreCase("highlight")){
                highlight(inputComponents);
            }
            else{
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "invalid input");
            }
        }
    }
    private void redraw(Set<ChessPosition> positions, ChessPosition startPosition){
        if(teamColor == ChessGame.TeamColor.BLACK){
            BoardPrinter.printBlackBottom(this.game, positions, startPosition);
        }
        else{
            BoardPrinter.printWhiteBottom(this.game, positions, startPosition);
        }
        ChessGame.TeamColor teamTurn = game.getChessGame().getTeamTurn();
        switch (teamTurn) {
            case WHITE -> System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "It's white's turn.");
            case BLACK -> System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "It's black's turn.");
            case WHITE_WON -> System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "White won the game.");
            case BLACK_WON -> System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Black won the game.");
            case STALEMATE -> System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Game is in stalemate.");
        }
    }
    private boolean leave(){
        UserGameCommand leaveCommand = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.LEAVE);
        try {
            this.ws.sendMessage(leaveCommand);
            this.ws.close();
            return true;
        } catch (ResponseException e) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + e);
            return false;
        }
    }
    public void move(String[] inputComponents){
        if (inputComponents.length < 6 || !inputComponents[0].equalsIgnoreCase("move") || !inputComponents[3].equalsIgnoreCase("to")) {
            System.out.println("Invalid move command format. Usage: move <column1> <row1> to <column2> <row2> [promotionPiece]");
            return;
        }
        try {
            int row1 = Integer.parseInt(inputComponents[2]);
            int column1 = inputComponents[1].charAt(0) - 'a' + 1;
            int row2 = Integer.parseInt(inputComponents[5]);
            int column2 = inputComponents[4].charAt(0) - 'a' + 1;
            String promotionPiece = (inputComponents.length > 6) ? inputComponents[6] : null;
            ChessPositionImpl position1 = new ChessPositionImpl(row1, column1);
            ChessPositionImpl position2 = new ChessPositionImpl(row2, column2);
            UserGameCommand moveCommand = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.MAKE_MOVE, teamColor, new ChessMoveImpl(position1, position2, convertToPieceType(promotionPiece)));
            this.ws.sendMessage(moveCommand);
        } catch (NumberFormatException | ResponseException e) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + e);
        }
    }
    public void resign(){
        UserGameCommand resignCommand = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.RESIGN);
        try {
            this.ws.sendMessage(resignCommand);
        } catch (ResponseException e) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + e);
        }
    }
    public void highlight(String[] inputComponents){
        if (inputComponents.length != 3) {
            System.out.println("Invalid move command format. Usage: highlight <column> <row>");
            return;
        }
        try {
            int column1 = inputComponents[1].charAt(0) - 'a' + 1;
            int row1 = Integer.parseInt(inputComponents[2]);
            ChessPositionImpl position = new ChessPositionImpl(row1, column1);
            if(game.getChessGame().getTeamTurn() != game.getChessGame().getBoard().getPiece(position).getTeamColor()){
                redraw(null, position);
                return;
            }
            Set<ChessPosition> positions = this.game.getChessGame()
                    .validMoves(position)
                    .stream()
                    .map(ChessMove::getEndPosition)
                    .collect(Collectors.toSet());
            redraw(positions, position);
        } catch (NumberFormatException e) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + e);
        }
    }

    public void notify(ServerMessage serverMessage){
//        System.out.print("\n");
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                this.game = serverMessage.getGame();
                redraw(null, null);
            }
            case ERROR -> System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + serverMessage.getMessage());
            case NOTIFICATION -> System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + serverMessage.getMessage());
        }
    }
    private static ChessPiece.PieceType convertToPieceType(String promotionPiece) {
        if (promotionPiece == null){
            return null;
        }
        return switch (promotionPiece.toUpperCase()) {
            case "KNIGHT", "N" -> ChessPiece.PieceType.KNIGHT;
            case "BISHOP", "B" -> ChessPiece.PieceType.BISHOP;
            case "ROOK", "R" -> ChessPiece.PieceType.ROOK;
            case "QUEEN", "Q" -> ChessPiece.PieceType.QUEEN;
            default -> null;
        };
    }
}
