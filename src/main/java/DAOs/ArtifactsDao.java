package DAOs;
import Models.Artifact;
import Models.ItemCollection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ArtifactsDao {
     void importArtifacts();
     ItemCollection<Artifact> getArtifacts();
     void addArtifactToDatabase(Artifact artifact) throws SQLException;
     void addArtifactToStudent(Artifact artifact, int StudentID) throws SQLException;
     void addArtifactToStudent(String crowdfundName, int founderID) throws SQLException;
     ResultSet returnSpecifiedStudentArtifacts(int studentID) throws SQLException;
     ResultSet returnSpecifiedStudentUnusedArtifacts (int studentID) throws SQLException;
     void markGivenArtifact(int artifactID) throws SQLException;
     void updateArtifactDataInDatabase(Artifact artifact) throws SQLException;

}
