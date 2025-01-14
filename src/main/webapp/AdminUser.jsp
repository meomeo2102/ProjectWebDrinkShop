<%@ page import="models.User" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Quản lý người dùng</title>
</head>
<body>
    <h1>Danh sách người dùng</h1>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên người dùng</th>
                <th>Email</th>
                <th>Vai trò</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<User> users = (List<User>) request.getAttribute("userList");
                for (User user : users) {
            %>
            <tr>
                <td><%= user.getId() %></td>
                <td><%= user.getUsername() %></td>
                <td><%= user.getEmail() %></td>
                <td><%= user.getRole() %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>
