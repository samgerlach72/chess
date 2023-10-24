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
    private HashSet<User> users = new HashSet<>();
    public void clearUsers(){
        users.clear();
    }
    public void addUser(User user) throws DataAccessException{
        if(user.getEmail() == null || user.getUsername() == null || user.getPassword() ==null){
            throw new DataAccessException("Error: bad request");
        }
        for(User existingUser: users){
            if(existingUser.getUsername().equals(user.getUsername())){
                throw new DataAccessException("Error: already taken");
            }
            if(existingUser.getEmail().equals(user.getEmail())){
                throw new DataAccessException("Error: already taken");
            }
        }
        users.add(user);
    }
    public void authenticateUser(User user) throws DataAccessException{    //if it hits return, user is authenticated
        for(User existingUser: users){
            if(existingUser.getUsername().equals(user.getUsername())){
                if(existingUser.getPassword().equals(user.getPassword())){
                    return;
                }
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }
}
