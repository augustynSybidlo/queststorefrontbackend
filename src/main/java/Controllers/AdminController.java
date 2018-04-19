package Controllers;


import DAOs.*;
import Iterator.CollectionIterator;
import Models.*;
import Views.UserView;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminController {

    private UsersDao dao = new UsersDaoImpl();
    private UserView view = new UserView();
    private GroupDao groupDao = new GroupDaoImpl();
    private ExperienceLevelsDao levelsDao = new ExperienceLevelsDaoImpl();
    private boolean isRuntime = true;

    public void startAdminPanel() {
        groupDao.importGroups();
        levelsDao.importExperienceLevels();

        while(isRuntime) {
            view.displayUserMenu("txt/adminMenu.txt");
            handleAdminPanelOptions();
        }
    }

    private void handleAdminPanelOptions() {
        try {
            String choice = view.getInput("Choose your option: ");
            switch (choice) {
                case "0":
                    dao.disconnectDatabase();
                    isRuntime = false;
                    break;
                case "1":
                    createNewMentor();
                    break;
                case "2":
                    createNewGroup();
                    break;
                case "3":
//                    assignMentorToGroup();
                    break;
                case "4":
                    editMentorData();
                    break;
                case "5":
                    getSpecificMentorData();
                    break;
                case "6":
                    createNewLevelOfExperience();
                    break;
                default:
                    view.displayText("No such option exists!");
                    Thread.sleep(1000);
                    break;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createNewMentor() throws SQLException {
        String mentorName = view.getInput("Enter mentor's name: ");
        String mentorSurname = view.getInput("Enter mentor's surname: ");
        String mentorPassword = view.getInput("Enter mentor's password: ");
        Mentor newMentor = new Mentor(mentorName, mentorSurname, mentorPassword);
        dao.addUserToDatabase(newMentor);
        dao.importUsersData();
    }

    private void createNewGroup() throws SQLException {
        String groupName = view.getInput("Enter new group name: ");
        Group group = new Group(groupName);
        groupDao.addGroup(group);
        groupDao.addGroupToDatabase(group);
    }

    private void assignMentorToGroup(int mentorId) throws SQLException {
        try {
            Mentor mentor = dao.getMentorById(mentorId);
            view.clearScreen();  // clear before displaying group names
            view.displayText("Choose group from those listed below:");
            getAllGroupsNames();
            String groupName = view.getInput("Choose group name:");
            Group newGroup = groupDao.getGroupByName(groupName);
            mentor.setMentorGroup(newGroup);
            dao.updateUserGroupInDatabase(mentor);
        } catch (NullPointerException e) {
            try {
                view.displayText("No such mentor or group exists!");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } catch (NumberFormatException e) {
            promptMessageAndStopThread("No such mentor or group exists!");
        }
    }

    private void editMentorData() throws SQLException {
        try {
            getAllMentors();
            int mentorId = Integer.parseInt(view.getInput("Choose mentor by ID"));
            if(checkIfGivenIdMentorExists(mentorId)) {
                Mentor mentor = dao.getMentorById(mentorId);
                setExistingMentorNewData(mentor);
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

    private void setExistingMentorNewData(Mentor mentor) {
        String newName = view.getInput("Enter mentor's new name: ");
        String newSurname = view.getInput("Enter mentor's new surname: ");
        String newPassword = view.getInput("Enter mentor's new password: ");
        mentor.setMentorName(newName);
        mentor.setMentorSurname(newSurname);
        mentor.setMentorPassword(newPassword);
        mentor.setMentorLogin(newName, newSurname);
    }

    private void getSpecificMentorData() {
        try {
            getAllMentors();
            int mentorId = Integer.parseInt(view.getInput("Choose mentor by ID"));
            if (checkIfGivenIdMentorExists(mentorId)) {
                Mentor mentor = dao.getMentorById(mentorId);
                view.displayText(mentor.toString());
                view.getInput("Press any key to continue");
            } else {
                view.displayText("No mentor with given ID exists!");
                Thread.sleep(1000);
            }
        } catch (NumberFormatException e) {
            promptMessageAndStopThread("No mentor with given ID exists!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void getAllMentors() {
        view.clearScreen();
        ArrayList<User> mentorsCollection = dao.getAllUsersByStatus("mentor");
        for(User mentor : mentorsCollection) {
            int mentorId = mentor.getId();
            String mentorName = mentor.getName();
            String mentorSurname = mentor.getSurname();
            view.displayText("ID: "+mentorId +" "+mentorName+" "+mentorSurname);
        }
    }

    private void getAllGroupsNames() {
        ItemCollection<Group> allGroups = groupDao.getGroups();
        CollectionIterator<Group> groupIterator = allGroups.getIterator();
        while(groupIterator.hasNext()) {
            Group group = groupIterator.next();
            view.displayText(group.getGroupName());
        }
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

    private void createNewLevelOfExperience() throws SQLException {
        try {
            view.clearScreen();
            String levelName = view.getInput("Set level name: ");
            int level = Integer.parseInt(view.getInput("Set xp needed to reach level: "));
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

    public UsersDao getDao() {
        return dao;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }
}
