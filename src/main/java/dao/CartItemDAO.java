package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CartItemDAO {

    public CartItemDAO() {
    	
    }

    public boolean addCartItem(int cartId, int productId, int quantity, double price) {
        String query = "INSERT INTO Cart_items (cart_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query);
            // Kiểm tra các tham số đầu vào
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Connection is null or closed");
            }

            // Gán các tham số vào câu lệnh SQL
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price); // Truyền giá trị price vào câu lệnh

            // Thực thi câu lệnh và kiểm tra số dòng bị ảnh hưởng
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
