<html>
<head>
    <title>Dashboard Admin</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }
        header {
            background-color: #333;
            color: #fff;
            padding: 15px 20px;
            text-align: center;
        }
        nav {
            width: 200px;
            height: 100vh;
            position: fixed;
            top: 0;
            left: 0;
            background-color: #2c3e50;
            color: #fff;
            padding-top: 20px;
        }
        nav ul {
            list-style-type: none;
            padding: 0;
        }
        nav ul li {
            padding: 10px 20px;
        }
        nav ul li a {
            color: #fff;
            text-decoration: none;
            display: block;
        }
        nav ul li a:hover {
            background-color: #34495e;
        }
        main {
            margin-left: 200px;
            padding: 20px;
        }
        .card {
            background-color: #fff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            display: inline-block;
            width: 30%;
            text-align: center;
        }
        .card h3 {
            margin: 10px 0;
            color: #333;
        }
        .card p {
            font-size: 18px;
            color: #555;
        }
    </style>
</head>
<body>
    <header>
        <h1>Admin Dashboard</h1>
    </header>
    <nav>
        <ul>
            <li><a href="admin_dashboard.jsp">Dashboard</a></li>
            <li><a href="admin_products.jsp">Quản lý sản phẩm</a></li>
            <li><a href="admin_users.jsp">Quản lý người dùng</a></li>
            <li><a href="admin_categories.jsp">Quản lý danh mục</a></li>
            <li><a href="admin_statistics.jsp">Thống kê</a></li>
            <li><a href="logout">Đăng xuất</a></li>
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
    </main>
</body>
</html>
