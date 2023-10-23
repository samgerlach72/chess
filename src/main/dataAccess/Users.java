package dataAccess;
import models.User;

import java.util.HashSet;

public class Users {
    //singleton instance
    private static Users instance;
    //Private constructor to prevent external instantiation
    private Users(){}
    //Public method to access the singleton instance
    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }
    private HashSet<User> users;
    public void clearUsers(){}
    void CreateUser(User user) throws DataAccessException{}
    boolean authenticateUser(String username, String password){
        return false;
    }
}
