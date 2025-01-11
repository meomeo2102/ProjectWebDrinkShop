package dao;

import models.Cart;
import models.CartItem;
import models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private Connection connection;

    public CartDAO(Connection connection) {
        this.connection = connection;
    }

    public Cart getCartByUserId(int userId) {
        String query = "SELECT * FROM Cart WHERE UserId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int cartId = rs.getInt("id");
                Cart cart = new Cart(cartId, userId);

                // Populate cart items
                String itemQuery = "SELECT * FROM CartItem WHERE CartId = ?";
                try (PreparedStatement itemStmt = connection.prepareStatement(itemQuery)) {
                    itemStmt.setInt(1, cartId);
                    ResultSet itemRs = itemStmt.executeQuery();
                    while (itemRs.next()) {
                        int productId = itemRs.getInt("ProductId");
                        int quantity = itemRs.getInt("Quantity");

                        ProductDAO productDAO = new ProductDAO();
                        Product product = productDAO.getProductById(productId);
                        if (product != null) {
                            cart.addItem(product, quantity);
                        } else {
                            System.err.println("Product not found: ID = " + productId);
                        }
                    }
                }

                return cart;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log SQL exceptions
        }
        return null;
    }

    public void createCart(Cart cart) {
        String query = "INSERT INTO Cart (UserId) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cart.getUserId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                cart.setCartId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCart(Cart cart) {
        String deleteItems = "DELETE FROM CartItem WHERE CartId = ?";
        String insertItem = "INSERT INTO CartItem (CartId, ProductId, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteItems)) {
            deleteStmt.setInt(1, cart.getCartId());
            deleteStmt.executeUpdate();

            try (PreparedStatement insertStmt = connection.prepareStatement(insertItem)) {
                for (CartItem item : cart.getItems().values()) {
                    insertStmt.setInt(1, cart.getCartId());
                    insertStmt.setInt(2, item.getProduct().getId());
                    insertStmt.setInt(3, item.getQuantity());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Phương thức close để đóng kết nối
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Log lỗi khi đóng kết nối thất bại
            }
        }
    }
    
    // Method to fetch all CartItems for a specific cartId
    public List<CartItem> getCartItems(int cartId) {
        String query = "SELECT * FROM CartItem WHERE CartId = ?";
        List<CartItem> items = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            ProductDAO productDAO = new ProductDAO();
            // Fetch product details for each cart item
            while (rs.next()) {
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");

                // Fetch the product
                Product product = productDAO.getProductById(productId);
                if (product != null) {
                    items.add(new CartItem(product, quantity));
                } else {
                    System.err.println("Product not found for ID: " + productId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    // Clear all items in the cart for a given cartId
    public void clearCart(int cartId) {
        String query = "DELETE FROM CartItem WHERE CartId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cartId); // Bind the cart ID
            stmt.executeUpdate();  // Execute the DELETE query
        } catch (SQLException e) {
            e.printStackTrace();  // Log the SQL exception
        }
    }
}
