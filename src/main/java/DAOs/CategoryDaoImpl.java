package DAOs;

import Iterator.CollectionIterator;
import Models.Category;
import Models.ItemCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDaoImpl extends ObjectsDaoImpl implements CategoryDao {

    private static ItemCollection<Category> allCategories = new ItemCollection<>("Category");
    private DatabaseConnection database = DatabaseConnection.getInstance();
    private Connection connection;

    public CategoryDaoImpl() {
        connection = database.getConnection();
    }

    public void importCategories() {
        String query = "SELECT * FROM categories";
        try {
            ArrayList<ArrayList<String>> category = getAllObjectsFromDatabase(connection, query);

            for (int i = 0; i < category.size(); i++) {

                String name = category.get(i).get(0);
                Category newCategory = new Category(name);
                addCategory(newCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ItemCollection<Category> getCategories() {
        return allCategories;
    }

    public void addCategory(Category category) {
        allCategories.add(category);
    }

    public Category getCategoryByName(String name) {
        CollectionIterator<Category> categoryIterator = allCategories.getIterator();
        while (categoryIterator.hasNext()) {
            Category category = categoryIterator.next();
            if (category.getCategoryName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public void addCategoryToDatabase(Category category) throws SQLException {
        String query = "INSERT INTO categories (name) VALUES (?)";
        PreparedStatement preparedInsert = connection.prepareStatement(query);
        preparedInsert.setString(1, category.getCategoryName());
        preparedInsert.executeUpdate();
    }
}
