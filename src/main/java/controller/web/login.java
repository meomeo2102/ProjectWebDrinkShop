package controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.CartDAO;
import dao.DBConnectionPool;
import dao.UserDAO;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	        
		 	// check if session is existed
	        HttpSession session = req.getSession();
	        User user = (User) session.getAttribute("user");
	        if (user != null) {
	            res.sendRedirect(getServletContext().getContextPath() + "/");
	        }
	        else {
	            req.getRequestDispatcher("/Login.jsp").forward(req, res);
	        }
	    }
	 @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
        String email = req.getParameter("email");
        String pass = req.getParameter("password");
        String message = "Sai thông tin tài khoản hoặc mật khẩu.";
        UserDAO udao = new UserDAO(connection);

        HttpSession session = req.getSession();
        try {
            User user = udao.getLogin(email, pass); // Lấy thông tin người dùng từ DB
            if (user == null) {
                req.setAttribute("message", message);
                req.getRequestDispatcher("/Login.jsp").forward(req, resp);
            } else {
                session.setAttribute("user", user);

                // Kiểm tra quyền admin
                if (user.isAdmin()) { 
                    resp.sendRedirect("admin/dashboard"); // Chuyển hướng admin
                } else {
                    resp.sendRedirect("Homepage"); // Chuyển hướng người dùng thường
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } catch (Exception e) {
        throw new ServletException("Error connecting to the database", e);
    }
}


	    
	/*@Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    	try (Connection connection = DBConnectionPool.getDataSource().getConnection()){
	            
	        	String email = req.getParameter("email");
	            String pass = req.getParameter("password");
	            String message = "Sai thông tin tài khoản mật khẩu ";
	            UserDAO udao = new UserDAO(connection);
	            String role = "";
	            
	            HttpSession session = req.getSession();
	            try {
	                User user = udao.getLogin(email, pass);
	                if (user == null) {
	                    req.setAttribute("message", message);
	                    req.getRequestDispatcher("/Login.jsp").forward(req, resp);
	                }
	                else {
	                	session.setAttribute("user", user);
	                    session.setAttribute("userId", user.getId());
	                    session.setAttribute("role", role);
	                    session.setAttribute("img", user.getImg());
	                    resp.sendRedirect("Homepage");
	                    
	                }
	            } catch (SQLException e) {
	                throw new RuntimeException(e);
	            }
	        } catch (Exception e) {
	            throw new ServletException("Error connecting to the database", e);
	        }


	    }*/
	    

}
