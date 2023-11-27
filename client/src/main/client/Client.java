package client;

import ui.EscapeSequences;

public class Client {
    public static void main(String[] args) {
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("Welcome to 240 chess. Type help to get started.\n\n");
        ui.PreLoginUI.preLogin();
    }
}
