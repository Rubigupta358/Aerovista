 <%@ page import="java.util.List"%>
<%@ page import="Servlets.model.Investment"%>
<%@ page import="Servlets.dao.InvestmentDAO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
<%@ include file="chatbot.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>Investments</title>
<link rel="stylesheet" type="text/css" href="style.css">

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
 <div style="max-width:900px; margin:30px auto; display:flex; align-items:center;">

    <!-- Center Heading -->
    <h2 style="flex:1; text-align:center; margin:0;">Existing Investment</h2>

    <!-- Right Button -->
    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addProductModal"   >
        Add Investment
    </button>
</div>

<div style="max-width:1300px; margin:0 auto;">

    <!-- Existing Investments Table -->
	<table class="table table-bordered table-striped">
		<thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Product ID</th>
                <th>Month</th>
                <th>Investment Type</th>
                <th>Amount</th>
            </tr>
        </thead>
        <tbody>
		<%
            // ✅ fallback: agar servlet ne data na bheja ho
            List<Investment> investments = (List<Investment>) request.getAttribute("investments");
            if (investments == null) {
                Servlets.dao.InvestmentDAO dao = new Servlets.dao.InvestmentDAO();
                investments = dao.getAllInvestments();
            }

            if (investments != null && !investments.isEmpty()) {
                for (Investment inv : investments) {
        %>
		<tr>
			<td><%=inv.getInvId()%></td>
			<td><%=inv.getProductId()%></td>
			<td><%=inv.getMonth()%></td>
			<td><%=inv.getInvestmentType()%></td>
			<td><%=inv.getAmount()%></td>
		</tr>
		<%
		        }
            } else {
        %>
        <tr>
            <td colspan="5" class="text-center text-muted">No investments recorded</td>
        </tr>
        <% } %>
        </tbody>
	</table>

    
     

     
</div>

 

<!-- Add Investment Modal -->
<div class="modal fade" id="addInvestmentModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header bg-primary text-white">
        <h5 class="modal-title">Add Investment</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- Modal Body -->
      <div class="modal-body">
        <form action="InvestmentServlet" method="post">
            <div class="mb-3">
                <label class="form-label">Product ID</label>
                <input type="number" name="productId" class="form-control" placeholder="Enter product ID" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Month</label>
                <input type="text" name="month" class="form-control" placeholder="Enter month (e.g. Jan-2025)" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Investment Type</label>
                <input type="text" name="investmentType" class="form-control" placeholder="Enter investment type" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Amount</label>
                <input type="number" step="0.01" name="amount" class="form-control" placeholder="Enter amount" required>
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
 