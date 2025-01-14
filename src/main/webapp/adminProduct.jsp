<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Quản lý sản phẩm</title>
</head>
<body>
    <h1>Danh sách sản phẩm</h1>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Mô tả</th>
                <th>Giá</th>
                <th>Danh mục</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.description}</td>
                    <td>${product.price}</td>
                    <td>${product.category.title}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
