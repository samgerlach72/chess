package dataAccess;
import models.AuthToken;
import java.util.HashSet;

import java.sql.SQLException;
import dataAccess.Database;

public class AuthTokens {
    public static String authenticate(String authTokenString) throws DataAccessException{  //returns username of user if authentication succeeds
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("SELECT username FROM AuthTokens WHERE authToken=?")){
                preparedStatement.setString(1, authTokenString);
                try (var rs = preparedStatement.executeQuery()) {
                    if(rs.next()) {
//                        System.out.println(rs.getString("username"));
                        return rs.getString("username");
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        throw new DataAccessException("Error: unauthorized");   //if rs returns empty, authentication fails
    }
    public static void removeToken(String authTokenString) throws DataAccessException{
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("DELETE FROM AuthTokens WHERE authToken=?")){
                preparedStatement.setString(1, authTokenString);
                if (preparedStatement.executeUpdate() != 0) {   //if entries deleted return
                    return;
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        throw new DataAccessException("Error: unauthorized");
    }
    public static void add(AuthToken authToken){
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("INSERT INTO AuthTokens (username, authToken) VALUES(?, ?)")){
                preparedStatement.setString(1, authToken.getUsername());
                preparedStatement.setString(2, authToken.getAuthToken());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static void clearTokens(){
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE AuthTokens")){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
