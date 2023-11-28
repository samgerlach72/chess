package client;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import chess.ChessPiece;
import com.google.gson.*;


import requests.*;
import responses.*;
public class ServerFacade {
    public static RegisterResponse register(RegisterRequest registerRequest){
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/user"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(registerRequest)))
                    .build();
            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(postResponse.body(), RegisterResponse.class);
        } catch(URISyntaxException | InterruptedException | IOException e){
            throw new RuntimeException(e);
        }
    }
    public static LoginResponse login(LoginRequest loginRequest){
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/session"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(loginRequest)))
                    .build();
            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(postResponse.body(), LoginResponse.class);
        } catch(URISyntaxException | InterruptedException | IOException e){
            throw new RuntimeException(e);
        }
    }
    public static LogoutResponse logout(String authToken){
        HttpClient client = HttpClient.newHttpClient();
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/session"))
                    .header("Authorization", authToken)  // Include the AuthToken in the Authorization header
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), LogoutResponse.class);
        } catch(URISyntaxException | InterruptedException | IOException e){
            throw new RuntimeException(e);
        }
    }
    public static ListGamesResponse list(String authToken){
        HttpClient client = HttpClient.newHttpClient();
        try{
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
        } catch(URISyntaxException | InterruptedException | IOException e){
            throw new RuntimeException(e);
        }
    }
    public static CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken){
        HttpClient client = HttpClient.newHttpClient();
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/game"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authToken)  // Include the AuthToken in the Authorization header
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(createGameRequest)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), CreateGameResponse.class);
        } catch(URISyntaxException | InterruptedException | IOException e){
            throw new RuntimeException(e);
        }
    }
    public static JoinGameResponse joinGame(JoinGameRequest joinGameRequest, String authToken){
        HttpClient client = HttpClient.newHttpClient();
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/game"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authToken)  // Include the AuthToken in the Authorization header
                    .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(joinGameRequest)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), JoinGameResponse.class);
        } catch(URISyntaxException | InterruptedException | IOException e){
            throw new RuntimeException(e);
        }
    }
    public static ClearApplicationResponse clearApplication(){
        HttpClient client = HttpClient.newHttpClient();
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/db"))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), ClearApplicationResponse.class);
        } catch(URISyntaxException | InterruptedException | IOException e){
            throw new RuntimeException(e);
        }
    }

    private static class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
        @Override
        public ChessPiece deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String pieceType = jsonObject.get("pieceType").getAsString();
            return switch (pieceType) {
                case "ROOK" -> context.deserialize(json, chess.ChessPieceImpl.Rook.class);
                case "QUEEN" -> context.deserialize(json, chess.ChessPieceImpl.Queen.class);
                case "PAWN" -> context.deserialize(json, chess.ChessPieceImpl.Pawn.class);
                case "KNIGHT" -> context.deserialize(json, chess.ChessPieceImpl.Knight.class);
                case "KING" -> context.deserialize(json, chess.ChessPieceImpl.King.class);
                case "BISHOP" -> context.deserialize(json, chess.ChessPieceImpl.Bishop.class);
                default -> throw new IllegalStateException("Unexpected value: " + pieceType);
            };
        }
    }
}
