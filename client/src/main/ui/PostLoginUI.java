package ui;

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
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "create <NAME>" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
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
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + gameNumber + ": " + EscapeSequences.SET_TEXT_COLOR_GREEN + "Name: " + game.getGameName() + " ID: " + game.getGameID() + EscapeSequences.SET_TEXT_COLOR_RED +" Players: White: " + game.getWhiteUsername() + " Black: " + game.getBlackUsername() + "\n");
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
        try {
            int gameID = Integer.parseInt(inputComponents[1]);
            String playerColor = null;
            if (inputComponents.length == 3) {
                playerColor = inputComponents[2];
            }
            JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);
            JoinGameResponse joinGameResponse = ServerFacade.joinGame(joinGameRequest, authToken);
            if (joinGameResponse.getMessage() != null) {
                System.out.println(joinGameResponse.getMessage());
            } else {
                if (playerColor != null) {
                    System.out.println("Successfully joined game as " + playerColor + ". Type help for gameplay options.");
                } else {
                    System.out.println("Successfully joined game as an observer. Type help for gameplay options.");
                }
                // Only create a new GameUI if the join operation was successful
                new GameUI(authToken, gameID, playerColor);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid gameID. Please enter a valid integer for gameID.");
        }
    }
}
