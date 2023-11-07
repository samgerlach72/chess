package dataAccess;
import models.User;

import java.sql.SQLException;
import java.util.HashSet;

public class Users {
    public static void clearUsers(){
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Users")){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static void addUser(User user) throws DataAccessException{
        if(user.getEmail() == null || user.getUsername() == null || user.getPassword() ==null){
            throw new DataAccessException("Error: bad request");
        }
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try (var checkStatement = conn.prepareStatement("SELECT username, email FROM Users WHERE username = ? OR email = ?")) {
                checkStatement.setString(1, user.getUsername());
                checkStatement.setString(2, user.getEmail());
                try (var rs = checkStatement.executeQuery()) {
                    if (!rs.next()) {
                        try (var preparedStatement = conn.prepareStatement("INSERT INTO Users (username, password, email) VALUES (?, ?, ?)")) {
                            preparedStatement.setString(1, user.getUsername());
                            preparedStatement.setString(2, user.getPassword());
                            preparedStatement.setString(3, user.getEmail());
                            preparedStatement.executeUpdate();
                            return;
                        }
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        throw new DataAccessException("Error: already taken");
    }
    public static void authenticateUser(User user) throws DataAccessException{    //if it hits return, user is authenticated
        try (var conn = Database.getConnection()) {
            conn.setCatalog("chess");
            try (var checkStatement = conn.prepareStatement("SELECT username, password FROM Users WHERE username = ? AND password = ?")) {
                checkStatement.setString(1, user.getUsername());
                checkStatement.setString(2, user.getPassword());
                try (var rs = checkStatement.executeQuery()) {
                    if (rs.next()) {
                        return;
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        throw new DataAccessException("Error: unauthorized");
    }
}
