package DAOs;

import Models.Mentor;
import Models.Student;
import Models.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UsersDao {
    ArrayList<User> getUsersCollection();
    void addUserToDatabase(User user) throws SQLException;
    void addStudentWalletToDatabase(Student student) throws SQLException;
    void updateUserGroupInDatabase(User user) throws SQLException;
    void updateStudentWalletInDatabase(Student student) throws SQLException;
    void addCurrencyToWallet(int questReward, int studentID) throws SQLException;
    void updateUserDataInDatabase(User user) throws SQLException;
    ArrayList<User> getAllUsersByStatus(String userStatus);
    Mentor getMentorById(int id);
    Student getStudentById(int id);
    void disconnectDatabase();
    void importUsersData() throws SQLException;

}
