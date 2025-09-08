<%@ page import="java.util.List" %>
<%@ page import="Servlets.model.Product" %>
<%@ page import="Servlets.dao.ProductDAO" %>   <%-- ✅ added --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
 <%@ include file="chatbot.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Products</title>
    <link rel="stylesheet" type="text/css" href="style.css">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head> 
<body>
<div class="container my-5">

    <!-- Heading -->
    <h2 class="text-center mb-4">Existing Products</h2>

    <!-- Existing Products Table -->
    <table class="table table-bordered table-striped"  >
        <thead class="table-dark" >
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Category</th>
                <th>Price</th>
                <th>Launch Date</th>
            </tr>
        </thead>
        <tbody>
            <%
                // ✅ fallback: agar servlet se products null aaye
                List<Product> products = (List<Product>) request.getAttribute("products");
                if (products == null) {
                    Servlets.dao.ProductDAO dao = new Servlets.dao.ProductDAO();
                    products = dao.getAllProducts();
                }

                if(products != null && !products.isEmpty()) {
                    for(Product p : products) {
            %>
            <tr>
                <td><%= p.getProductId() %></td>
                <td><%= p.getName() %></td>
                <td><%= p.getCategory() %></td>
                <td><%= p.getPrice() %></td>
                <td><%= p.getLaunchDate() %></td>
            </tr>
            <% 
                    }
                } else { 
            %>
            <tr>
                <td colspan="5" class="text-center text-muted">No products available</td>
            </tr>
            <% } %>
        </tbody>
    </table>

    <!-- Add Product Button -->
    <div class="text-center my-4">
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addProductModal">
            Add Product
        </button>
    </div>
    <div class="mt-3 text-center">
	    <a href="index.jsp" class="btn btn-secondary">Back to Home</a>
    </div>

</div>

<!-- Add Product Modal -->
<div class="modal fade" id="addProductModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h5 class="modal-title">Add Product</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- Modal Body -->
      <div class="modal-body">
        <form action="ProductServlet" method="post">
            <div class="mb-3">
                <label class="form-label">Product Name</label>
                <input type="text" name="name" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Category</label>
                <input type="text" name="category" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Price</label>
                <input type="number" name="price" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Launch Date</label>
                <input type="date" name="launch_date" class="form-control" required>
            </div>
            <div class="text-center">
                <input type="submit" class="btn btn-success" value="Save">
            </div>
        </form>
      </div>

    </div>
  </div>
</div>

</body>
</html>
