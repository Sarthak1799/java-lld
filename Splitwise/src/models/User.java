package models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userId;
    private String name;
    private String email;
    private String phone;
    private List<String> groupIds;

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.groupIds = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void addGroup(String groupId) {
        this.groupIds.add(groupId);
    }

    @Override
    public String toString() {
        return "User{"
                + "userId='" + userId + '\''
                + ", name='" + name + '\''
                + '}';
    }
}
