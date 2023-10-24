package handlers;
import com.google.gson.Gson;
import responses.ClearApplicationResponse;
import services.ClearApplicationService;
import spark.Request;
import spark.Response;

public class ClearApplicationHandler {
    public static String clearApplication(Request req, Response res) {
        ClearApplicationResponse clearApplicationResponse = new ClearApplicationService().clearApplication();
        if(clearApplicationResponse.getMessage() == null){
            res.status(200);
        }
        else{
            res.status(500);
        }
        res.type("application/json");
        return new Gson().toJson(clearApplicationResponse);
    }
}
