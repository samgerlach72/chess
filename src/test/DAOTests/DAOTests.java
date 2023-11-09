package DAOTests;
import models.*;
import dataAccess.*;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import services.*;
import java.sql.SQLException;
import java.util.HashSet;

public class DAOTests {
    private final AuthToken testAuthToken = new AuthToken("user1");
    private final User testUser = new User("user1", "password1", "email1");
    private final Game testGame =new Game("game1");
    private static Connection conn;

    @BeforeEach
    public void setup() {
        ClearApplicationService.clearApplication();
    }
    @BeforeAll
    public static void beforeAll() {
        try {
            conn = Database.getConnection();
            conn.setCatalog("chess");
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @AfterAll
    public static void afterAll(){
        try {
            Database.closeConnection(conn);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //AuthTokens tests
    @Test
    public void successAuthenticate() {
        AuthTokens.add(testAuthToken);
        Assertions.assertDoesNotThrow(() -> AuthTokens.authenticate(testAuthToken.getAuthToken()), "valid authToken should authenticate user");
    }
    @Test
    public void failAuthenticate() {    //without adding to db
        Assertions.assertThrows(DataAccessException.class, () -> AuthTokens.authenticate(testAuthToken.getAuthToken()), "Invalid authToken should not authenticate the user");
    }
    @Test
    public void successRemoveToken() {
        AuthTokens.add(testAuthToken);
        Assertions.assertDoesNotThrow(() -> AuthTokens.removeToken(testAuthToken.getAuthToken()), "valid Token should be removable");
    }
    @Test
    public void failRemoveToken() {    //without adding to db
        Assertions.assertThrows(DataAccessException.class, () -> AuthTokens.removeToken(testAuthToken.getAuthToken()), "Token successfully removed without even adding it to db");
    }
    @Test
    public void successAuthTokenAdd() {
        AuthTokens.add(testAuthToken);
        try(var preparedStatement = conn.prepareStatement("Select authToken FROM AuthTokens WHERE username=?")){
            preparedStatement.setString(1, testAuthToken.getUsername());
            try (var rs = preparedStatement.executeQuery()) {
                rs.next();
                Assertions.assertEquals(testAuthToken.getAuthToken(), rs.getString("authToken"), "authToken not added");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void failAuthTokenAdd() {   //tries to add null userName token
        AuthToken newTestAuthToken = new AuthToken(null);
        Assertions.assertThrows(RuntimeException.class, () -> AuthTokens.add(newTestAuthToken), "null username should not add");
    }
    @Test
    public void successAuthTokensClear() {
        AuthTokens.add(testAuthToken);
        AuthTokens.clearTokens();
        try(var preparedStatement = conn.prepareStatement("Select * FROM AuthTokens")){
            try (var rs = preparedStatement.executeQuery()) {
                Assertions.assertFalse(rs.next(), "AuthTokens should be empty");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Users tests
    @Test
    public void successUserAdd() {
        Assertions.assertDoesNotThrow(() -> Users.add(testUser));
        try(var preparedStatement = conn.prepareStatement("Select * FROM Users")){
            try (var rs = preparedStatement.executeQuery()) {
                rs.next();
                Assertions.assertEquals(testUser.getUsername(), rs.getString("username"), "username not correctly stored");
                Assertions.assertEquals(testUser.getPassword(), rs.getString("password"), "password not correctly stored");
                Assertions.assertEquals(testUser.getEmail(), rs.getString("email"), "email not correctly stored");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void failUserAdd() {    //adding twice
        try{
            Users.add(testUser);
        } catch(DataAccessException e){
            //ignore
        }
        Assertions.assertThrows(DataAccessException.class, () -> Users.add(testUser), "User should not be added twice");
    }
    @Test
    public void successUserAuthenticate() {
        try{
            Users.add(testUser);
        } catch(DataAccessException e){
            //ignore
        }
        Assertions.assertDoesNotThrow(() -> Users.authenticate(testUser), "User login should succeed after registration");
    }
    @Test
    public void failUserAuthenticate() {    //authenticate without being in db
        Assertions.assertThrows(DataAccessException.class, () -> Users.authenticate(testUser), "User login success without being in db");
    }
    @Test
    public void successUsersClear() {
        try{
            Users.add(testUser);
        } catch(DataAccessException e){
            //ignore
        }
        AuthTokens.clearTokens();
        try(var preparedStatement = conn.prepareStatement("Select * FROM AuthTokens")){
            try (var rs = preparedStatement.executeQuery()) {
                Assertions.assertFalse(rs.next(), "Users should be empty");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Games tests
    @Test
    public void successGameAdd() {
        Assertions.assertDoesNotThrow(() -> Games.add(testGame), "valid add throws exception");
        try(var preparedStatement = conn.prepareStatement("Select * FROM Games")){
            try (var rs = preparedStatement.executeQuery()) {
                rs.next();
                Assertions.assertEquals(testGame.getGameID(), rs.getInt("gameID"), "gameID not correctly stored");
                Assertions.assertEquals(testGame.getGameName(), rs.getString("gameName"), "gameName not correctly stored");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void failGameAdd() {    //adding game with null gameName
        testGame.setGameName(null);
        Assertions.assertThrows(DataAccessException.class, () -> Games.add(testGame), "should throw exception for null game");
    }
    @Test
    public void successGetAllGames() {
        try{
            Games.add(testGame);
        } catch(DataAccessException e){
            //ignore
        }
        Assertions.assertNotNull(Games.getAllGames(), "should return Games not null");
        for(Game game : Games.getAllGames()){
            Assertions.assertEquals(testGame, game, "game in db is not the same as added game");
        }
    }
    @Test
    public void failGetAllGames() {    //listing games without any games in db. Not really a fail, but returns empty
        Assertions.assertEquals(new HashSet<Game>(), Games.getAllGames(), "Games should be empty");
    }
    @Test
    public void successClaimSpot() {
        try{
            Games.add(testGame);
        } catch(DataAccessException e){
            //ignore
        }
        Assertions.assertDoesNotThrow(() -> Games.claimSpot("username", testGame.getGameID(), "BLACK"), "claimSpot throws error on valid request");
        Game expectedGame = new Game();
        expectedGame.setGameID(testGame.getGameID());
        expectedGame.setGameName(testGame.getGameName());
        expectedGame.setChessGame(testGame.getChessGame());
        expectedGame.setGameName(testGame.getGameName());
        expectedGame.setObservers(testGame.getObservers());
        expectedGame.setBlackUsername("username");
        for(Game game : Games.getAllGames()){
            Assertions.assertEquals(expectedGame, game, "game in db not updated to reflect the spot that was claimed");
        }
    }
    @Test
    public void failClaimSpot() {    //claim spot with no game in db
        Assertions.assertThrows(DataAccessException.class, () -> Games.claimSpot("username", testGame.getGameID(), "BLACK"), "spot claimed even though no game exists");
    }
    @Test
    public void successGetNumGames() {
        try{
            Games.add(testGame);
        } catch(DataAccessException e){
            //ignore
        }
        Assertions.assertEquals(1, Games.getNumGames(), "wrong number of games in db");
    }
    @Test
    public void failGetNumGames() {    //getting numGames without any games in db. Not really a fail, but returns zero
        Assertions.assertEquals(0, Games.getNumGames(), "wrong number of games in db");
    }
    @Test
    public void successGamesClear() {
        try{
            Games.add(testGame);
        } catch(DataAccessException e){
            //ignore
        }
        Games.clearGames();
        try(var preparedStatement = conn.prepareStatement("Select * FROM Games")){
            try (var rs = preparedStatement.executeQuery()) {
                Assertions.assertFalse(rs.next(), "no game should exist after clear");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
