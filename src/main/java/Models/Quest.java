package Models;

public class Quest {
    private String name;
    private int reward;
    private int id;
    private String categoryName;

    public Quest(int id, String name, int reward, String categoryName) {
        this.name = name;
        this.id = id;
        this.reward = reward;
        this.categoryName = categoryName;
    }

    public Quest(String name, int reward, String categoryName) {
        this.name = name;
        this.reward = reward;
        this.categoryName = categoryName;
    }

    public String getQuestCategoryName(){
        return this.categoryName;
    }

    public void setQuestName(String name){
        this.name = name;
    }

    public String getQuestName(){
        return this.name;
    }

    public int getQuestId(){
        return this.id;
    }

    public int getQuestReward(){
        return this.reward;
    }

    public void setQuestReward(int reward){
        this.reward = reward;
    }

    public void setQuestCategory(String category){
      this.categoryName = category;
    }

    @Override
    public String toString() {
        return  String.format("ID: %d, NAME: %s, REWARD: %d, CATEGORY NAME: %s",
                this.id,
                this.name,
                this.reward,
                this.categoryName);
    }
}
