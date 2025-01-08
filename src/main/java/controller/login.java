package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
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
	        try {
	            
	        	String email = req.getParameter("email");
	            String pass = req.getParameter("password");
	            String message = "Sai thông tin tài khoản mật khẩu ";
	            UserDAO udao = new UserDAO();

	            String role = "";
	            
	            HttpSession session = req.getSession();
	            try {
	                User user = udao.getLogin(email, pass);
	                if (user == null) {
	                    req.setAttribute("message", message);
	                    req.getRequestDispatcher("/Login.jsp").forward(req, resp);
	                }
	                else {
	                	role = user.getIsAdmin() == 1 ? "admin" : "user";
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


	    }
	    

}
