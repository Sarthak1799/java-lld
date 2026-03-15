package services;

import models.Group;

import java.util.HashMap;
import java.util.Map;

public class GroupService {

    private Map<String, Group> groups;
    private static GroupService instance;

    private GroupService() {
        this.groups = new HashMap<>();
    }

    public static GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService();
        }
        return instance;
    }

    public void createGroup(Group group) {
        groups.put(group.getGroupId(), group);

        // Add group to each member's group list
        UserService userService = UserService.getInstance();
        for (String userId : group.getMemberIds()) {
            userService.getUser(userId).addGroup(group.getGroupId());
        }
    }

    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    public void addMemberToGroup(String groupId, String userId) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.addMember(userId);
            UserService.getInstance().getUser(userId).addGroup(groupId);
        }
    }

    public Map<String, Group> getAllGroups() {
        return groups;
    }
}
