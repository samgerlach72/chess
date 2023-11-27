package ui;
import client.ServerFacade;
import requests.LoginRequest;
import requests.*;
import responses.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class PreLoginUI {
    public static void preLogin() {
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "[LOGGED_OUT] >>> ");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            String userInput = scanner.nextLine();
            String[] inputComponents = userInput.split("\\s+");
            if(inputComponents[0].equalsIgnoreCase("help")){
                System.out.print("register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - to create an account" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                    + "login <USERNAME> <PASSWORD>" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - to play chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                    + "quit" + EscapeSequences.SET_TEXT_COLOR_GREEN + " - playing chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "\n"
                    + "help"  + EscapeSequences.SET_TEXT_COLOR_GREEN + " - with possible commands" + "\n");
            }
            else if(inputComponents[0].equalsIgnoreCase("register")){
                register(inputComponents);
            }
            else if(inputComponents[0].equalsIgnoreCase("login")){
                login(inputComponents);
            }
            else if(inputComponents[0].equalsIgnoreCase("quit")){
                System.exit(0);
            }
            else{
                System.out.print("invalid input\n");
            }
        }
    }

    private static void register(String[] inputComponents){
        if(inputComponents.length != 4){
            System.out.print("wrong input length for \"register\"\n");
            return;
        }
        RegisterRequest registerRequest = new RegisterRequest(inputComponents[1], inputComponents[2], inputComponents[3]);
        RegisterResponse registerResponse;
        try {
            registerResponse = ServerFacade.register(registerRequest);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);  //fixme
        }
        if(registerResponse.getMessage() != null){
            System.out.print(registerResponse.getMessage() + "\n");
        }
        else{
            System.out.print("successfully logged in as " + registerResponse.getUsername() + "\n");
            PostLoginUI.postLogin(registerResponse.getAuthToken());
        }
    }

    private static void login(String[] inputComponents){
        if(inputComponents.length != 3){
            System.out.print("wrong input length for \"login\"\n");
            return;
        }
        LoginRequest loginRequest = new LoginRequest(inputComponents[1], inputComponents[2]);
        LoginResponse loginResponse;
        try {
            loginResponse = ServerFacade.login(loginRequest);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);  //fixme
        }
        if(loginResponse.getMessage() != null){
            System.out.print(loginResponse.getMessage() + "\n");
        }
        else{
            System.out.print("successfully logged in as " + loginResponse.getUsername() + "\n");
            PostLoginUI.postLogin(loginResponse.getAuthToken());
        }
    }

}
