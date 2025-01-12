package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Cart;
import models.CartItem;
import models.Product;

public class CartItemDAO {

    public CartItemDAO() {
    	
    }

    public boolean addCartItem(int cartId, int productId, int quantity) {
        String query = "INSERT INTO CartItem (CartId, ProductId, Quantity) VALUES (?, ?, ?)";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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

            // Thực thi câu lệnh và kiểm tra số dòng bị ảnh hưởng
            int rowsInserted = stmt.executeUpdate();
            if(rowsInserted > 0)
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean addCartItem(Cart cart, Product product, int quantity) {
    	
        String query = "INSERT INTO CartItem (CartId, ProductId, Quantity) VALUES (?, ?, ?)";
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
        	
            // Gán các tham số vào câu lệnh SQL
            stmt.setInt(1, cart.getCartId());
            stmt.setInt(2, product.getId());
            stmt.setInt(3, quantity);
            CartItem ci = new CartItem(product,quantity);

            // Thực thi câu lệnh và kiểm tra số dòng bị ảnh hưởng
            int rowsInserted = stmt.executeUpdate();
            if(rowsInserted > 0) {
            	cart.getItems().put(product.getId(), ci);
            	return true;
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

	public void setQuantity(Cart cart, Product product, int quantity) {
		String query2 = "SELECT Quantity FROM CartItem WHERE CartId = ? AND ProductId = ?";
		String query = "UPDATE CartItem SET Quantity = ? WHERE CartId = ? AND ProductId = ?";
	    try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
	    	
	    	// lấy quantity ban đầu 
	    	PreparedStatement stmt = connection.prepareStatement(query2);
	    	stmt.setInt(1, cart.getId());
	    	stmt.setInt(2, product.getId());
	    	
	    	ResultSet resultSet = stmt.executeQuery();
	    	int quantityDb = resultSet.getInt("Quantity");
	    	quantity += quantityDb;
	    	
	    	
	    	
	    	// set quantity sau khi cộng thêm
	        stmt = connection.prepareStatement(query);
	        stmt.setInt(1, quantity);
	        stmt.setInt(2, cart.getCartId());
	        stmt.setInt(3, product.getId());

	        // Execute the update and check if any rows were affected
	        int rowsUpdated = stmt.executeUpdate();
	        if (rowsUpdated > 0) {
	            // Update the quantity in the cart's items map
	            CartItem ci = cart.getItems().get(product.getId());
	            if (ci != null) {
	                ci.setQuantity(quantity + quantityDb); // cộng thêm vào list
	            }
	        } 
	        
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
