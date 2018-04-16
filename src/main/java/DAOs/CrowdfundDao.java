package DAOs;

import Models.Crowdfund;
import Models.ItemCollection;

import java.sql.SQLException;

public interface CrowdfundDao {
    void importCrowdfunds();
    void deleteCrowdfund(int id) throws SQLException;
    ItemCollection<Crowdfund> getCrowdfunds();
    void addCrowdfundToDatabase(Crowdfund crowdfund) throws SQLException;
    void updateCrowdfundAccount(int crowdfundID, int amountToAdd) throws SQLException;
}
