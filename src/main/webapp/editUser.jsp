
<%@ page import="java.sql.Connection"%>
<%@ page import="dao.DBConnectionPool"%>
<%@ page import="dao.UserDAO"%>
<%@ page import="models.User"%>
<%
    int userId = Integer.parseInt(request.getParameter("id")); // Lấy id người dùng từ URL
    Connection con = DBConnectionPool.getDataSource().getConnection();
    UserDAO userDAO = new UserDAO(con);
    User user = userDAO.getUser(userId); // Lấy thông tin người dùng
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Chỉnh Sửa Thông Tin Người Dùng</title>
</head>
<body>
	<h2>Chỉnh Sửa Thông Tin Người Dùng</h2>

	<form action="updateUser.jsp" method="post">
		<input type="hidden" name="id" value="<%= user.getId() %>">
		<!-- Để truyền ID người dùng -->

		<label for="username">Username:</label><br> <input type="text"
			id="username" name="username" value="<%= user.getUsername() %>"
			required><br>
		<br> <label for="email">Email:</label><br> <input
			type="email" id="email" name="email" value="<%= user.getEmail() %>"
			required><br>
		<br> <label for="address">Địa Chỉ:</label><br> <input
			type="text" id="address" name="address"
			value="<%= user.getAddress() %>" required><br>
		<br> <label for="phone">Số Điện Thoại:</label><br> <input
			type="text" id="phone" name="phone" value="<%= user.getPhone() %>"
			required><br>
		<br> <label for="isAdmin">Vai Trò:</label><br> <select
			name="isAdmin" id="isAdmin">
			<option value="1" <%= user.getIsAdmin() == 1 ? "selected" : "" %>>Admin</option>
			<option value="0" <%= user.getIsAdmin() == 0 ? "selected" : "" %>>User</option>
		</select><br>
		<br> <input type="submit" value="Cập Nhật">
	</form>
</body>
</html>

