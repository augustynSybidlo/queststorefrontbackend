package DAOs;

import Models.ExperienceLevel;

import java.sql.SQLException;

public interface ExperienceLevelsDao {
    void importExperienceLevels();
    void addExperienceLevelToDatabase(ExperienceLevel experienceLevel) throws SQLException;

}
