package DAOs;

import Models.ExperienceLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExperienceLevelsDaoImpl extends ObjectsDaoImpl implements ExperienceLevelsDao {


    private DatabaseConnection database = DatabaseConnection.getInstance();
    private Connection connection;
    private final static int moneyRequiredIndex = 0;
    private final static int experienceLevelNameIndex = 1;

    public ExperienceLevelsDaoImpl() {
        connection = database.getConnection();
    }

    public void importExperienceLevels() {
        String query = "SELECT * FROM experience_levels";
        try {
            ArrayList<ArrayList<String>> experience = getAllObjectsFromDatabase(connection, query);
            for(int i =0; i < experience.size(); i++) {

                int moneyRequired = Integer.parseInt(experience.get(i).get(moneyRequiredIndex));
                String name = experience.get(i).get(experienceLevelNameIndex);

                ExperienceLevel experienceLevel = new ExperienceLevel(moneyRequired, name);
                experienceLevel.addExperienceLevel(experienceLevel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addExperienceLevelToDatabase(ExperienceLevel experienceLevel) throws SQLException {
        String query = "INSERT INTO experience_levels (name, money_required) VALUES (?, ?);";
        PreparedStatement prepareInsert = connection.prepareStatement(query);
        prepareInsert.setString(1, experienceLevel.getLevelName());
        prepareInsert.setInt(2, experienceLevel.getLevelMoneyRequired());
        prepareInsert.executeUpdate();
    }
}
