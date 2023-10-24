package handlers;
import com.google.gson.Gson;
import requests.RegisterRequest;
import responses.RegisterResponse;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public static String register(Request req, Response res) {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResponse registerResponse = RegisterService.register(registerRequest);
        res.status(GetResponseStatus.getResponseStatus(registerResponse.getMessage()));
        res.type("application/json");
        return new Gson().toJson(registerResponse);
    }
}
