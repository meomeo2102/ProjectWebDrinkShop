package controller.web;


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

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/secure/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String LOGIN_PAGE = "login.jsp";
    private static final String CART_PAGE = "cart.jsp";
    private static final String ERROR_INVALID_INPUT = "Invalid input.";
    private static final String ERROR_PROCESSING_REQUEST = "An error occurred while processing your request.";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product not found.";
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        dataSource = DBConnectionPool.getDataSource();
        if (dataSource == null) {
            throw new ServletException("Failed to initialize DataSource.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = getUserIdFromSession(request.getSession());
        if (isUserNotLoggedIn(userId)) {
            System.out.println("User not logged in. Redirecting to login page.");
            // Get the context path dynamically
            String contextPath = getServletContext().getContextPath();
            // Prepend the context path to the redirect URL
            response.sendRedirect(contextPath + LOGIN_PAGE);
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Fetching the cart for userId: " + userId);
            
            CartDAO cartDAO = new CartDAO(connection);
            Cart cart = cartDAO.getCartByUserId(userId);
            request.getSession().setAttribute("cart", cart);
            
            // Display the user's cart
            handleCartDisplay(request, response, userId, cartDAO);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while displaying cart: " + e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_REQUEST);
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = getUserIdFromSession(request.getSession());
        if (isUserNotLoggedIn(userId)) {
            redirectToPage(response, LOGIN_PAGE);
            return;
        }

        // Debugging: Log all parameters sent in the request
        System.out.println("POST Request Parameters:");
        request.getParameterMap().forEach((key, value) -> System.out.println(key + ": " + String.join(",", value)));

        String action = request.getParameter("action");
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");

        // Log action and parameters
        System.out.println("Action: " + action);
        System.out.println("Product ID: " + productIdParam);
        System.out.println("Quantity: " + quantityParam);

        if (isInvalidInput(action)) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            CartDAO cartDAO = new CartDAO(connection);

            switch (action) {
                case "addToCart": // Added new case
                    ProductDAO productDAO = new ProductDAO(); // Dependency required for handleAddToCart
                    handleAddToCart(request, response, userId, cartDAO, productDAO);
                    break;
                case "updateQuantity":
                    updateQuantity(request, response, userId, cartDAO);
                    break;
                case "removeItem":
                    removeItem(request, response, userId, cartDAO);
                    break;
                default:
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Unsupported action.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred.");
        }
    }

    private void handleCartDisplay(HttpServletRequest request, HttpServletResponse response, int userId, CartDAO cartDAO) throws ServletException, IOException {
    	
    	
    	Cart cart = cartDAO.getCartByUserId(userId);
        System.out.println(cart.toString());
        if (cart == null || cart.getItems().isEmpty()) {
            System.out.println("Cart is empty or not found for userId: " + userId);
            request.setAttribute("errorMessage", "Your cart is empty.");
            request.getSession().setAttribute("cart", null);
        } else {
            System.out.println("Cart retrieved for userId: " + userId + " - " + cart);
            request.getSession().setAttribute("cart", cart);
        }

        // Forward to the cart JSP page for rendering
        request.getRequestDispatcher(CART_PAGE).forward(request, response);
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, int userId, CartDAO cartDAO, ProductDAO productDAO) throws IOException {
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");

        System.out.println("Add to Cart invoked - ProductId: " + productIdParam + ", Quantity: " + quantityParam);

        if (isInvalidInput(productIdParam) || isInvalidInput(quantityParam)) {

            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_INPUT);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);

            validateProductQuantity(quantity, response);

            Product product = productDAO.getProductById(productId);
            if (product == null) {
                System.out.println("Product not found for productId: " + productId);
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, ERROR_PRODUCT_NOT_FOUND);
                return;
            }

            addItemToCart(userId, cartDAO, product, quantity);
            System.out.println("Product added to cart. Redirecting to cart page.");
            redirectToPage(response, "/cart");
        } catch (NumberFormatException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_INPUT);
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_REQUEST);
        }
    }

    private Integer getUserIdFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            System.out.println("Session does not contain a userId.");
        }
        return userId;
    }

    private boolean isUserNotLoggedIn(Integer userId) {
        return userId == null || userId <= 0;
    }

    private boolean isInvalidInput(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void redirectToPage(HttpServletResponse response, String page) throws IOException {
        // Get the context path dynamically
        String contextPath = getServletContext().getContextPath();
        // Prepend the context path to the redirect URL
        response.sendRedirect(contextPath + page);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(message);
    }

    private void validateProductQuantity(int quantity, HttpServletResponse response) throws IOException {
        if (quantity <= 0) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity.");
            throw new IllegalArgumentException("Invalid quantity");
        }
    }

    private void addItemToCart(int userId, CartDAO cartDAO, Product product, int quantity) throws SQLException {
        Cart cart = cartDAO.getCartByUserId(userId);
        if (cart == null) {
            System.out.println("Creating a new cart for userId: " + userId);
            cart = new Cart(0, userId); // Default cartId is 0 temporarily
            cartDAO.createCart(cart);
        }

        cart.addItem(product.getId(), quantity);
        cartDAO.updateCart(cart);
    }

    private void updateQuantity(HttpServletRequest request, HttpServletResponse response, int userId, CartDAO cartDAO) throws IOException {
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");
        System.out.println("Update quantity invoked - ProductId: " + productIdParam + ", Quantity: " + quantityParam);

        if (isInvalidInput(productIdParam) || isInvalidInput(quantityParam)) {
            System.out.println("Invalid input: Product ID or Quantity is missing.");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid input.\"}");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);

            if (quantity <= 0) {
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid quantity.\"}");
                return;
            }

            Cart cart = cartDAO.getCartByUserId(userId);
            if (cart == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Cart not found.\"}");
                return;
            }

            // Update quantity in the cart
            cart.updateQuantity(productId, quantity);
            cartDAO.updateCart(cart);
            double itemTotalPrice = cart.getItemTotalPrice(productId); // Ensure Cart class has this function
            double totalCartPrice = cart.getTotalPrice();

            // Respond with both the updated individual item price and total price
            response.getWriter().write("{\"success\": true, \"updatedItemTotal\": " + itemTotalPrice + ", \"totalCartPrice\": " + totalCartPrice + "}");
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID or quantity format.");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid product ID or quantity.\"}");
        }
    }

    private void removeItem(HttpServletRequest request, HttpServletResponse response, int userId, CartDAO cartDAO) throws IOException {
        String productIdParam = request.getParameter("productId");

        if (isInvalidInput(productIdParam)) {
            System.out.println("Invalid input: Product ID is missing.");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid product ID.\"}");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);

            Cart cart = cartDAO.getCartByUserId(userId);
            if (cart == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Cart not found.\"}");
                return;
            }

            // Remove item from the cart
            cart.removeItem(productId);
            cartDAO.updateCart(cart);
            double totalCartPrice = cart.getTotalPrice();

            // Respond with updated total price
            response.getWriter().write("{\"success\": true, \"totalCartPrice\": " + totalCartPrice + "}");
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID format.");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid product ID.\"}");
        }
    }
}