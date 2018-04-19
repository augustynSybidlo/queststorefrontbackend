package Models;

public class Mentor extends User {

    public Mentor(String name, String surname, String password) {
        super(name, surname, password, "mentor");
    }

    public Mentor(int id, String name, String surname, String password, Group group) {
        super(id,name,surname,password,"mentor", group);
    }

    public void setMentorGroup(Group group) {
        this.group = group;
    }

    public void setMentorName(String name){
        this.name = name;
    }

    public void setMentorSurname(String surname){
        this.surname = surname;
    }

    public void setMentorPassword(String password){
        this.password = password;
    }

    public void setMentorLogin(String name, String surname) {
        this.login = name.toLowerCase() + surname.toLowerCase() + "@cc.com";
    }

    @Override
    public String toString() {
        return  String.format("ID: %d, NAME: %s, SURNAME: %s, GROUP: %s",
                              this.id,
                              this.name,
                              this.surname,
                              this.getUserGroupName());
    }
}
