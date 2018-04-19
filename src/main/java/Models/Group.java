package Models;

public class Group {
    private String groupName;
    private int groupId;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(int groupId, String name) {
        this.groupId = groupId;
        this.groupName = name;
    }

    public String getGroupName(){
        return this.groupName;
    }

    public int getGroupId() { return this.groupId; }

    @Override
    public String toString() {
        return "Group Name ='" + groupName + '\'' +
                "Group Id = " + groupId;
    }
}
