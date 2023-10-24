package services;
import dataAccess.AuthTokens;
import dataAccess.Games;
import dataAccess.Users;
import responses.ClearApplicationResponse;

public class ClearApplicationService {
    public ClearApplicationResponse clearApplication() {
        ClearApplicationResponse response = new ClearApplicationResponse();
        Users.getInstance().clearUsers();
        Games.getInstance().clearGames();
        AuthTokens.getInstance().clearTokens();
        return response;
    }
}
