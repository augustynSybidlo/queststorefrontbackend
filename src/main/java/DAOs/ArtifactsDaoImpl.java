package DAOs;

import Models.Artifact;
import Models.ItemCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ArtifactsDaoImpl extends ObjectsDaoImpl implements ArtifactsDao  {

    private static ItemCollection<Artifact> artifactsCollection = new ItemCollection<Artifact>("Artifacts");
    private DatabaseConnection database = DatabaseConnection.getInstance();
    private Connection connection;
    private static final int artifactIdIndex = 0;
    private static final int artifactNameIndex = 1;
    private static final int artifactPriceIndex = 2;
    private static final int artifactCategoryIndex = 3;

    public ArtifactsDaoImpl() {
        connection = database.getConnection();
    }

    public void importArtifacts() {

        artifactsCollection.clear();

        try {
            String query = "SELECT * FROM artifacts";
            ArrayList<ArrayList<String>> artifacts = getAllObjectsFromDatabase(connection, query);

            for(int i=0; i < artifacts.size(); i++) {
                int id = Integer.parseInt(artifacts.get(i).get(artifactIdIndex));
                String name = artifacts.get(i).get(artifactNameIndex);
                int price = Integer.parseInt(artifacts.get(i).get(artifactPriceIndex));
                String category = artifacts.get(i).get(artifactCategoryIndex);

                Artifact artifact = new Artifact(id, name, price, category);
                addArtifactToArtifactsCollection(artifact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ItemCollection<Artifact> getArtifacts(){
        return artifactsCollection;
    }

    private void addArtifactToArtifactsCollection(Artifact artifact) {
        artifactsCollection.add(artifact);
    }

    public void addArtifactToDatabase(Artifact artifact) throws SQLException {
        String query = "INSERT INTO artifacts (name, price, category) " +
                        "VALUES (?,?,?);";
        PreparedStatement preparedInsert = connection.prepareStatement(query);
        preparedInsert.setString(1, artifact.getArtifactName());
        preparedInsert.setString(2, String.valueOf(artifact.getArtifactPrice()));
        preparedInsert.setString(3, artifact.getArtifactCategory());
        preparedInsert.executeUpdate();
        importArtifacts();
    }

    public void addArtifactToStudent(Artifact artifact, int StudentID) throws SQLException {
        String query = "INSERT INTO student_artifacts (artifact_id, student_id, status) " +
                        "VALUES (?,?, 'not used');";
        PreparedStatement preparedInsert = connection.prepareStatement(query);
        preparedInsert.setString(1, String.valueOf(artifact.getArtifactId()));
        preparedInsert.setString(2, String.valueOf(StudentID));
        preparedInsert.executeUpdate();
    }

    public void addArtifactToStudent(String crowdfundName, int founderID) throws SQLException {
        String idQery = "SELECT id FROM artifacts WHERE name = ?";
        PreparedStatement prepareSelect = connection.prepareStatement(idQery);
        prepareSelect.setString(1, crowdfundName);
        ResultSet queryResult = prepareSelect.executeQuery();
        String id = String.valueOf(queryResult);
        String query = "INSERT INTO student_artifacts (artifact_id, student_id, status) " +
                        "VALUES (?, ?, 'not used');";
        PreparedStatement preparedInsert = connection.prepareStatement(query);
        preparedInsert.setString(1, id);
        preparedInsert.setInt(2, founderID);
        preparedInsert.executeUpdate();
    }

    public ResultSet returnSpecifiedStudentArtifacts(int studentID) throws SQLException {
        String query = "SELECT student_artifacts.artifact_id, artifacts.name, student_artifacts.status " +
                "FROM student_artifacts " +
                "INNER JOIN artifacts ON student_artifacts.artifact_id = artifacts.id " +
                "WHERE student_artifacts.student_id= ?;";
        PreparedStatement prepareSelect = connection.prepareStatement(query);
        prepareSelect.setInt(1, studentID);
        ResultSet queryResult = prepareSelect.executeQuery();
        return queryResult;
    }

    public ResultSet returnSpecifiedStudentUnusedArtifacts (int studentID) throws SQLException {
        String query = "SELECT student_artifacts.artifact_id, artifacts.name, student_artifacts.status " +
                "FROM student_artifacts " +
                "INNER JOIN artifacts ON student_artifacts.artifact_id = artifacts.id " +
                "WHERE student_artifacts.student_id= ? AND student_artifacts.status = 'not used' ;";
        PreparedStatement prepareSelect = connection.prepareStatement(query);
        prepareSelect.setInt(1, studentID);
        ResultSet queryResult = prepareSelect.executeQuery();
        return queryResult;
    }

    public void markGivenArtifact(int artifactID) throws SQLException {
        String query = "UPDATE student_artifacts " +
                "SET status = 'used' " +
                "WHERE artifact_id= ?;";
        PreparedStatement prepareUpdate = connection.prepareStatement(query);
        prepareUpdate.setInt(1, artifactID);
        prepareUpdate.executeUpdate();
    }

    public void updateArtifactDataInDatabase(Artifact artifact) throws SQLException {
        String name = artifact.getArtifactName();
        int price = artifact.getArtifactPrice();
        String category = artifact.getArtifactCategory();

        String query = "UPDATE artifacts SET name = ? ," +
                "price = ? ," +
                "category = ? " +
                "WHERE id = ?;";
        PreparedStatement prepareUpdate = connection.prepareStatement(query);
        prepareUpdate.setString(1, name);
        prepareUpdate.setInt(2, price);
        prepareUpdate.setString(3, category);
        prepareUpdate.setInt(4, artifact.getArtifactId());
        prepareUpdate.executeUpdate();
    }
}