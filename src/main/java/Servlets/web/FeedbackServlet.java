package Servlets.web;

import Servlets.dao.FeedbackDAO;
import Servlets.model.Feedback;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FeedbackDAO dao = new FeedbackDAO();

    // ✅ POST: Add Feedback
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String featureName = request.getParameter("featureName");
            int rating = Integer.parseInt(request.getParameter("rating"));
            String feedbackText = request.getParameter("feedbackText");

            Feedback f = new Feedback(0, productId, featureName, rating, feedbackText);
            dao.addFeedback(f);

            // ✅ redirect to doGet() so updated list shows
            response.sendRedirect("FeedbackServlet");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // ✅ GET: Show all Feedback
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Feedback> feedbacks = dao.getAllFeedbacks();
            request.setAttribute("feedbacks", feedbacks);
            request.getRequestDispatcher("feedback.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
