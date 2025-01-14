<%@ page import="models.Category" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Quản lý danh mục</title>
</head>
<body>
    <h1>Danh sách danh mục</h1>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên danh mục</th>
                <th>Mô tả</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Category> categories = (List<Category>) request.getAttribute("categoryList");
                for (Category category : categories) {
            %>
            <tr>
                <td><%= category.getId() %></td>
                <td><%= category.getTitle() %></td>
                <td><%= category.getDescription() %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>
