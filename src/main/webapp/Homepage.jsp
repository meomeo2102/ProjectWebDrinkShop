<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
	<title>Shop Homepage</title>
	<link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
	<link href="css/styles.css" rel="stylesheet" />
</head>


<body>

	<!-- Navbar -->
	<%@ include file="template/includes/navbar.jsp"%>

	<!-- Main Content Area -->
	<div class="container">
		<h1>${requestScope.welcome}</h1>
		<jsp:include page="template/static/home.jsp">
			<jsp:param name="contentPage" value="template/static/home.jsp" />
		</jsp:include>
	</div>

	<%@ include file="template/includes/footer.jsp"%>

	<!-- Bootstrap core JS-->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
