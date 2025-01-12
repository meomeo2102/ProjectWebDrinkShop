package models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dao.ProductDAO;

public class Cart {
    private int cartId;
    private int userId;
    
    private Map<Integer, CartItem> items; // Product ID -> CartItem map

    // Constructor
    public Cart(int cartId, int userId) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = new ConcurrentHashMap<>();
    }

    
    // Add an item to the cart
    public synchronized void addItem(int productId, int quantity) {
    	ProductDAO dao = new ProductDAO();
    	Product product = dao.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        
        items.put(productId, new CartItem(product, quantity));

        
    }

    // Update the quantity of an item in the cart
    public synchronized void updateQuantity(int productId, int quantity) {
    	
        CartItem item = items.get(productId);
        if (item == null) {
            throw new IllegalArgumentException("Product not found in the cart.");
        }
        if (quantity <= 0 || quantity > item.getProduct().getStock()) {
            throw new IllegalArgumentException("Invalid quantity.");
        }
        item.setQuantity(quantity);
    }

    // Remove an item from the cart
    public synchronized void removeItem(int productId) {
        items.remove(productId);
    }

    // Calculate the total price for the cart
    public double calculateTotal() {
    	double result = 0;
    	for(Map.Entry<Integer, CartItem> entry : items.entrySet()) {
		   result += entry.getValue().getTotalPrice();
		}
        return result;
    }

    // Get the total price of a specific product
    public double getItemTotalPrice(int productId) {
        CartItem item = items.get(productId);
        return (item != null) ? item.getTotalPrice() : 0.0;
    }

    // Getters and setters for cart-specific data
    public int getCartId() {
        return cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map<Integer, CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return calculateTotal();
    }

    // Clear all items from the cart
    public void clearCart() {
        items.clear();
    }

    // Check if a product exists in the cart
    public boolean containsProduct(int productId) {
        return items.containsKey(productId);
    }

    public int getId() {
        return this.cartId;
    }


	@Override
	public String toString() {
		return "Cart [cartId=" + cartId + ", userId=" + userId + ", items=" + items + "]";
	}


	public Product lookup(int product_id) {
		for (Map.Entry<Integer, CartItem> entry : items.entrySet()) {
		    if(entry.getKey().equals(product_id)) return entry.getValue().getProduct();
		}
		return null;
	}
	
	public int getQuantity(int product_id) {
		return items.get(product_id).getQuantity();
	}
    
    
}