package DAOs;

import Iterator.CollectionIterator;
import Models.Group;
import Models.ItemCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupDaoImpl extends ObjectsDaoImpl implements GroupDao {

    private static ItemCollection<Group> groupsCollection = new ItemCollection<>("Group");
    private DatabaseConnection database = DatabaseConnection.getInstance();
    private Connection connection;

    public GroupDaoImpl() {
        connection = database.getConnection();
    }

    public void importGroups() {
        String query = "SELECT * FROM groups";
        try {
            ArrayList<ArrayList<String>> group = getAllObjectsFromDatabase(connection, query);
            for(int i =0; i < group.size(); i++){
                int groupId = Integer.parseInt(group.get(i).get(0));
                String groupName = group.get(i).get(1);
                Group newGroup = new Group(groupId, groupName);
                addGroup(newGroup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGroupToDatabase(Group group) throws SQLException {
        String query = "INSERT INTO groups (name) VALUES (?);";
        PreparedStatement prepareInsert = connection.prepareStatement(query);
        prepareInsert.setString(1, group.getGroupName());
        prepareInsert.executeUpdate();
    }

    public ItemCollection<Group> getGroups(){
        return groupsCollection;
    }

    public void addGroup(Group group){
        groupsCollection.add(group);
    }

    public Group getGroupByName(String name) {
        groupsCollection.clear();
        importGroups();
        CollectionIterator<Group> groupIterator = groupsCollection.getIterator();
        while(groupIterator.hasNext()){
            Group group = groupIterator.next();
            if (group.getGroupName().equals(name)){
                return group;
            }
        }
        return null;
    }
}
