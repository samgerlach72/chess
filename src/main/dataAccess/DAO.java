package dataAccess;
import models.AuthToken;
import models.Game;
import models.User;
import java.util.HashSet;

/**
 * Contains and manipulates database
 */
public class DAO {
    /**
     * Private static variable to hold the singleton instance of DAO
     */
    private static DAO instance;

    /**
     * Private constructor to prevent external instantiation
     */
    private DAO(){}

    /**
     * Public method to access the singleton instance
     */
    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    /**
     * List of authTokens
     */
    private HashSet<AuthToken> authTokens;
    /**
     * List of games
     */
    private HashSet<Game> games;
    /**
     * List of users
     */
    private HashSet<User> users;

    /**
     * checks if given authToken is in the DAO
     *
     * @param authToken is the authToken to be checked against the DAO
     * @return boolean type to signify if the authToken is in the DAO
     */
    boolean checkForToken(String authToken){
        return false;
    }

    /**
     * inserts a new game into the DAO
     *
     * @param gameToAdd is the game being added
     */
    public void insertGame(Game gameToAdd){
        games.add(gameToAdd);
    }

    /**
     * finds a Game in the DAO given a gameID
     *
     * @param gameID is the ID of the game to find
     * @return Game type is the Game that is found returns null if no match
     */
    public Game findGame(int gameID){
        for (Game game : games) {
            if (game.getGameID() == gameID) {
                return game; // Return the Game with the specified gameID
            }
        }
        return null; // If no matching game is found
    }

    /**
     * returns all games in the DAO
     *
     * @return HashSet of Game type is a List of all games
     */
    public HashSet<Game> findAllGames(){
        return games;
    }

    /**
     * inserts a user into a gae
     *
     * @param username is the username of the player trying to join
     * @param gameID is the ID of the desired game
     * @param playerColor is the color that the user wants to play
     * @throws DataAccessException if there is already a user at the desired position
     */
    public void ClaimSpot(String username, int gameID, String playerColor) throws DataAccessException{}

    /**
     * removes a game from DAO
     *
     * @param gameID is the ID of game to delete
     */
    public void removeGame(int gameID){}

    /**
     * clears all games from DAO
     */
    public void clearAllGames(){}

    /**
     * registers new user
     *
     * @param user is the new User object to add
     * @throws DataAccessException if the username (or email?) is already in the DAO
     */
    void CreateUser(User user) throws DataAccessException{}

    /**
     * checks if username and password are a match
     *
     * @param username is the entered username
     * @param password is the entered password
     * @return boolean type for if the user is authenticated or not
     */
    boolean authenticateUser(String username, String password){
        return false;
    }
}
