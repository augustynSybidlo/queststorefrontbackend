package Controllers;

import DAOs.UsersDao;
import DAOs.UsersDaoImpl;
import Models.Admin;
import Models.Mentor;
import Models.Student;
import Models.User;
import Views.LoginView;

import java.util.ArrayList;

public class LoginController {
    private LoginView view = new LoginView();
    private UsersDao usersDao = new UsersDaoImpl();
    private ArrayList<User> usersCollection = usersDao.getUsersCollection();

    public void login() {
        boolean isLoginSession = true;

        while (isLoginSession) {
            view.clearScreen();
            String userLogin = view.getLogin();
            String userPassword = view.getPassword();
            String userStatus = getUserStatus(userLogin, userPassword);
            if (checkIfUserExists(userLogin) && checkUserPassword(userLogin, userPassword)) {
                runProperUserPanel(userLogin, userPassword, userStatus);
                isLoginSession = false;
            }
        }
    }

    private boolean checkIfUserExists(String login) {
        for (User user : usersCollection) {
            if (login.equals(user.getLogin())) {
                return true;
            }
        }
        view.displayText("Given user does not exists!");
        sleepThreadForOneSec();
        return false;
    }

    private boolean checkUserPassword(String login, String password) {
        for (User user : usersCollection) {
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        view.displayText("Wrong password!");
        sleepThreadForOneSec();
        return false;
    }

    private void sleepThreadForOneSec() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private String getUserStatus(String login, String password) {
        for (User user : usersCollection){
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())){
                return user.getStatus();
            }
        }
        return null;
    }

    private User getUserAccount(String userLogin, String userPassword) {
        for (User user : usersCollection) {
            if(userLogin.equals(user.getLogin()) && userPassword.equals(user.getPassword())){
                return user;
            }
        }
        return null;
    }

    private void runProperUserPanel(String userLogin, String userPassword, String userStatus) {
        if(userStatus.equals("admin")) {
            User user = new Admin(userLogin, userPassword, userStatus);
            AdminController controller = new AdminController();
            controller.startAdminPanel();
        }
        else if(userStatus.equals("mentor")) {
            User user = new Mentor(userLogin, userPassword, userStatus);
            MentorController controller = new MentorController();
            controller.startMentorPanel();
        }
        else if(userStatus.equals("student")) {
            Student user = (Student)getUserAccount(userLogin, userPassword);
            StudentController controller = new StudentController();
            controller.startStudentPanel(user);
        }
    }
}
