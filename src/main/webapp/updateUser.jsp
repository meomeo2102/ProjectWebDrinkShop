<%@ page import="java.sql.Connection" %>
<%@ page import="dao.DBConnectionPool" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="models.User" %>
<%
    int userId = Integer.parseInt(request.getParameter("id"));
    String username = request.getParameter("username");
    String email = request.getParameter("email");
    String address = request.getParameter("address");
    String phone = request.getParameter("phone");
    int isAdmin = Integer.parseInt(request.getParameter("isAdmin"));

    Connection con = DBConnectionPool.getDataSource().getConnection();
    UserDAO userDAO = new UserDAO(con);

    User user = new User();
    user.setId(userId);
    user.setUsername(username);
    user.setEmail(email);
    user.setAddress(address);
    user.setPhone(phone);
    user.setIsAdmin(isAdmin);

    boolean isUpdated = userDAO.updateUser(user); // Hàm updateUser sẽ xử lý cập nhật thông tin người dùng

    if (isUpdated) {
        response.sendRedirect("users.jsp"); // Chuyển hướng lại trang danh sách người dùng
    } else {
        out.println("Cập nhật không thành công");
    }
%>
