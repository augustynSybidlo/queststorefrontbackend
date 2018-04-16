package DAOs;

import Models.Crowdfund;
import Models.ItemCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


public class CrowdfundDaoImpl extends ObjectsDaoImpl implements CrowdfundDao {

    private static ItemCollection<Crowdfund> crowdfundCollection = new ItemCollection<>("Crowdfunds");
    private DatabaseConnection database = DatabaseConnection.getInstance();
    private Connection connection;

    public CrowdfundDaoImpl() {
        connection = database.getConnection();
    }

    public void importCrowdfunds() {
        crowdfundCollection.clear();
        String query = "SELECT * FROM crowdfunds";
        try {
            ArrayList<ArrayList<String>> crowdfunds = getAllObjectsFromDatabase(connection, query);

            for(int i =0; i < crowdfunds.size(); i++){
                int id = Integer.parseInt(crowdfunds.get(i).get(0));
                String name = crowdfunds.get(i).get(1);
                int totalPrice = Integer.parseInt(crowdfunds.get(i).get(2));
                int account = Integer.parseInt(crowdfunds.get(i).get(3));
                int founderID = Integer.parseInt(crowdfunds.get(i).get(4));
                Crowdfund crowdfund = new Crowdfund(id, name, totalPrice, account, founderID);
                addCrowdfund(crowdfund);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCrowdfund(int id) throws SQLException {
        String query = "DELETE FROM crowdfunds WHERE id= ?;";
        PreparedStatement prepareDelete = connection.prepareStatement(query);
        prepareDelete.setInt(1, id);
        prepareDelete.executeUpdate();
    }

    public ItemCollection<Crowdfund> getCrowdfunds() {

        importCrowdfunds();
        return crowdfundCollection;
    }

    private void addCrowdfund(Crowdfund crowdfund){
        crowdfundCollection.add(crowdfund);
    }

    public void addCrowdfundToDatabase(Crowdfund crowdfund) throws SQLException {
        String query = "INSERT INTO crowdfunds (name, total_price, account, founder_id) VALUES (?, ?, ?, ?)";
        PreparedStatement prepareInsert = connection.prepareStatement(query);
        prepareInsert.setString(1, crowdfund.getCrowdfundName());
        prepareInsert.setInt(2, crowdfund.getCrowdfundTotalPrice());
        prepareInsert.setInt(3, crowdfund.getCrowdfundAccount());
        prepareInsert.setInt(4, crowdfund.getCrowdfundFounderID());
        prepareInsert.executeUpdate();
    }

    public void updateCrowdfundAccount(int crowdfundID, int amountToAdd) throws SQLException {
        String query = "UPDATE crowdfunds SET account = account + ?" +
                " WHERE id = ?;";
        PreparedStatement prepareUpdate = connection.prepareStatement(query);
        prepareUpdate.setInt(1, amountToAdd);
        prepareUpdate.setInt(2, crowdfundID);
        prepareUpdate.executeUpdate();
    }
}
