package DAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ObjectsDao {
    ArrayList<ArrayList<String>> getAllObjectsFromDatabase(Connection connection, String query) throws SQLException;
}
