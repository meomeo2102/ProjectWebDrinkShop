package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Category;
import models.Product;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }


    // Constructor chứa tham số Connection
    public ProductDAO() {

    }

    // Thêm sản phẩm
    public void addProduct(Product product) {
        String sql = "INSERT INTO Product (name, description, photo, price, discount, category_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setString(3, product.getPhoto());
            statement.setDouble(4, product.getPrice());
            statement.setInt(6, product.getCategory().getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
        try {
            List<Product> products = dao.getAllProducts();
            if (products.isEmpty()) {
                System.out.println("Không có sản phẩm nào trong cơ sở dữ liệu.");
            } else {
                for (Product product : products) {
                    System.out.println("ID: " + product.getId());
                    System.out.println("Tên: " + product.getName());
                    System.out.println("Mô tả: " + product.getDescription());
                    System.out.println("Ảnh: " + product.getPhoto());
                    System.out.println("Giá: " + product.getPrice());
                    System.out.println("Danh mục: " + (product.getCategory() != null ? product.getCategory().getTitle() : "Không rõ"));
                    System.out.println("-----------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int id) {
        Product product = null;
        String sql = "SELECT * FROM Product WHERE id = ?";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("product_name"));
                product.setDescription(resultSet.getString("description"));
                product.setPhoto(resultSet.getString("images"));
                product.setPrice(resultSet.getDouble("price"));

                int categoryId = resultSet.getInt("category_id");
                Category category = getCategoryById(categoryId); 
                product.setCategory(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()){

                PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery() ;

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("product_name"));
                product.setDescription(resultSet.getString("description"));
                product.setPhoto(resultSet.getString("images"));
                product.setPrice(resultSet.getDouble("price"));

                int categoryId = resultSet.getInt("category_id");
                Category category = getCategoryById(categoryId);
                product.setCategory(category);

                products.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;}

        // Cập nhật thông tin sản phẩm
    public void updateProduct(Product product) {
        String sql = "UPDATE Product SET name = ?, description = ?, photo = ?, price = ?, discount = ?, category_id = ? WHERE id = ?";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setString(3, product.getPhoto());
            statement.setDouble(4, product.getPrice());
            statement.setInt(6, product.getCategory().getId());
            statement.setInt(7, product.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa sản phẩm
    public void deleteProduct(int id) {
        String sql = "DELETE FROM Product WHERE id = ?";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method để lấy thông tin Category theo ID
    private Category getCategoryById(int categoryId) {
        Category category = null;
        String sql = "SELECT * FROM product_categories WHERE id = ?";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setTitle(resultSet.getString("category_name"));
                category.setDescription(resultSet.getString("description"));
                // Set other Category fields if necessary
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    // Lấy sản phẩm theo Category ID
    public List<Product> getProductsByCategoryId(int categoryId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE category_id = ?";

        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setPhoto(rs.getString("photo"));
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }

    // Tìm kiếm sản phẩm theo tên
    public List<Product> searchProductByName(String name) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE name like ?";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPhoto(resultSet.getString("photo"));
                product.setPrice(resultSet.getDouble("price"));

                int categoryId = resultSet.getInt("category_id");
                Category category = getCategoryById(categoryId);
                product.setCategory(category);

                list.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lọc sản phẩm theo giá
    public List<Product> filteringProductByPrice(double minPrice, double maxPrice) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE price BETWEEN ? AND ?";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPhoto(resultSet.getString("photo"));
                product.setPrice(resultSet.getDouble("price"));

                int categoryId = resultSet.getInt("category_id");
                Category category = getCategoryById(categoryId);
                product.setCategory(category);

                list.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Product> getTop4() {
    	 List<Product> list = new ArrayList<>();
         String sql = "SELECT TOP 4 * FROM Product";
         try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery();
             while (resultSet.next()) {
                 Product product = new Product();
                 product.setId(resultSet.getInt("id"));
                 product.setName(resultSet.getString("product_name"));
                 product.setDescription(resultSet.getString("description"));
                 product.setPhoto(resultSet.getString("images"));
                 product.setStock(resultSet.getInt("stock"));
                 product.setPrice(resultSet.getDouble("price"));
                 int categoryId = resultSet.getInt("category_id");
                 Category category = getCategoryById(categoryId);
                 product.setCategory(category);

                 list.add(product);
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
         return list;
    }
    
    public List<Product> getNext4(int amount) {
   	 List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product ORDER BY id OFFSET ? ROWS FETCH NEXT 3 ROWS ONLY";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, amount);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("product_name"));
                product.setDescription(resultSet.getString("description"));
                product.setPhoto(resultSet.getString("images"));
                product.setStock(resultSet.getInt("stock"));
                product.setPrice(resultSet.getDouble("price"));
                int categoryId = resultSet.getInt("category_id");
                Category category = getCategoryById(categoryId);
                product.setCategory(category);

                list.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
   }
    	public double getTotalRevenue() {
		double totalRevenue = 0;
		String query = "SELECT SUM(price) AS totalRevenue FROM products"; // Hoặc có thể tính doanh thu từ đơn hàng
		try (Connection connection = DBConnectionPool.getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			if (rs.next()) {
				totalRevenue = rs.getDouble("totalRevenue");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalRevenue;
	}

}
}
