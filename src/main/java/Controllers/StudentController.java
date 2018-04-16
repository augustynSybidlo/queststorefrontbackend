package Controllers;

import DAOs.*;
import Iterator.CollectionIterator;
import Models.*;
import Views.UserView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentController{

    private UserView view = new UserView();
    private UsersDao userDao = new UsersDaoImpl();
    private ArtifactsDao artifactsDao = new ArtifactsDaoImpl();
    private CrowdfundDao crowdfundsDao = new CrowdfundDaoImpl();
    private QuestDao questDao = new QuestDaoImpl();
    private ItemCollection<Artifact> artifactsCollection = artifactsDao.getArtifacts();
    private ItemCollection<Crowdfund> crowdfundsCollection = crowdfundsDao.getCrowdfunds();
    private ItemCollection<Quest> questsCollection = questDao.getQuests();

    private CollectionIterator<Artifact> artifactIterator = artifactsCollection.getIterator();
    private CollectionIterator<Crowdfund> crowdfundIterator = crowdfundsCollection.getIterator();
    private CollectionIterator<Quest> questIterator = questsCollection.getIterator();

    private Student student;
    private boolean isRuntime = true;

    public void startStudentPanel(Student student){
        this.student = student;
        artifactsDao.importArtifacts();
        crowdfundsDao.importCrowdfunds();

        while(isRuntime){
            view.displayUserMenu("txt/studentMenu.txt");
            handleStudentPanelOptions();
            String choice = view.getInput("Press anything to continue");
        }
    }

    private void handleStudentPanelOptions(){

        try {
            refreshDB();
            String choice = view.getInput("Choose your option: ");
            switch(choice) {
                case "0":   userDao.disconnectDatabase();
                    isRuntime = false;
                    break;
                case "1":   view.clearScreen();
                    view.displayText("\n\nWallet is:");
                    view.displayText(this.student.getStudentWallet() + "\n\nVery nice!\n\n");
                    break;
                case "2":   buyArtifact();
                    break;
                case "3":   createCrowdfund();
                    break;
                case "4":   returnAllCrowdfunds();
                    break;
                case "5":   joinCrowdfund();
                    break;
                case "6":   showStudentArtifacts();
                    break;
                case "7":   view.clearScreen();
                    view.displayText("\n\nExperience status is:");
                    view.displayText(this.student.getStudentExperienceLevel() +
                            "\n\nVery nice!\n\n");
                    break;
                case "8":   view.clearScreen();
                    enrollOnQuest();
                    break;
                case "9":   view.clearScreen();
                    ResultSet result = questDao.displayStudentQuest(this.student.getId());
                    view.printQueryResults(result);
                    break;
                default:    view.displayText("No such choice");
                    break;
            }
        } catch (SQLException e) {
            e.getErrorCode();
        }
    }

    private void enrollOnQuest() throws SQLException {
        returnAllQuests();
        boolean isQuest = false;

        try{
            int choice = Integer.parseInt(view.getInput("Choose your option: "));

            while(questIterator.hasNext()) {
                Quest nextQuest = questIterator.next();
                if(choice == nextQuest.getQuestId() && questDao.userDontHaveQuest(choice, this.student.getId())) {
                    isQuest = true;
                    questDao.addQuestToStudent(nextQuest.getQuestId() , this.student.getId());
                }
            }
            if(!isQuest) {
                view.displayText("\n\nNo such kłest or You already enrolled.¯\\_(ツ)_/¯ \n\n");
            } else {
                view.displayText("\n\nQuest added, my friend.\n\n");
            }
        } catch (NumberFormatException e) {
            view.displayText("Wrong input!");
        }

    }

    private void refreshDB() {
        artifactsDao = new ArtifactsDaoImpl();
        artifactsDao.importArtifacts();
        artifactsCollection = artifactsDao.getArtifacts();
        crowdfundsDao = new CrowdfundDaoImpl();
        crowdfundsDao.importCrowdfunds();
        crowdfundsCollection = crowdfundsDao.getCrowdfunds();
        questDao = new QuestDaoImpl();
        questDao.importQuests();
        questsCollection = questDao.getQuests();
    }

    private void showStudentArtifacts() throws SQLException {
        ResultSet result = artifactsDao.returnSpecifiedStudentArtifacts(this.student.getId());
        view.printQueryResults(result);
    }

    private void returnAllCrowdfunds() {
        this.crowdfundIterator = crowdfundsCollection.getIterator();
        view.displayText("Crowdfunds:");
        while(crowdfundIterator.hasNext()) {
            view.displayText(crowdfundIterator.next().toString());
        }
        this.crowdfundIterator = crowdfundsCollection.getIterator();
    }

    private void returnAllQuests() {
        this.questIterator = questsCollection.getIterator();
        view.displayText("Quests:");
        while(questIterator.hasNext()) {
            view.displayText(questIterator.next().toString());
        }
        this.questIterator = questsCollection.getIterator();
    }

    private void returnAllArtifacts() {
        this.artifactIterator = artifactsCollection.getIterator();
        view.displayText("Artifacts:");
        while(artifactIterator.hasNext()) {
            view.displayText(artifactIterator.next().toString());
        }
        this.artifactIterator = artifactsCollection.getIterator();
    }

    private void createCrowdfund() throws SQLException {
        returnAllArtifacts();

        int artifactID;
        boolean ifExists = false;
        boolean noError = false;

        while (!noError) {
            try {
                artifactID = Integer.parseInt(view.getInput("Enter artifact ID: "));
                noError = true;
                while (artifactIterator.hasNext()) {
                    Artifact nextArtifact = artifactIterator.next();
                    if (nextArtifact.getArtifactId() == artifactID) {
                        ifExists = true;
                        int founderID = student.getId();
                        Crowdfund crowdfund = new Crowdfund(nextArtifact.getArtifactName(),
                                nextArtifact.getArtifactPrice(),
                                0,
                                founderID);


                        crowdfundsDao.addCrowdfundToDatabase(crowdfund);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                view.clearScreen();
                view.displayText("Wrong format.\n\n");
                returnAllArtifacts();
            }
            if (!ifExists) {
                view.clearScreen();
                view.displayText("No such ID\n\n");
                createCrowdfund();
            }
        }
    }

    private void buyArtifact() throws SQLException {
        int walletBalance = this.student.getStudentWallet();
        returnAllArtifacts();
        boolean doesArtifactExist = false;
        boolean isRunning = true;

        while(isRunning) {
            String choice = view.getInput("Choose your option: ");

            while(artifactIterator.hasNext()){
                Artifact nextArtifact = artifactIterator.next();
                int artifactPrice = nextArtifact.getArtifactPrice();

                if(choice.equals(String.valueOf(nextArtifact.getArtifactId()))) {
                    doesArtifactExist = true;
                    isRunning = false;
                    view.displayText("\n\nThis artifact bought! Good Job!\n\n");
                    if(walletBalance >= artifactPrice) {
                        artifactsDao.addArtifactToStudent(nextArtifact, this.student.getId());
                        this.student.reduceWallet(nextArtifact.getArtifactPrice());
                        userDao.updateStudentWalletInDatabase(this.student);
                    } else {
                        view.displayText("Not enough funds! Not very nice...");
                    }
                }
            } break;
        }if(!doesArtifactExist){
            view.displayText("\n\nNo such artifact ¯\\_(ツ)_/¯ \n\n");
        }
    }

    private void joinCrowdfund() throws SQLException {
        returnAllCrowdfunds();

        int crowdfundID;
        boolean ifExists = false;
        boolean noError = false;

        while(!noError){
            try{
            crowdfundID = Integer.parseInt(view.getInput("Enter crowdfund ID: "));
            noError = true;
                while(crowdfundIterator.hasNext()) {
                    Crowdfund nextCrowdfund = crowdfundIterator.next();
                    if (nextCrowdfund.getCrowdfundId() == crowdfundID) {
                        ifExists = true;
                        processJoiningCrowdfund(nextCrowdfund);
                    }
                }

        }catch(NumberFormatException e){
            view.clearScreen();
            view.displayText("Wrong format.\n\n");
            returnAllCrowdfunds();
            }
        }

        if(!ifExists){
            view.clearScreen();
            view.displayText("No such ID\n\n");
            joinCrowdfund();
        }
    }

    private void processJoiningCrowdfund(Crowdfund crowdfundToContribute) throws SQLException {
        int contribution;
        boolean isRunning = true;

        while(isRunning){
            try{
                contribution = Integer.parseInt(view.getInput("How much you want to contribute? "));
                if(contribution > student.getStudentWallet()){
                    view.displayText("You are to poor to contribute that much, amigo \n\n\n");
                    continue;
                }
                isRunning = false;
                student.reduceWallet(contribution);
                userDao.updateStudentWalletInDatabase(this.student);
                crowdfundToContribute.reduceCurrentPrice(contribution);
                crowdfundsDao.updateCrowdfundAccount(crowdfundToContribute.getCrowdfundId(), contribution);
                int totalPrice = crowdfundToContribute.getCrowdfundTotalPrice();
                int account = crowdfundToContribute.getCrowdfundAccount();
                if (totalPrice <= account){
                    int id = crowdfundToContribute.getCrowdfundId();
                    String name = crowdfundToContribute.getCrowdfundName();
                    int founder = crowdfundToContribute.getCrowdfundFounderID();
                    crowdfundsDao.deleteCrowdfund(id);
                    view.displayText("\n\nCrowdfund for: " +
                            name +
                            " has been successfully finished! Very nice !!\n\n\n");
                    artifactsDao.addArtifactToStudent(name, founder);
                    view.displayText("\nArtifact has been added to founders stash! Very good !!\n");
                }
            }catch(NumberFormatException e){
                view.clearScreen();
                view.displayText("Wrong format.\n\n");
                returnAllCrowdfunds();
            }
        }
    }
}