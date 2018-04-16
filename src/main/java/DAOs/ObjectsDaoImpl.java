package DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

abstract class ObjectsDaoImpl implements ObjectsDao {

    public ArrayList<ArrayList<String>> getAllObjectsFromDatabase(Connection connection, String query) throws SQLException {
        ArrayList<ArrayList<String>> allObjects = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        // Group all column names from query result
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<String> columnNames = new ArrayList<String>();

        for (int row = 1; row <= columnCount; row++){
            String columnName = metaData.getColumnName(row).toString();
            columnNames.add(columnName);
        }

        while(result.next()){
            ArrayList<String> rowResult = new ArrayList<String>();
            for (int i = 0; i < columnNames.size(); i++){
                rowResult.add(result.getString(columnNames.get(i)));
            }
            allObjects.add(rowResult);
        }
        return allObjects;
    }
}

