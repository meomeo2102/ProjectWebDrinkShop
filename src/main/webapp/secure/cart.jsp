<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
	rel="stylesheet" />
<link href="../css/styles.css" rel="stylesheet" />
<title>Shopping Cart</title>
</head>
<body>

	<!-- Navbar -->
	<%@ include file="/template/includes/navbar.jsp"%>

	<div class="container py-5">
		<h1 class="mb-5">Your Shopping Cart</h1>
		<div class="row">
			<div class="col-lg-8">
				<!-- Cart Items -->
				<div class="card mb-4">
					<div class="card-body">
						<c:choose>
							<c:when test="${not empty cart.items }">
								<c:forEach var="cartItem" items="${cart.items.value() }">
									<div class="row cart-item mb-3">
										<div class="col-md-3">
											<img src="/image/product/${cartItem.product.photo }"
												alt="${cartItem.product.name }" class="img-fluid rounded">
										</div>
										<div class="col-md-5">
											<h5 class="card-title">${cartItem.product.name }</h5>
											<p class="text-muted">Category: ${cartItem.product.name }</p>
										</div>
										<div class="col-md-2">
											<div class="input-group">
												<button class="btn btn-outline-secondary btn-sm"
													type="button">-</button>
												<input style="max-width: 100px" type="text"
													class="form-control  form-control-sm text-center quantity-input"
													onchange="updateQuantity('<c:out value="${fn:escapeXml(cartItem.product.id)}"/>', this)"
													value="${cartItem.quantity }" min="1">
												<button class="btn btn-outline-secondary btn-sm"
													type="button">+</button>
											</div>
										</div>
										<div class="col-md-2 text-end">
											<p class="fw-bold">${cartItem.product.price }</p>
											<button class="btn btn-sm btn-outline-danger">
												<i class="bi bi-trash"></i>
											</button>
										</div>
									</div>
									<hr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<div class="row cart-item mb-3">
									<div class="col-md-5">
										<h5 class="card-title">Your Cart Is Empty</h5>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<!-- Continue Shopping Button -->
				<div class="text-start mb-4">
					<a href="${pageContext.request.contextPath}/Homepage" class="btn btn-outline-primary"> <i
						class="bi bi-arrow-left me-2"></i>Continue Shopping
					</a>
				</div>
			</div>

			<!-- calculate Summary -->
			<c:set var="subtotal" value="0" />
			<c:forEach var="cartItem" items="${cart.items.values()}">
				<c:set var="subtotal"
					value="${subtotal + (cartItem.product.price * cartItem.quantity)}" />
			</c:forEach>
			<c:set var="shipping" value="10.00" />
			<c:set var="total" value="${subtotal + shipping}" />



			<!-- Cart Summary -->
			<div class="col-lg-4">
				<div class="card cart-summary">
					<div class="card-body">
						<h5 class="card-title mb-4">Order Summary</h5>
						<div class="d-flex justify-content-between mb-3">
							<span>Subtotal</span> <span><fmt:formatNumber
									value="${subtotal}" type="currency" /></span>
						</div>
						<div class="d-flex justify-content-between mb-3">
							<span>Shipping</span> <span><fmt:formatNumber
									value="${shipping}" type="currency" /></span>
						</div>
						<hr>
						<div class="d-flex justify-content-between mb-4">
							<strong>Total</strong> <strong><fmt:formatNumber
									value="${total}" type="currency" /></strong>
						</div>

						<c:choose>
							<c:when test="${subtotal <= 0}">
								<button class="btn btn-warning w-100" disabled>Proceed
									to Checkout</button>
							</c:when>
							<c:otherwise>
								<button class="btn btn-primary w-100">Proceed to
									Checkout</button>
							</c:otherwise>
						</c:choose>

					</div>
				</div>
				<!-- Promo Code -->
				<div class="card mt-4">
					<div class="card-body">
						<h5 class="card-title mb-3">Apply Promo Code</h5>
						<div class="input-group mb-3">
							<input type="text" class="form-control"
								placeholder="Enter promo code">
							<button class="btn btn-outline-secondary" type="button">Apply</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/template/includes/footer.jsp"%>

</body>
</html>