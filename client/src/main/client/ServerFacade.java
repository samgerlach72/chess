package client;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import chess.ChessGameImpl;
import chess.ChessPiece;
import com.google.gson.*;


import requests.*;
import responses.*;
public class ServerFacade {
    public static RegisterResponse register(RegisterRequest registerRequest) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(registerRequest)))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(postResponse.body(), RegisterResponse.class);
    }
    public static LoginResponse login(LoginRequest loginRequest) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(loginRequest)))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(postResponse.body(), LoginResponse.class);
    }
    public static LogoutResponse logout(String authToken) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/session"))
                .header("Authorization", authToken)  // Include the AuthToken in the Authorization header
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), LogoutResponse.class);
    }
    public static ListGamesResponse list(String authToken) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/game"))
                .header("Authorization", authToken)  // Include the AuthToken in the Authorization header
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(response.body(), ListGamesResponse.class);
    }
    public static CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/game"))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)  // Include the AuthToken in the Authorization header
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(createGameRequest)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), CreateGameResponse.class);
    }
    public static JoinGameResponse joinGame(JoinGameRequest joinGameRequest, String authToken) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/game"))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)  // Include the AuthToken in the Authorization header
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(joinGameRequest)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), JoinGameResponse.class);
    }
    public static ClearApplicationResponse clearApplication(String authToken) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/db"))
                .header("Authorization", authToken)  // Include the AuthToken in the Authorization header
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), ClearApplicationResponse.class);
    }

    public static class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
        @Override
        public ChessPiece deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String pieceType = jsonObject.get("pieceType").getAsString();

            switch (pieceType) {
                case "ROOK":
                    return context.deserialize(json, chess.ChessPieceImpl.Rook.class);
                case "QUEEN":
                    return context.deserialize(json, chess.ChessPieceImpl.Queen.class);
                case "PAWN":
                    return context.deserialize(json, chess.ChessPieceImpl.Pawn.class);
                case "KNIGHT":
                    return context.deserialize(json, chess.ChessPieceImpl.Knight.class);
                case "KING":
                    return context.deserialize(json, chess.ChessPieceImpl.King.class);
                case "BISHOP":
                    return context.deserialize(json, chess.ChessPieceImpl.Bishop.class);
                default:
                    throw new IllegalStateException("Unexpected value: " + pieceType);
            }
        }
    }
}
