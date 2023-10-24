package handlers;
import com.google.gson.Gson;
import responses.LogoutResponse;
import services.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    public static String logout(Request req, Response res) {
        String authToken = req.headers("Authorization");
        LogoutResponse logoutResponse = LogoutService.logout(authToken);
        res.status(GetResponseStatus.getResponseStatus(logoutResponse.getMessage()));
        res.type("application/json");
        return new Gson().toJson(logoutResponse);
    }
}
