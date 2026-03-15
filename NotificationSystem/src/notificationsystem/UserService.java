package notificationsystem;
import notificationsystem.models.*;
import java.util.*;

public class UserService implements Subscriber {
    private Map<String, User> users;
    private static UserService instance = null;

    public UserService() {
        users = new HashMap<>(); 
    }

    public static UserService getInstance(){
        if(instance == null)
            instance = new UserService();
        return instance;
    }

    public String registerUser(String name, String username){
        User user = new User(name, username);
        users.put(user.getId(), user);
        return user.getId();
    }

    public User getUser(String userId){
        return users.get(userId);
    }

    @Override
    public void sendNotification(List<String> userIds, Notification notification) {
        for(String id : userIds){
            User user = users.get(id);
            if(user != null){
                user.displayNotification(notification);
            }
        }
    }
}
