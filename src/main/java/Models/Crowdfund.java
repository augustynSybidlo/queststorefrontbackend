package Models;

public class Crowdfund {
    private int id;
    private String name;
    private int totalPrice;
    private int account;
    private int founderID;
    private static ItemCollection<Crowdfund> crowdfundCollection = new ItemCollection<>("Crowdfunds");

    public Crowdfund(int id, String name, int totalPrice, int account, int founderID) {
        this.id = id;
        this.name = name;
        this.totalPrice = totalPrice;
        this.account = account;
        this.founderID = founderID;
    }

    public Crowdfund(String name, int totalPrice, int account, int founderID) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.account = account;
        this.founderID = founderID;
    }

    public int getCrowdfundId(){
        return this.id;
    }

    public String getCrowdfundName(){
        return this.name;
    }

    public int getCrowdfundTotalPrice(){
        return this.totalPrice;
    }

    public int getCrowdfundAccount(){
        return this.account;
    }

    public int getCrowdfundFounderID(){
        return this.founderID;
    }

    @Override
    public String toString() {
        return  String.format("ID: %d, NAME: %s, PRICE: %d, CURRENT ACCOUNT: %d, CONTRIBUTOR: %s",
                              this.id,
                              this.name,
                              this.totalPrice,
                              this.account,
                              this.founderID);
    }

    public void reduceCurrentPrice(int amount){
        this.account = this.account + amount;
    }
}
