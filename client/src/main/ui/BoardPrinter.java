package ui;

import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessPositionImpl;
import client.ServerFacade;
import models.Game;
import responses.ListGamesResponse;
import java.util.HashSet;
import java.util.Set;

public class BoardPrinter {
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
            printBlackBottom(gameToPrint, null, null);
            System.out.print("\n");
            printWhiteBottom(gameToPrint, null, null);
            System.out.print("\n");
        }
    }
    public static void printWhiteBottom(Game gameToPrint, Set<ChessPosition> positions, ChessPosition startPosition){
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
                ChessPosition position = new ChessPositionImpl(i,j);
                if(startPosition != null && startPosition.equals(position)){
                    System.out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                }
                else if((j + i) % 2 == 0){
                    if(positions != null && positions.contains(position)){
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    else {
                        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                    }
                }
                else{
                    if(positions != null && positions.contains(position)){
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    else {
                        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                    }
                }
                ChessPiece pieceToPrint = gameToPrint.getChessGame().getBoard().getPiece(position);
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
    public static void printBlackBottom(Game gameToPrint, Set<ChessPosition> positions, ChessPosition startPosition){
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
                ChessPosition position = new ChessPositionImpl(i,j);
                if(startPosition != null && startPosition.equals(position)){
                    System.out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                }
                else if((j + i) % 2 == 0){
                    if(positions != null && positions.contains(position)){
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }
                    else {
                        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                    }
                }
                else{
                    if(positions != null && positions.contains(position)){
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    }
                    else {
                        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                    }
                }
                ChessPiece pieceToPrint = gameToPrint.getChessGame().getBoard().getPiece(position);
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
