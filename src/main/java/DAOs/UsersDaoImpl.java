package DAOs;

import Models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsersDaoImpl extends ObjectsDaoImpl implements UsersDao {

    private static ArrayList<User> usersCollection = new ArrayList<User>();
    private DatabaseConnection database = DatabaseConnection.getInstance();
    private Connection connection;
    private static final int idIndex = 0;
    private static final int nameIndex = 1;
    private static final int surnameIndex = 2;
    private static final int passwordIndex = 4;
    private static final int statusIndex = 5;
    private static final int groupIdIndex = 6;
    private static final int experienceIndex = 7;

    public UsersDaoImpl(){
        connection = database.getConnection();

        try {
            importUsersData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getUsersCollection() {
        try {
            importUsersData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersCollection;
    }

    public void addUserToDatabase(User user) throws SQLException {

        String query = "INSERT INTO users (name, surname, login, password," +
                        "status, group_id, experience) VALUES (?,?,?,?,?,?,?);";
        PreparedStatement preparedInsert = connection.prepareStatement(query);
        preparedInsert.setString(1, user.getName());
        preparedInsert.setString(2, user.getSurname());
        preparedInsert.setString(3, user.getLogin());
        preparedInsert.setString(4, user.getPassword());
        preparedInsert.setString(5, user.getStatus());
        preparedInsert.setString(6, String.valueOf(user.getUserGroupId()));
        preparedInsert.setString(7, user.getUserExperienceLevel());
        preparedInsert.executeUpdate();
    }

    public void addStudentWalletToDatabase(Student student) throws SQLException {

        String query = "INSERT INTO wallet (current_balance, total_income, student_id) VALUES(?,?,?);";
        PreparedStatement preparedInsert = connection.prepareStatement(query);
        preparedInsert.setInt(1, student.getStudentWallet());
        preparedInsert.setInt(2, student.getStudentTotalIncome());
        preparedInsert.setInt(3, getUserId(student));
        preparedInsert.executeUpdate();
    }

    public void updateUserGroupInDatabase(User user) throws SQLException {
        String query = "UPDATE users SET group_id = ? WHERE id = ?;";
        PreparedStatement preparedUpdate = connection.prepareStatement(query);
        preparedUpdate.setInt(1, user.getUserGroupId());
        preparedUpdate.setInt(2, user.getId());
        preparedUpdate.executeUpdate();
    }

    public void updateStudentWalletInDatabase(Student student) throws SQLException {
        String query = "UPDATE wallet SET current_balance = ?, total_income = ? " +
                       "WHERE student_id = ?;";
        PreparedStatement preparedUpdate = connection.prepareStatement(query);
        preparedUpdate.setInt(1, student.getStudentWallet());
        preparedUpdate.setInt(2, student.getStudentTotalIncome());
        preparedUpdate.setInt(3, student.getId());
        preparedUpdate.executeUpdate();
    }

    public void addCurrencyToWallet(int questReward, int studentID) throws SQLException {

        int currentBalance = getStudentWalletCurrentBalanceByStudentId(studentID);
        int totalIncome =  getStudentTotalIncomeByStudentId(studentID);

        currentBalance += questReward;
        totalIncome += questReward;

        String query = "UPDATE wallet SET current_balance = ?, total_income = ?" +
                       "WHERE student_id = ?;";
        PreparedStatement preparedUpdate = connection.prepareStatement(query);
        preparedUpdate.setInt(1,currentBalance);
        preparedUpdate.setInt(2, totalIncome);
        preparedUpdate.setInt(3,studentID);
        preparedUpdate.executeUpdate();
    }

    public void updateUserDataInDatabase(User user) throws SQLException {
        String query = "UPDATE users SET name = ?, surname = ?, login = ?, password = ?, status = ?, group_id = ?, experience = ? " +
                       "WHERE id = ?";
        PreparedStatement preparedUpdate = connection.prepareStatement(query);
        preparedUpdate.setString(1, user.getName());
        preparedUpdate.setString(2, user.getSurname());
        preparedUpdate.setString(3, user.getLogin());
        preparedUpdate.setString(4, user.getPassword());
        preparedUpdate.setString(5, user.getStatus());
        preparedUpdate.setInt(6, user.getUserGroupId());
        preparedUpdate.setString(7, user.getUserExperienceLevel());
        preparedUpdate.setInt(8, user.getId());
        preparedUpdate.executeUpdate();
    }

    public ArrayList<User> getAllUsersByStatus(String userStatus) {
        ArrayList<User> usersWithGivenStatus = new ArrayList<User>();
        for (User user : usersCollection){
            if (user.getStatus().equals(userStatus)){
                usersWithGivenStatus.add(user);
            }
        }
        return usersWithGivenStatus;
    }

    public Mentor getMentorById(int id) {
        for (User user : usersCollection){
            if(user.getId() == id && user.getStatus().equals("mentor")){
                return (Mentor)user;
            }
        }
        return null;
    }

    public Student getStudentById(int id) {
        for (User user : usersCollection){
            if(user.getId() == id && user.getStatus().equals("student")){
                return (Student)user;
            }
        }
        return null;
    }

    public void disconnectDatabase(){
        database.closeConnection();
    }

    public void importUsersData() throws SQLException {
        usersCollection.clear();
        String query = "SELECT * FROM users";
        ArrayList<ArrayList<String>> users = getAllObjectsFromDatabase(connection, query);
        for(int i =0; i < users.size(); i++) {
            ArrayList<String> personData = users.get(i);
            User person = createUserObject(personData);
            usersCollection.add(person);
        }
    }

    private User createUserObject(ArrayList<String> personData) throws SQLException {
        int id = Integer.parseInt(personData.get(idIndex));
        String name = personData.get(nameIndex);
        String surname = personData.get(surnameIndex);
        String password = personData.get(passwordIndex);
        String status = personData.get(statusIndex);
        String experience = personData.get(experienceIndex);
        int groupId = 0;

        if (personData.get(groupIdIndex) != null){
            groupId = Integer.parseInt(personData.get(groupIdIndex));
        }

        String groupName = getUserGroupNameByGroupId(groupId);
        Group group = new Group(groupId, groupName);
        User person = null;
        if(status.equals("admin")){
            person = new Admin(name, surname, password);
        }
        else if(status.equals("mentor")){
            person = new Mentor(id, name, surname, password, group);
        }
        else if(status.equals("student")){
            int wallet = getStudentWalletCurrentBalanceByStudentId(id);
            int totalIncome = getStudentTotalIncomeByStudentId(id);
            person = new Student(id, name, surname, password, group, wallet, totalIncome, experience);
        }
        return person;
    }

    private int getUserId(User user) throws SQLException {
        String query = "SELECT id FROM users WHERE login  = ?;";
        PreparedStatement preparedSelect = connection.prepareStatement(query);
        preparedSelect.setString(1, user.getLogin());
        ResultSet queryResult = preparedSelect.executeQuery();
        return queryResult.getInt("id");
    }

    private String getUserGroupNameByGroupId(int groupID) throws SQLException {
        String query = "SELECT name FROM groups WHERE id = ? AND id IS NOT NULL";
        PreparedStatement preparedSelect = connection.prepareStatement(query);
        preparedSelect.setInt(1, groupID);
        ResultSet queryResult = preparedSelect.executeQuery();
        return queryResult.getString("name");
    }

    private int getStudentWalletCurrentBalanceByStudentId(int studentID) throws SQLException {
        String query = "SELECT current_balance FROM wallet WHERE student_id = ?";
        PreparedStatement preparedSelect = connection.prepareStatement(query);
        preparedSelect.setInt(1, studentID);
        ResultSet queryResult = preparedSelect.executeQuery();
        return queryResult.getInt("current_balance");
    }

    private int getStudentTotalIncomeByStudentId(int studentID) throws SQLException {
        String query = "SELECT total_income FROM wallet WHERE student_id = ? AND total_income >= 0;";
        PreparedStatement preparedSelect = connection.prepareStatement(query);
        preparedSelect.setInt(1,studentID);
        ResultSet queryResult = preparedSelect.executeQuery();
        return queryResult.getInt("total_income");
    }
}
