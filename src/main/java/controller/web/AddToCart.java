package controller.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.CartDAO;
import dao.CartItemDAO;
import dao.DBConnectionPool;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.Product;
import models.User;

/**
 * Servlet implementation class AddToCart
 */
@WebServlet("/addToCart")
public class AddToCart extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		int quantity = 1;
		int id;
		HttpSession session = req.getSession();
		
		User user = (User) session.getAttribute("user");
		if(user == null) {
			res.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		ProductDAO productDAO = new ProductDAO();
		try {
			Connection connection = DBConnectionPool.getDataSource().getConnection();
			if(req.getParameter("productId") != null) {
				id = Integer.parseInt(req.getParameter("productId"));
				Product product = productDAO.getProductById(id);
				if(product != null) {
					
					if(req.getParameter("quantity") != null) {
						quantity = Integer.parseInt(req.getParameter("quantity"));
					}
					CartDAO cartDAO = new CartDAO(connection);
					Cart cart;
					if(session.getAttribute("cart") == null) {
						cart = new Cart(1, user.getId());
						cartDAO.createCart(cart);
						session.setAttribute("cart", cartDAO.getCartByUserId(user.getId()));
					}
						cart = (Cart) session.getAttribute("cart");
						CartItemDAO cartItemDAO = new CartItemDAO();
						
						// Kiểm tra các tham số đầu vào
				        if (quantity <= 0) {
				            throw new IllegalArgumentException("Quantity must be greater than 0");
				        }
				        
				        if(cart == null) {
				        	throw new NullPointerException("cart is Null");
				        }
				        
				        Product item = cart.lookup(product.getId());
				        if(item == null && quantity > 0) {
				        	cartItemDAO.addCartItem(cart, product, quantity);
				        }
				        
				        if(item != null && quantity > 0) {
				        	cartItemDAO.setQuantity(cart, product, quantity);
				        }

				}

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		res.sendRedirect(req.getContextPath() + "/secure/cart");
	}



}
