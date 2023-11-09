package services;
import dataAccess.AuthTokens;
import dataAccess.Games;
import dataAccess.Users;
import responses.ClearApplicationResponse;

public class ClearApplicationService {
    public static ClearApplicationResponse clearApplication() {
        ClearApplicationResponse response = new ClearApplicationResponse();
        Users.clearUsers();
        Games.clearGames();
        AuthTokens.clearTokens();
        return response;
    }
}
