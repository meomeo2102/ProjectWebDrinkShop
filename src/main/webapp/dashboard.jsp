<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        /* Thêm các style của bạn ở đây */
        .card {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table th, table td {
            padding: 8px 12px;
            border: 1px solid #ddd;
        }
        .message {
            padding: 10px;
            margin-bottom: 20px;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <header>
        <h1>Admin Dashboard</h1>
    </header>
    <nav>
        <ul>
            <li><a href="/admin/dashboard">Dashboard</a></li>
            <li><a href="/admin/products">Quản lý sản phẩm</a></li>
            <li><a href="/admin/users">Quản lý người dùng</a></li>
            <li><a href="/admin/categories">Quản lý danh mục</a></li>
            <li><a href="/admin/statistics">Thống kê</a></li>
            <li><a href="/logout">Đăng xuất</a></li>
        </ul>
    </nav>
    <main>
        <h2>Chào mừng bạn đến với Admin Dashboard</h2>

        <div class="card">
            <h3>Tổng số sản phẩm</h3>
            <p>${totalProducts}</p>
        </div>
        <div class="card">
            <h3>Tổng số người dùng</h3>
            <p>${totalUsers}</p>
        </div>
        <div class="card">
            <h3>Tổng số danh mục</h3>
            <p>${totalCategories}</p>
        </div>
        <div class="card">
            <h3>Doanh thu hôm nay</h3>
            <p>${totalRevenue}</p>
        </div>

        <h3>User List</h3>
        <c:if test="${not empty message}">
            <div class="message success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>
                            <form action="/admin/dashboard" method="post">
                                <input type="hidden" name="action" value="deleteUser">
                                <input type="hidden" name="userId" value="${user.id}">
                                <input type="submit" value="Delete">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h3>Product List</h3>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="product" items="${productList}">
                    <tr>
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>${product.price}</td>
                        <td>
                            <form action="/admin/dashboard" method="post">
                                <input type="hidden" name="action" value="deleteProduct">
                                <input type="hidden" name="productId" value="${product.id}">
                                <input type="submit" value="Delete">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
</body>
</html>
