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
        for(User existingUser: users){
            if(existingUser.getUsername().equals(user.getUsername())){
                throw new DataAccessException("already taken");
            }
            if(existingUser.getEmail().equals(user.getEmail())){
                throw new DataAccessException("already taken");
            }
        }
        if(!users.add(user)){
            throw new DataAccessException("should not be equal error"); //fixme
        }
    }
    boolean authenticateUser(User user){
        return users.contains(user);
    }
}
