package dataAccess;

import models.Game;

import java.util.HashSet;

public class Games {
    //singleton instance
    private static Games instance;
    //Private constructor to prevent external instantiation
    private Games(){}
    //Public method to access the singleton instance
    public static Games getInstance() {
        if (instance == null) {
            instance = new Games();
        }
        return instance;
    }

    private HashSet<Game> games = new HashSet<>();
    public void insertGame(Game gameToAdd) throws DataAccessException{
        if(gameToAdd.getGameName() == null){
            throw new DataAccessException("Error: bad request");
        }
        games.add(gameToAdd);
    }
    public Game findGame(int gameID){
        for (Game game : games) {
            if (game.getGameID() == gameID) {
                return game; // Return the Game with the specified gameID
            }
        }
        return null; // If no matching game is found
    }
    public HashSet<Game> getAllGames(){
        return games;
    }
    public void claimSpot(String username, int gameID, String playerColor) throws DataAccessException{
        Game game = findGame(gameID);
        if(playerColor.equals("WHITE")){
            if(game.getWhiteUsername() == null){
                game.setWhiteUsername(username);
            }
            else{
                throw new DataAccessException("Error: already taken");
            }
        }
        else if(playerColor.equals("BLACK")){
            if(game.getBlackUsername() == null){
                game.setBlackUsername(username);
            }
            else{
                throw new DataAccessException("Error: already taken");
            }
        }
        else{
            throw new DataAccessException("Error: bad request");
        }
    }
//    public void removeGame(int gameID){
//        Game
//    }
    public void clearGames(){
        games.clear();
    }

    public int getNumGames(){
        return games.size();
    }
}
