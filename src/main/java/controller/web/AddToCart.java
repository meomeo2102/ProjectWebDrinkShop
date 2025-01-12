package controller.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.CartDAO;
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


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		int quantity = 1;
		int id;
		System.out.println("11111 djt me m");
		ProductDAO productDAO = new ProductDAO();
		try {
			Connection connection = DBConnectionPool.getDataSource().getConnection();
			if(req.getParameter("productId") != null) {
				id = Integer.parseInt(req.getParameter("productId"));
				Product product = productDAO.getProductById(id);
				if(product != null) {
					HttpSession session = req.getSession();
					if(req.getParameter("quantity") != null) {
						quantity = Integer.parseInt(req.getParameter("quantity"));
					}
					User user = (User) session.getAttribute("user");
					
					if(session.getAttribute("cart") == null) {

						CartDAO cartDAO = new CartDAO(connection);
						cartDAO.createCart(new Cart(1, user.getId()));
						
					}
					Cart cart = (Cart) session.getAttribute("cart");
					cart.addItem(product, quantity);
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
