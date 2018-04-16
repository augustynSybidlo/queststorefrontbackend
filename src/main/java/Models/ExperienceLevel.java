package Models;

public class ExperienceLevel {
    private int money_required;
    private String levelName;
    private static ItemCollection<ExperienceLevel> experienceLevelCollection = new ItemCollection<>("nazwa_grupy.java.Models.ExperienceLevel");

    public ExperienceLevel(int money_required, String levelName) {
        this.money_required = money_required;
        this.levelName = levelName;
    }

    public int getLevelMoneyRequired(){
        return this.money_required;
    }

    public String getLevelName(){
        return this.levelName;
    }

    public void addExperienceLevel(ExperienceLevel experienceLevel){
        experienceLevelCollection.add(experienceLevel);
    }
}
