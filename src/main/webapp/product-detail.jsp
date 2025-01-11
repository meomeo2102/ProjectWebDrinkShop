<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="template/includes/headerResource.jsp" />

<title>Insert title here</title>
</head>
<body>
	<%@ include file="template/includes/navbar.jsp"%>

	<c:set var="singleProduct" value="${requestScope.product}" />
	<!-- Product section-->
	<section class="py-5">
		<div class="container px-4 px-lg-5 my-5">
			<div class="row gx-4 gx-lg-5 align-items-center">
				<div class="col-md-6">
					<img class="card-img-top mb-5 mb-md-0"
						src="${pageContext.request.contextPath}/image/product/${product.photo }"
						alt="..." />
				</div>
				<div class="col-md-6">
					<div class="small mb-1"><i>PRODUCT_ID :</i> ${product.id }</div>
					<h1 class="display-5 fw-bolder">${product.name }</h1>
					<div class="fs-5 mb-5">
						<span>$ ${product.price }</span>
					</div>
					<p class="lead">${product.description }</p>
					<div class="d-flex">
						<input class="form-control text-center me-3" name="inputQuantity"
							type="number" value="1" style="max-width: 3rem" />
						<button class="btn btn-outline-dark flex-shrink-0" type="button">
							<i class="bi-cart-fill me-1"></i> Add to cart
						</button>
					</div>
				</div>
			</div>
		</div>
	</section>

	<section class="py-5 bg-light">
		<div class="container px-4 px-lg-5 mt-5">
			<h2 class="fw-bolder mb-4">Other Item</h2>
			<c:choose>
				<c:when test="${not empty productList }">
					<div
						class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
						<c:forEach var="product" items="${productList}" varStatus="status">
							<c:if test="${status.index < 4}">
								<!-- Show only the first 4 products -->
								<div class="col mb-5">
									<div class="card h-100">
										<a href="./product?id=${product.id}"> <!-- Product image-->
											<img class="card-img-top bg-dark"
											src="${pageContext.request.contextPath}/image/product/${product.photo }"
											alt="${product.name}" />
										</a>
										<!-- Product details-->
										<div class="card-body p-4">
											<div class="text-center">
												<!-- Product name-->
												<h5 class="fw-bolder">${product.name }</h5>
												<!-- Product reviews-->
												<div
													class="d-flex justify-content-center small text-warning mb-2">
													<div class="bi-star-fill"></div>
													<div class="bi-star-fill"></div>
													<div class="bi-star-fill"></div>
													<div class="bi-star-fill"></div>
													<div class="bi-star-fill"></div>
												</div>
												<!-- Product price-->
												$ ${product.price}
											</div>
										</div>
										<!-- Product actions-->
										<div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
											<div class="text-center">
												<a class="btn btn-outline-dark mt-auto" href="#">View
													options</a>
											</div>
										</div>
									</div>
								</div>
							</c:if>
						</c:forEach>
					</div>
					<div class="text-center">
						<button id="show-more" class="btn btn-outline-dark mt-4">Show
							More</button>
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-md-12">
						<p>No products available.</p>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</section>
</body>
</html>