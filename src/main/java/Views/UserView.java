package Views;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserView{
    private BufferedReader br = null;
    private Scanner reader = new Scanner(System.in);

    private String importUserMenu(String filename ) {
        String userMenu = "";
        try {
            br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
                userMenu = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
        return userMenu;
    }
    public void displayUserMenu(String filename){
        clearScreen();
        String userMenu = importUserMenu(filename);
            System.out.println(userMenu);
        }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public String getInput(String text){
        System.out.println(text);
        String input = reader.nextLine();
        return input;
    }

    public void displayText(String text){
        System.out.println(text);
    }

    public void displayObject(Object object) {
        System.out.println(object);
    }

    public void printQueryResults(ResultSet result) throws SQLException {

        // Group all column names from query result
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<String> columnNames = new ArrayList<String>();


        for (int row = 1; row <= columnCount; row++) {
            String columnName = metaData.getColumnName(row).toString();
            columnNames.add(columnName);
        }

        while (result.next()) {
            String row = "";

            for (int i = 0; i < columnNames.size(); i++) {
                row += columnNames.get(i) + ": " + result.getString(columnNames.get(i));
                row += "  ";
            }
            System.out.println(row);
        }

    }
}
