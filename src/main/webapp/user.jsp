<%@ page import="java.util.List" %>
<%@ page import="models.User" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="dao.DBConnectionPool" %>
<%
    Connection con = DBConnectionPool.getDataSource().getConnection();
    UserDAO userDAO = new UserDAO(con);
    List<User> userList = userDAO.getAllUsers(); // Lấy danh sách người dùng
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Người Dùng</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        .actions {
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <h2>Quản lý Người Dùng</h2>
    
    <!-- Thêm người dùng mới -->
    <div class="actions">
        <a href="addUser.jsp">Thêm Người Dùng</a>
    </div>
    
    <!-- Bảng danh sách người dùng -->
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Địa chỉ</th>
                <th>Số điện thoại</th>
                <th>Vai trò</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <%
                for (User user : userList) {
            %>
            <tr>
                <td><%= user.getId() %></td>
                <td><%= user.getUsername() %></td>
                <td><%= user.getEmail() %></td>
                <td><%= user.getAddress() %></td>
                <td><%= user.getPhone() %></td>
                <td><%= user.getIsAdmin() == 1 ? "Admin" : "User" %></td>
                <td>
                    <a href="editUser.jsp?id=<%= user.getId() %>">Sửa</a> |
                    <a href="deleteUser?id=<%= user.getId() %>" onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này?');">Xóa</a>
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</body>
</html>
