package Controllers;


import DAOs.*;
import Models.*;
import Views.UserView;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminController {

    private UsersDao dao = new UsersDaoImpl();
    private UserView view = new UserView();
    private GroupDao groupDao = new GroupDaoImpl();
    private ExperienceLevelsDao levelsDao = new ExperienceLevelsDaoImpl();

    public AdminController() {
        groupDao.importGroups();
        levelsDao.importExperienceLevels();
    }

    public void createNewMentor(String mentorName, String mentorSurname, String mentorPassword) throws SQLException {
        Mentor newMentor = new Mentor(mentorName, mentorSurname, mentorPassword);
        dao.addUserToDatabase(newMentor);
        dao.importUsersData();
    }

    public void createNewGroup(String groupName) throws SQLException {
        Group group = new Group(groupName);
        groupDao.addGroup(group);
        groupDao.addGroupToDatabase(group);
    }

    private void assignMentorToGroup(String id, String groupName) throws SQLException {
        try {
            int mentorId = Integer.parseInt(id);
            Mentor mentor = dao.getMentorById(mentorId);
            Group newGroup = groupDao.getGroupByName(groupName);
            mentor.setMentorGroup(newGroup);
            dao.updateUserGroupInDatabase(mentor);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void editMentorData(String id, String newName,
                                String newSurname, String newPassword) throws SQLException {
        try {
            int mentorId = Integer.parseInt(id);
            if(checkIfGivenIdMentorExists(mentorId)) {
                Mentor mentor = dao.getMentorById(mentorId);
                setExistingMentorNewData(mentor, newName,
                        newSurname, newPassword);
                dao.updateUserDataInDatabase(mentor);
            }
            else {
                view.displayText("No mentor with given ID exists!");
                Thread.sleep(1000);
            }
        } catch (NumberFormatException e){
            promptMessageAndStopThread("No mentor with given ID exists!");
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private void setExistingMentorNewData(Mentor mentor, String newName,
                                          String newSurname, String newPassword) {
        mentor.setMentorName(newName);
        mentor.setMentorSurname(newSurname);
        mentor.setMentorPassword(newPassword);
        mentor.setMentorLogin(newName, newSurname);
    }

    private Mentor getSpecificMentorData(String id) {
        Mentor mentor = null;
        try {
            int mentorId = Integer.parseInt(id);
            if (checkIfGivenIdMentorExists(mentorId)) {
                mentor = dao.getMentorById(mentorId);
            } else {
                view.displayText("No mentor with given ID exists!");
                Thread.sleep(1000);
            }
        } catch (NumberFormatException e) {
            promptMessageAndStopThread("No mentor with given ID exists!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return mentor;
    }

    private ArrayList<User> getAllMentors() {
        view.clearScreen();
        ArrayList<User> mentorsCollection = dao.getAllUsersByStatus("mentor");
        return mentorsCollection;
    }

    private ArrayList<Group> getAllGroups() {
        ItemCollection<Group> allGroups = groupDao.getGroups();
        return allGroups.getCollection();
    }

    private boolean checkIfGivenIdMentorExists(int id) {
        ArrayList<User> mentorsCollection = dao.getAllUsersByStatus("mentor");
        for(User mentor : mentorsCollection) {
            int mentorId = mentor.getId();
            if (mentorId == id) {
                return true;
            }
        }
        return false;
    }

    public void createNewLevelOfExperience(String levelName, String levelAsString) throws SQLException {
        try {
            int level = Integer.parseInt(levelAsString);
            ExperienceLevel newLevel = new ExperienceLevel(level, levelName);
            newLevel.addExperienceLevel(newLevel);
            levelsDao.addExperienceLevelToDatabase(newLevel);
        } catch (NumberFormatException e) {
            promptMessageAndStopThread("Experience needed should be a number!");
        }
    }

    private void promptMessageAndStopThread(String message) {
        try {
            view.displayText(message);
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
