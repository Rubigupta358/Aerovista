 <%@ page import="java.util.List"%>
<%@ page import="Servlets.model.Sale"%>
<%@ page import="Servlets.dao.SalesDAO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
 
<!DOCTYPE html>
<html>
<head>
<title>Sales</title>
<link rel="stylesheet" type="text/css" href="style.css">

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container my-5">

    <!-- Heading -->
	<h2 class="text-center mb-4">Existing Sales</h2>

    <!-- Existing Sales Table -->
	<table class="table table-bordered table-striped">
		<thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Product ID</th>
                <th>Quantity</th>
                <th>Revenue</th>
                <th>Sale Date</th>
            </tr>
        </thead>
        <tbody>
		<%
            // ✅ fallback: agar servlet ne data na bheja ho
            List<Sale> sales = (List<Sale>) request.getAttribute("sales");
            if (sales == null) {
                Servlets.dao.SalesDAO dao = new Servlets.dao.SalesDAO();
                sales = dao.getAllSales();
            }

            if (sales != null && !sales.isEmpty()) {
                for (Sale s : sales) {
        %>
		<tr>
			<td><%=s.getSaleId()%></td>
			<td><%=s.getProductId()%></td>
			<td><%=s.getQuantity()%></td>
			<td><%=s.getRevenue()%></td>
			<td><%=s.getSaleDate()%></td>
		</tr>
		<%
		        }
            } else {
        %>
        <tr>
            <td colspan="5" class="text-center text-muted">No sales recorded</td>
        </tr>
        <% } %>
        </tbody>
	</table>

    <!-- Add Sale Button -->
    <div class="text-center my-4">
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addSaleModal">
            Add Sale
        </button>
    </div>

    <div class="mt-3 text-center">
	    <a href="index.jsp" class="btn btn-secondary">Back to Home</a>
    </div>

</div>

<!-- Add Sale Modal -->
<div class="modal fade" id="addSaleModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header bg-primary text-white">
        <h5 class="modal-title">Add Sale</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- Modal Body -->
      <div class="modal-body">
        <form action="SaleServlet" method="post">
            <div class="mb-3">
                <label class="form-label">Product ID</label>
                <input type="number" name="productId" class="form-control" placeholder="Enter product ID" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Quantity</label>
                <input type="number" name="quantity" class="form-control" placeholder="Enter quantity" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Revenue</label>
                <input type="number" step="0.01" name="revenue" class="form-control" placeholder="Enter revenue amount" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Sale Date</label>
                <input type="date" name="saleDate" class="form-control" required>
            </div>
            <div class="text-center">
                <input type="submit" class="btn btn-success px-4" value="Save">
            </div>
        </form>
      </div>

    </div>
  </div>
</div>
         
</body>
    
</html> 
 