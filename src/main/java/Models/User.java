package Models;

public abstract class User{
    protected String name;
    String surname;
    protected int id;
    String login;
    String password;
    private String status;
    protected Group group;
    private String experienceLevel;

    public User(String name, String surname, String password, String status){
        this.name = name;
        this.surname = surname;
        this.login = name.toLowerCase() + surname.toLowerCase() + "@cc.com";
        this.password = password;
        this.status = status;
        this.group = null;
        this.experienceLevel = " ";
    }

     public User(int id, String name, String surname, String password, String status, Group group){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = name.toLowerCase() + surname.toLowerCase() + "@cc.com";
        this.password = password;
        this.status = status;
        this.group = group;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public int getId(){
        return this.id;
    }

    public String getLogin(){
        return this.login;
    }

    public String getPassword(){
        return this.password;
    }

    public String getStatus(){
        return this.status;
    }

    public String getUserExperienceLevel(){
        return this.experienceLevel;
    }

    public String getUserGroupName(){
        return this.group.getGroupName();
    }

    public int getUserGroupId() {
        return (this.group != null) ? this.group.getGroupId() : 0;
    }
}
