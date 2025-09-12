<%@ page import="java.util.List"%>
<%@ page import="Servlets.model.Feedback"%>
<%@ page import="Servlets.dao.FeedbackDAO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="navbar.jsp"%>
<%@ include file="chatbot.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>Feedback Records</title>
<link rel="stylesheet" type="text/css" href="style.css">

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Bootstrap JS -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

	<!-- Heading + Add Feedback Button -->
	<div
		style="max-width: 900px; margin: 30px auto; display: flex; align-items: center;">

		<!-- Center Heading -->
		<h2 style="flex: 1; text-align: center; margin: 0;">Existing
			Feedback</h2>

		<!-- Right Button -->
		<button class="btn btn-primary" data-bs-toggle="modal"
			data-bs-target="#addFeedbackModal">Add Feedback</button>
	</div>

	<!-- Existing Feedback Table -->
	<div style="max-width: 1300px; margin: 0 auto;">
		<table class="table table-bordered table-striped text-center">
			<thead class="table-dark">
				<tr>
					<th>ID</th>
					<th>Product ID</th>
					<th>Feature</th>
					<th>Rating</th>
					<th>Feedback</th>
				</tr>
			</thead>
			<tbody>
				<%
                List<Feedback> feedbacks = (List<Feedback>) request.getAttribute("feedbacks");
                if (feedbacks == null) {
                    FeedbackDAO dao = new FeedbackDAO();
                    feedbacks = dao.getAllFeedbacks();
                }

                if (feedbacks != null && !feedbacks.isEmpty()) {
                    for (Feedback f : feedbacks) {
            %>
				<tr>
					<td><%= f.getFeedbackId() %></td>
					<td><%= f.getProductId() %></td>
					<td><%= f.getFeatureName() %></td>
					<td><%= f.getRating() %></td>
					<td><%= f.getFeedbackText() %></td>
				</tr>
				<% 
                    }
                } else { 
            %>
				<tr>
					<td colspan="5" class="text-center text-muted">No feedbacks
						found</td>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>

	<!-- Add Feedback Modal -->
	<div class="modal fade" id="addFeedbackModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h5 class="modal-title">Add Feedback</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">
					<form action="FeedbackServlet" method="post">
						<div class="mb-3">
							<label class="form-label">Product ID</label> <input type="number"
								name="productId" class="form-control" required>
						</div>
						<div class="mb-3">
							<label class="form-label">Feature Name</label> <input type="text"
								name="featureName" class="form-control" required>
						</div>
						<div class="mb-3">
							<label class="form-label">Rating (1-5)</label> <input
								type="number" name="rating" min="1" max="5" class="form-control"
								required>
						</div>
						<div class="mb-3">
							<label class="form-label">Feedback</label>
							<textarea name="feedbackText" class="form-control" rows="3"
								required></textarea>
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
