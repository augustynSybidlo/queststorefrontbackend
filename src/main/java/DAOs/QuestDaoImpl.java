package DAOs;

import Iterator.CollectionIterator;
import Models.ItemCollection;
import Models.Quest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuestDaoImpl extends ObjectsDaoImpl implements QuestDao {

    private static ItemCollection<Quest> questsCollection = new ItemCollection<>("Quests");
    private DatabaseConnection database = DatabaseConnection.getInstance();
    private Connection connection;
    private final static int idIndex = 0;
    private final static int nameIndex = 1;
    private final static int rewardIndex = 2;
    private final static int categoryIndex = 3;

    public QuestDaoImpl() {
        connection = database.getConnection();
    }

    public void importQuests() {
        questsCollection.clear();
        String query = "SELECT * FROM quests";
        try {
            ArrayList<ArrayList<String>> quests = getAllObjectsFromDatabase(connection, query);

            for(int i=0; i< quests.size(); i++) {

                int id = Integer.parseInt(quests.get(i).get(idIndex));
                String name = quests.get(i).get(nameIndex);
                int reward = Integer.parseInt(quests.get(i).get(rewardIndex));
                String category = quests.get(i).get(categoryIndex);

                Quest quest = new Quest(id, name, reward, category);
                addQuest(quest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Quest getQuestById(int id) {
        CollectionIterator<Quest> questIterator = questsCollection.getIterator();

        while (questIterator.hasNext()){
            Quest quest = questIterator.next();

            if(quest.getQuestId() == id) {
                return quest;
            }
        }
        return null;
    }

    public ItemCollection<Quest> getQuests() {
        importQuests();
        return questsCollection;
    }

    public void addQuest(Quest quest){
        questsCollection.add(quest);
    }

    public void addQuestToDatabase(Quest quest) throws SQLException {
        String query = "INSERT INTO quests (name, reward, category) VALUES (?, ?, ?)";
        PreparedStatement prepareInsert = connection.prepareStatement(query);
        prepareInsert.setString(1, quest.getQuestName());
        prepareInsert.setInt(2, quest.getQuestReward());
        prepareInsert.setString(3, quest.getQuestCategoryName());
        prepareInsert.executeUpdate();
        importQuests();
    }

    public void editQuestOnDatabase(Quest quest) throws SQLException {
        String query = "UPDATE quests SET name= ? " +
                ", reward= ? " +
                ", category= ? " +
                "WHERE id= ?;";
        PreparedStatement prepareUpdate = connection.prepareStatement(query);
        prepareUpdate.setString(1, quest.getQuestName());
        prepareUpdate.setInt(2, quest.getQuestReward());
        prepareUpdate.setString(3, quest.getQuestCategoryName());
        prepareUpdate.setInt(4, quest.getQuestId());
        prepareUpdate.executeUpdate();
    }

    public ResultSet chooseStudentQuest(int studentID) throws SQLException {
        String query = "SELECT student_quests.quests_id AS ID, " +
                "student_quests.student_id AS Student, " +
                "quests.name AS 'quest name', " +
                "student_quests.status FROM student_quests " +
                "JOIN quests ON student_quests.quests_id=quests.id " +
                "WHERE student_quests.student_id = ?" +
                " AND student_quests.status = 'not done';";
        PreparedStatement prepareSelect = connection.prepareStatement(query);
        prepareSelect.setInt(1, studentID);
        ResultSet resultSet = prepareSelect.executeQuery();
        return resultSet;
    }

    public ResultSet displayStudentQuest(int studentID) throws SQLException {
        String query = "SELECT quests.name AS 'quest name', student_quests.status " +
                "FROM student_quests JOIN quests ON student_quests.quests_id=quests.id " +
                "WHERE student_quests.student_id = ?;";
        PreparedStatement prepareSelect = connection.prepareStatement(query);
        prepareSelect.setInt(1, studentID);
        ResultSet resultSet = prepareSelect.executeQuery();
        return resultSet;
    }

    public void setQuestStatusAsDone(int questID) throws SQLException {
        String query = "UPDATE student_quests SET status = 'done' WHERE quests_id = ?";
        PreparedStatement prepareUpdate = connection.prepareStatement(query);
        prepareUpdate.setInt(1, questID);
        prepareUpdate.executeUpdate();
    }

    public void addQuestToStudent(int questID, int studentID) throws SQLException {
        String query = "INSERT INTO student_quests (quests_id, student_id, status) VALUES (?, ?, 'not done')";
        PreparedStatement prepareInsert = connection.prepareStatement(query);
        prepareInsert.setInt(1, questID);
        prepareInsert.setInt(2, studentID);
        prepareInsert.executeUpdate();
    }

    public boolean userDontHaveQuest(int choice, int studentId) throws SQLException {
        String idQuery = "SELECT quests_id FROM student_quests " +
                "WHERE quests_id= ? " +
                " AND student_id= ?;";
        PreparedStatement prepareSelect = connection.prepareStatement(idQuery);
        prepareSelect.setInt(1, choice);
        prepareSelect.setInt(2,studentId);
        ResultSet resultSet = prepareSelect.executeQuery();
        int questId = resultSet.getInt("quests_id");

        if(questId == choice){
            return false;
        } else {
            return true;
        }
    }
}
