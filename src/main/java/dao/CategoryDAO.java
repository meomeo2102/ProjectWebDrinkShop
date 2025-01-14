package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Category;

public class CategoryDAO {
    	private Connection connection;

	public CategoryDAO(Connection connection) {
		this.connection = connection;
	}
    publi
    public CategoryDAO() {

    }
    
    // danh sach cac danh muc
    public List<Category> getAllCategories() {
        List<Category> categoriesList = new ArrayList<>();
        String query = "SELECT * FROM product_categories";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setTitle(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                categoriesList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoriesList;
    }

    // Thêm các phương thức khác như addCategory(), updateCategory(), deleteCategory() nếu cần



}
