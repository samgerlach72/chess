package handlers;

public class GetResponseStatus {
    public static int getResponseStatus(String message){
        if(message == null){
            return 200;
        }
        else if(message.equals("Error: bad request")){
            return 400;
        }
        else if(message.equals("Error: unauthorized")){
            return 401;
        }
        else if(message.equals("Error: already taken")){
            return 403;
        }
        else{
            return 500;
        }
    }
}
