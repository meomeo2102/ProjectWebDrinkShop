<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Navigation-->
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container px-4 px-lg-5">
                <a class="navbar-brand" href="#!">Boba Station</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
                        <li class="nav-item"><a class="nav-link active" aria-current="page" href="#!">Home</a></li>
                        <li class="nav-item"><a class="nav-link" href="#!">About</a></li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">Shop</a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li><a  class="dropdown-item" href="#!" href="${pageContext.request.contextPath}/category?id=all">All Products</a></li>
								<c:set var="categoryList" value="${applicationScope.categoryList}"/>
								<c:if test="${categoryList != null}">
                            	<c:forEach var="item" items="${categoryList}">
	                                <li>
	                                    <a class="dropdown-item border-bottom"
	                                       href="${pageContext.request.contextPath}/category?id=${ item.getId()}"> ${ item.getTitle()}
	                                    </a>
	                                </li>
                            	</c:forEach>
                        </c:if>
                        <c:if test="${categoryList == null}">
                            <li>No categories available.</li>
                        </c:if>
                                <li><hr class="dropdown-divider" /></li>
                                <li><a class="dropdown-item" href="#!">Popular Items</a></li>
                                <li><a class="dropdown-item" href="#!">New Arrivals</a></li>
                            </ul>
                        </li>
                    </ul>
                    <form class="d-flex px-4">
                        <button class="btn btn-outline-dark" type="submit">
                            <i class="bi-cart-fill me-1"></i>
                            Cart
                            <span class="badge bg-dark text-white ms-1 rounded-pill">0</span>
                        </button>
                    </form>
                     <form class="d-flex">
                        <button class="btn btn-outline-dark" type="submit">
                            <i class="badge bg-dark text-white ms-1 rounded-pill">login</i>
                        </button>
                    </form>
                </div>
            </div>
        </nav>