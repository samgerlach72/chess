package serverFacadeTests;

import client.ServerFacade;
import models.Game;
import org.junit.jupiter.api.*;
import requests.*;
import responses.*;
import java.util.HashSet;
import java.util.Locale;

public class ServerFacadeTests {
    private static RegisterRequest existingUser;
    private static RegisterRequest newUser;
    private static CreateGameRequest createRequest;
    private String existingAuth;

    @BeforeAll
    public static void init() {
        existingUser = new RegisterRequest("username1", "password1", "email1@example.com");
        newUser = new RegisterRequest("username2", "password2", "email@example2.com");
        createRequest = new CreateGameRequest("testGame");
    }
    @BeforeEach
    public void setup() {
        ServerFacade.clearApplication();
        //registering user
        RegisterResponse response = ServerFacade.register(existingUser);
        //obtaining authToken
        existingAuth = response.getAuthToken();
    }
    @Test
    public void successRegisterUser() {
        RegisterResponse registerResult = ServerFacade.register(newUser);

        Assertions.assertFalse(
                registerResult.getMessage() != null && registerResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");
        Assertions.assertEquals(newUser.getUsername(), registerResult.getUsername(),
                "Response did not have the same username as was registered");
        Assertions.assertNotNull(registerResult.getAuthToken(), "Response did not contain an authentication string");
    }
    @Test
    public void registerTwice() {
        RegisterResponse registerResult = ServerFacade.register(existingUser);

        Assertions.assertTrue(registerResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Response missing error message");
        Assertions.assertNull(registerResult.getUsername(), "Response incorrectly contained username");
        Assertions.assertNull(registerResult.getAuthToken(), "Response incorrectly contained authentication string");
    }
    @Test
    public void successLogin() {
        LoginRequest loginRequest = new LoginRequest(existingUser.getUsername(), existingUser.getPassword());
        LoginResponse loginResult = ServerFacade.login(loginRequest);

        Assertions.assertFalse(
                loginResult.getMessage() != null && loginResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Response returned error message");
        Assertions.assertEquals(existingUser.getUsername(), loginResult.getUsername(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(loginResult.getAuthToken(), "Response did not return authentication String");
    }
    @Test
    public void loginWrongPassword() {
        LoginRequest loginRequest = new LoginRequest(existingUser.getUsername(), "wrong password");
        LoginResponse loginResult = ServerFacade.login(loginRequest);

        Assertions.assertTrue(
                loginResult.getMessage() != null && loginResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Response did not return error message");
        Assertions.assertNull(loginResult.getUsername(), "Response incorrectly returned username");
        Assertions.assertNull(loginResult.getAuthToken(), "Response incorrectly return authentication String");
    }
    @Test
    public void successLogout() {
        //log out existing user
        LogoutResponse result = ServerFacade.logout(existingAuth);

        Assertions.assertFalse(result.getMessage() != null &&
                        result.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");
    }
    @Test
    public void failLogout() {
        //log out user twice
        //second logout should fail
        ServerFacade.logout(existingAuth);
        LogoutResponse result = ServerFacade.logout(existingAuth);

        Assertions.assertTrue(result.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Response did not return error message");
    }
    @Test
    public void successList() {
        //register a few users to create games
        RegisterRequest registerRequest = new RegisterRequest("a", "A", "a.A");
        RegisterResponse userA = ServerFacade.register(registerRequest);

        registerRequest = new RegisterRequest("b", "B", "b.B");
        RegisterResponse userB = ServerFacade.register(registerRequest);

        //create games

        //1 as black from A
        createRequest = new CreateGameRequest("I'm numbah one!");
        CreateGameResponse game1 = ServerFacade.createGame(createRequest, userA.getAuthToken());
        ServerFacade.joinGame(new JoinGameRequest("BLACK", game1.getGameID()), userA.getAuthToken());

        //1 as white from B
        createRequest = new CreateGameRequest("Lonely");
        CreateGameResponse game2 = ServerFacade.createGame(createRequest, userB.getAuthToken());
        ServerFacade.joinGame(new JoinGameRequest("WHITE", game2.getGameID()), userB.getAuthToken());

        //create expected entry items
        HashSet<Game> expectedList = new HashSet<>();

        //game 1
        Game newGame = new Game("I'm numbah one!");
        newGame.setGameID(game1.getGameID());
        newGame.setBlackUsername(userA.getUsername());
        newGame.setWhiteUsername(null);
        expectedList.add(newGame);

        //game2
        newGame = new Game("Lonely");
        newGame.setGameID(game2.getGameID());
        newGame.setBlackUsername(null);
        newGame.setWhiteUsername(userB.getUsername());
        expectedList.add(newGame);

        HashSet<Game> returnedList = ServerFacade.list(existingAuth).getGames();

        //check
        Assertions.assertEquals(expectedList, returnedList, "Returned Games list was incorrect");
    }
    @Test
    public void badAuthList() {
        //register a few users to create games
        RegisterRequest registerRequest = new RegisterRequest("a", "A", "a.A");
        RegisterResponse userA = ServerFacade.register(registerRequest);

        registerRequest = new RegisterRequest("b", "B", "b.B");
        RegisterResponse userB = ServerFacade.register(registerRequest);

        //create games

        //1 as black from A
        createRequest = new CreateGameRequest("I'm numbah one!");
        ServerFacade.createGame(createRequest, userA.getAuthToken());

        //1 as white from B
        createRequest = new CreateGameRequest("Lonely");
        ServerFacade.createGame(createRequest, userB.getAuthToken());

        //logging out
        ServerFacade.logout(existingAuth);
        //listGames with bad auth
        ListGamesResponse listResult = ServerFacade.list(existingAuth);

        //check
        Assertions.assertTrue(listResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Bad result did not return an error message");
        Assertions.assertNull(listResult.getGames(), "Bad result returned a list of Games");
    }
    @Test
    public void successCreate() {
        CreateGameResponse createResult = ServerFacade.createGame(createRequest, existingAuth);

        Assertions.assertFalse(
                createResult.getMessage() != null && createResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
        Assertions.assertNotNull(createResult.getGameID(), "Result did not return a game ID");
        Assertions.assertTrue(createResult.getGameID() > 0, "Result returned invalid game ID");
    }
    @Test
    public void badAuthCreate() {
        //log out user so auth is invalid
        ServerFacade.logout(existingAuth);

        CreateGameResponse createResult = ServerFacade.createGame(createRequest, existingAuth);

        Assertions.assertTrue(createResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Bad result did not return an error message");
        Assertions.assertNull(createResult.getGameID(), "Bad result returned a game ID");
    }
    @Test
    public void successJoin() {
        //create game
        CreateGameResponse createResult = ServerFacade.createGame(createRequest, existingAuth);

        //join as white
        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", createResult.getGameID());

        //try join
        JoinGameResponse joinResult = ServerFacade.joinGame(joinRequest, existingAuth);

        //check
        Assertions.assertFalse(
                joinResult.getMessage() != null && joinResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");

        ListGamesResponse listResult = ServerFacade.list(existingAuth);
        Assertions.assertEquals(1, listResult.getGames().size());

        //getting single element of hashSet
        Game onlyGame = null;
        for (Game game: listResult.getGames()){
            onlyGame = game;
        }
        assert onlyGame != null;

        Assertions.assertEquals(existingUser.getUsername(), onlyGame.getWhiteUsername());
        Assertions.assertNull(onlyGame.getBlackUsername());
    }
    @Test
    public void badColorJoin() {
        //create game
        CreateGameResponse createResult = ServerFacade.createGame(createRequest, existingAuth);

        //add existing user as black
        JoinGameRequest joinRequest = new JoinGameRequest("BLACK", createResult.getGameID());
        ServerFacade.joinGame(joinRequest, existingAuth);

        //register second user
        RegisterResponse registerResult = ServerFacade.register(newUser);

        //join request trying to also join  as black
        joinRequest = new JoinGameRequest("BLACK", createResult.getGameID());
        JoinGameResponse joinResult = ServerFacade.joinGame(joinRequest, registerResult.getAuthToken());

        //check failed
        Assertions.assertTrue(
                joinResult.getMessage() != null && joinResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }
    @Test
    public void clearSuccess() {
        //register a few users to create games
        RegisterRequest registerRequest = new RegisterRequest("a", "A", "a.A");
        RegisterResponse userA = ServerFacade.register(registerRequest);

        registerRequest = new RegisterRequest("b", "B", "b.B");
        RegisterResponse userB = ServerFacade.register(registerRequest);

        //create games

        //1 as black from A
        createRequest = new CreateGameRequest("I'm numbah one!");
        ServerFacade.createGame(createRequest, userA.getAuthToken());

        //1 as white from B
        createRequest = new CreateGameRequest("Lonely");
        ServerFacade.createGame(createRequest, userB.getAuthToken());

        ClearApplicationResponse clearResult = ServerFacade.clearApplication();
        //listing games after clear to check if empty
        ListGamesResponse listResult = ServerFacade.list(existingAuth);

        //check
        Assertions.assertFalse(
                clearResult.getMessage() != null && clearResult.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
        Assertions.assertNull(listResult.getGames(), "list of games is not null after clear");
    }
}

