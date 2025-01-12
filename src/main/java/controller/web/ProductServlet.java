package controller.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import dao.CategoryDAO;
import dao.DBConnectionPool;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Category;
import models.Product;

// hiển thị thông tin sản phẩm
@WebServlet("/product")
public class ProductServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnectionPool.getDataSource().getConnection()) { // Lấy connection từ pool

			// Nhận ID sản phẩm từ yêu cầu
			String productId = request.getParameter("id");

			// Không có ID sản phẩm 
			if (productId == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
			}

			// Lấy thông tin sản phẩm từ ProductDAO
			ProductDAO productDAO = new ProductDAO();
			Product product = productDAO.getProductById(Integer.parseInt(productId));

			// Kiểm tra nếu sản phẩm không tồn tại
			if (product == null) {
				// Xử lý khi không tìm thấy sản phẩm
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
			}

			// Nếu sản phẩm tồn tại
			List<Product> productList = new ProductDAO().getAllProducts();
			request.setAttribute("productList", productList);

			// Thiết lập thuộc tính cho request
			request.setAttribute("product", product);
			// Chuyển hướng đến product-detail.jsp
			request.getRequestDispatcher("product-detail.jsp").forward(request, response);

		} catch (Exception e) {
			throw new ServletException("Error connecting to the database", e);
		}

	}

}
