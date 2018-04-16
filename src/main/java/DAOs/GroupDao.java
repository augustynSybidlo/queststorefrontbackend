package DAOs;

import Models.Group;
import Models.ItemCollection;

import java.sql.SQLException;

public interface GroupDao {
    void importGroups();
    void addGroupToDatabase(Group group) throws SQLException;
    ItemCollection<Group> getGroups();
    void addGroup(Group group);
    Group getGroupByName(String name);

}
