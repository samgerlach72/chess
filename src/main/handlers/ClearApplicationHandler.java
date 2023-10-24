package handlers;
import com.google.gson.Gson;
import responses.ClearApplicationResponse;
import services.ClearApplicationService;
import spark.Request;
import spark.Response;

public class ClearApplicationHandler {
    public static String clearApplication(Request req, Response res) {
        ClearApplicationResponse clearApplicationResponse = ClearApplicationService.clearApplication();
        res.status(GetResponseStatus.getResponseStatus(clearApplicationResponse.getMessage()));
        res.type("application/json");
        return new Gson().toJson(clearApplicationResponse);
    }
}
