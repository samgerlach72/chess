package ui;

import chess.ChessPiece;
import chess.ChessPositionImpl;
import client.ServerFacade;
import models.Game;
import requests.*;
import responses.*;
import java.util.HashSet;
import java.util.Scanner;

public class PostLoginUI {
    public static void postLogin(String authToken) {
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "[LOGGED_IN] >>> ");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            String userInput = scanner.nextLine();
            String[] inputComponents = userInput.split("\\s+");
            if(inputComponents[0].equalsIgnoreCase("help")){
                System.out.print("create <NAME> - a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "list" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - games" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "join <ID> [WHITE|BLACK|<empty>]" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "observe <ID>" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "logout" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - when you are done" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "quit" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - playing chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                        + "help" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - with possible commands\n");
            }
            else if(inputComponents[0].equalsIgnoreCase("create")){
                create(inputComponents, authToken);
            }
            else if(inputComponents[0].equalsIgnoreCase("list")){
                list(authToken);
            }
            else if(inputComponents[0].equalsIgnoreCase("join")){
                join(inputComponents, authToken);
            }
            else if(inputComponents[0].equalsIgnoreCase("observe")){
                join(inputComponents, authToken);
            }
            else if(inputComponents[0].equalsIgnoreCase("logout")){
                if(logout(authToken)){
                    return;     //returns back to PreLoginUI
                }
            }
            else if(inputComponents[0].equalsIgnoreCase("quit")){
                System.exit(0);
            }
            else{
                System.out.print("invalid input\n");
            }
        }
    }

    private static void create(String[] inputComponents, String authToken){
        if(inputComponents.length != 2){
            System.out.print("Wrong input length for \"create.\"\n");
            return;
        }
        CreateGameRequest createGameRequest = new CreateGameRequest(inputComponents[1]);
        CreateGameResponse createGameResponse;
        createGameResponse = ServerFacade.createGame(createGameRequest, authToken);
        if(createGameResponse.getMessage() != null){
            System.out.print(createGameResponse.getMessage() + "\n");
        }
        else{
            System.out.print("Successfully created game of ID " + createGameResponse.getGameID() + ".\n");
        }
    }
    private static void list(String authToken){
        ListGamesResponse listGamesResponse;
        listGamesResponse = ServerFacade.list(authToken);
        if(listGamesResponse.getMessage() != null){
            System.out.print(listGamesResponse.getMessage() + "\n");
        }
        else{
            HashSet<Game> games = listGamesResponse.getGames();
            int gameNumber = 1;
            for(Game game : games){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + gameNumber + ": " + EscapeSequences.SET_TEXT_COLOR_GREEN + "Name: " + game.getGameName() + EscapeSequences.SET_TEXT_COLOR_RED +" Players: White: " + game.getWhiteUsername() + " Black: " + game.getBlackUsername() + "\n");
                gameNumber += 1;
            }
        }
    }
    private static boolean logout(String authToken){   //returns true if logs out
        LogoutResponse logoutResponse;
        logoutResponse = ServerFacade.logout(authToken);
        if(logoutResponse.getMessage() != null){
            System.out.print(logoutResponse.getMessage() + "\n");
            return false;
        }
        else{
            System.out.print("Successfully logged out. Type help for options.\n");
            return true;
        }
    }
    private static void join(String[] inputComponents, String authToken){
        if(inputComponents.length != 3 && inputComponents.length != 2){
            System.out.print("wrong input length for \"join/observe.\"\n");
            return;
        }
        String playerColor = null;
        int gameID = Integer.parseInt(inputComponents[1]);  //fixme if int is not entered on command line, program execution halts.
        if(inputComponents.length == 3) {
            playerColor = inputComponents[2];
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);
        JoinGameResponse joinGameResponse;
        joinGameResponse = ServerFacade.joinGame(joinGameRequest, authToken);
        if(joinGameResponse.getMessage() != null){
            System.out.print(joinGameResponse.getMessage() + "\n");
        }
        else{
            if(playerColor != null){
                System.out.print("Successfully joined game as " + playerColor + ".\n");
            }
            else{
                System.out.print("Successfully joined game as observer.\n");
            }
//            printBoard(gameID, authToken);
        }
        new GameUI(authToken, gameID, playerColor);
    }
    public static void printBoard(int gameID, String authToken){
        ListGamesResponse listGamesResponse;
        listGamesResponse = ServerFacade.list(authToken);
        if(listGamesResponse.getMessage() != null){
            System.out.print("Could not print board because list failed message: " + listGamesResponse.getMessage() + "\n");
        }
        else{
            HashSet<Game> games = listGamesResponse.getGames();
            Game gameToPrint = null;
            for(Game game : games){
                if (game.getGameID() == gameID){
                    gameToPrint = game;
                }
            }
            printBlackBottom(gameToPrint);
            System.out.print("\n");
            printWhiteBottom(gameToPrint);
            System.out.print("\n");
        }
    }
    public static void printWhiteBottom(Game gameToPrint){
        for(int i = 0; i < 10; ++i){
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            if(i == 0 || i == 9){
                System.out.print("   ");
            }
            else{
                System.out.print(" " + (char)(i - 1 + 'a') + " ");
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + "\n");
        for(int i = 8; i > 0; --i){
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" " + i + " ");
            for(int j = 1; j < 9; ++j){
                if((j + i) % 2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                }
                else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                }
                ChessPiece pieceToPrint = gameToPrint.getChessGame().getBoard().getPiece(new ChessPositionImpl(i,j));
                String pieceChar = pieceToChar(pieceToPrint);
                System.out.print(pieceChar);
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" " + i + " ");
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + "\n");
        }
        for(int i = 0; i < 10; ++i){
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            if(i == 0 || i == 9){
                System.out.print("   ");
            }
            else{
                System.out.print(" " + (char)(i - 1 + 'a') + " ");
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + "\n");
    }
    public static void printBlackBottom(Game gameToPrint){
        for(int i = 9; i >= 0; --i){
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            if(i == 0 || i == 9){
                System.out.print("   ");
            }
            else{
                System.out.print(" " + (char)(i - 1 + 'a') + " ");
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + "\n");
        for(int i = 1; i < 9; ++i){
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" " + i + " ");
            for(int j = 8; j > 0; --j){
                if((j + i) % 2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                }
                else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                }
                ChessPiece pieceToPrint = gameToPrint.getChessGame().getBoard().getPiece(new ChessPositionImpl(i,j));
                String pieceChar = pieceToChar(pieceToPrint);
                System.out.print(pieceChar);
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" " + i + " ");
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + "\n");
        }
        for(int i = 9; i >= 0; --i){
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            if(i == 0 || i == 9){
                System.out.print("   ");
            }
            else{
                System.out.print(" " + (char)(i - 1 + 'a') + " ");
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + "\n");
    }
    private static String pieceToChar(ChessPiece pieceToPrint){
        if(pieceToPrint == null){
            return "   ";
        }
        switch (pieceToPrint.getTeamColor()) {
            case WHITE -> {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                switch (pieceToPrint.getPieceType()) {
                    case KING -> {
                        return EscapeSequences.WHITE_KING;
                    }
                    case QUEEN -> {
                        return EscapeSequences.WHITE_QUEEN;
                    }
                    case BISHOP -> {
                        return EscapeSequences.WHITE_BISHOP;
                    }
                    case KNIGHT -> {
                        return EscapeSequences.WHITE_KNIGHT;
                    }
                    case ROOK -> {
                        return EscapeSequences.WHITE_ROOK;
                    }
                    case PAWN -> {
                        return EscapeSequences.WHITE_PAWN;
                    }
                }
            }
            case BLACK -> {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                switch (pieceToPrint.getPieceType()) {
                    case KING -> {
                        return EscapeSequences.BLACK_KING;
                    }
                    case QUEEN -> {
                        return EscapeSequences.BLACK_QUEEN;
                    }
                    case BISHOP -> {
                        return EscapeSequences.BLACK_BISHOP;
                    }
                    case KNIGHT -> {
                        return EscapeSequences.BLACK_KNIGHT;
                    }
                    case ROOK -> {
                        return EscapeSequences.BLACK_ROOK;
                    }
                    case PAWN -> {
                        return EscapeSequences.BLACK_PAWN;
                    }
                }
            }
        }
        return "   ";
    }
}
