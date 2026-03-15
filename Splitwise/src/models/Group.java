package models;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String groupId;
    private String groupName;
    private List<String> memberIds;
    private List<String> expenseIds;

    public Group(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberIds = new ArrayList<>();
        this.expenseIds = new ArrayList<>();
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public List<String> getExpenseIds() {
        return expenseIds;
    }

    public void addMember(String userId) {
        if (!memberIds.contains(userId)) {
            memberIds.add(userId);
        }
    }

    public void addExpense(String expenseId) {
        expenseIds.add(expenseId);
    }

    @Override
    public String toString() {
        return "Group{"
                + "groupId='" + groupId + '\''
                + ", groupName='" + groupName + '\''
                + ", members=" + memberIds.size()
                + '}';
    }
}
