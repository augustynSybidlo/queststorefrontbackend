package DAOs;

import Models.ItemCollection;
import Models.Quest;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QuestDao {
    void importQuests();
    Quest getQuestById(int id);
    ItemCollection<Quest> getQuests();
    void addQuest(Quest quest);
    void addQuestToDatabase(Quest quest) throws SQLException;
    void editQuestOnDatabase(Quest quest) throws SQLException;
    ResultSet chooseStudentQuest(int studentID) throws SQLException;
    ResultSet displayStudentQuest(int studentID) throws SQLException;
    void setQuestStatusAsDone(int questID) throws SQLException;
    void addQuestToStudent(int questID, int studentID) throws SQLException;
    boolean userDontHaveQuest(int choice, int studentId) throws SQLException;
}
