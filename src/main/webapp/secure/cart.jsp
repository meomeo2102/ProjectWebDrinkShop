<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/template/includes/headerResource.jsp" />

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
								<c:forEach var="cartItem" items="${cart.items.values() }">
									<div id="row-${cartItem.product.id }">
										<div class="row cart-item mb-3">
											<div class="col-md-3">
												<img
													src="${pageContext.request.contextPath}/image/product/${cartItem.product.photo }"
													alt="${cartItem.product.name }" class="img-fluid rounded">
											</div>
											<div class="col-md-5">
												<h5 class="card-title">${cartItem.product.name }</h5>
												<p class="text-muted">Category: ${cartItem.product.name }</p>
											</div>
											<div class="col-md-2">
												<div class="input-group">
													<button class="btn btn-outline-secondary btn-sm"
														type="button" onclick="changeQuantity(-1, this)">-</button>
													<input style="max-width: 100px" type="number"
														class="form-control  form-control-sm text-center quantity-input"
														onchange="updateQuantity('<c:out value="${fn:escapeXml(cartItem.product.id)}"/>', this)"
														value="${cartItem.quantity}" min="1" />
													<button class="btn btn-outline-secondary btn-sm"
														type="button">+</button>
												</div>
											</div>
											<div class="col-md-2 text-end">
												<p class="fw-bold">$ ${cartItem.product.price }</p>

												<!-- delete cartItem -->
												<button class="btn btn-sm btn-outline-danger"
													onclick="removeItem('<c:out value="${fn:escapeXml(cartItem.product.id)}"/>', document.getElementById('row-${fn:escapeXml(cartItem.product.id)}'))">
													<i class="bi bi-trash"></i>
												</button>
											</div>
										</div>
										<hr>
									</div>
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
					<a href="${pageContext.request.contextPath}/Homepage"
						class="btn btn-outline-primary"> <i
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

						<!--  SUMMARY -->
						<div class="d-flex justify-content-between mb-3">
							<span>Subtotal</span> <span id="subtotal" class="subtotal"><fmt:formatNumber
									value="${subtotal}" type="currency" /></span>
						</div>
						<div class="d-flex justify-content-between mb-3">
							<span>Shipping</span> <span id="shipping" class="shipping"><fmt:formatNumber
									value="${shipping}" type="currency" /></span>
						</div>
						<hr>
						<div class="d-flex justify-content-between mb-4">
							<strong>Total</strong> <strong id="total" class="total"><fmt:formatNumber
									value="${total}" type="currency" /></strong>
						</div>

						<!-- SUMMARY -->
						<c:choose>
							<c:when test="${subtotal <= 0}">
								<button id="checkoutButton" class="btn btn-warning w-100"
									disabled>Proceed to Checkout</button>
							</c:when>
							<c:otherwise>
								<button id="checkoutButton" class="btn btn-primary w-100">Proceed
									to Checkout</button>
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
	<script>
		function removeItem(productId, rowElement) {
			$.ajax({
				url : '/zzzz/removeItem',
				type : 'POST',
				data : {
					id : productId
				},
				success : function(response) {
					if (response.success) {
						alert()
						// Xóa dòng sản phẩm khỏi giao diện
						rowElement.remove();
						// Cập nhật lại Summary
						const subtotal1 = response.subtotal;
						const shipping1 = response.shipping;
						const total1 = response.total;
						// Gán giá trị cho các phần tử

						document.getElementById('subtotal').innerHTML = "$"
								+ subtotal1;
						document.getElementById('shipping').innerHTML = "$"
								+ shipping1;
						document.getElementById('total').innerHTML = "$"
								+ total1;

						const checkoutButton = document.getElementById('checkoutButton');
		                if (response.subtotal <= 0) {
		                    checkoutButton.classList.remove('btn-primary');
		                    checkoutButton.classList.add('btn-warning');
		                    checkoutButton.setAttribute('disabled', 'disabled');
		                    checkoutButton.textContent = 'Proceed to Checkout';
		                } else {
		                    checkoutButton.classList.remove('btn-warning');
		                    checkoutButton.classList.add('btn-primary');
		                    checkoutButton.removeAttribute('disabled');
		                    checkoutButton.textContent = 'Proceed to Checkout';
		                }
		                
					} else {
						alert('Có lỗi xảy ra: ' + response.message);
					}
				},
				error : function() {
					alert('Có lỗi xảy ra khi gửi yêu cầu.');
				}
			});
		}
	</script>

</body>
</html>