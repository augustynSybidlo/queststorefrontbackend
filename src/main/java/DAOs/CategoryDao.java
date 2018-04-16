package DAOs;

import Models.Category;
import Models.ItemCollection;

import java.sql.SQLException;

public interface CategoryDao {
    void importCategories();
    ItemCollection<Category> getCategories();
    void addCategory(Category category);
    Category getCategoryByName(String name);
    void addCategoryToDatabase(Category category) throws SQLException;
}
