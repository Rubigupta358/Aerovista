package Servlets.web;

import Servlets.dao.InvestmentDAO;
import Servlets.model.Investment;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/InvestmentServlet")
public class InvestmentServlet extends HttpServlet {

    private InvestmentDAO dao = new InvestmentDAO();

    // ✅ POST: Add Investment
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String month = request.getParameter("month");
            String investmentType = request.getParameter("investmentType");
            double amount = Double.parseDouble(request.getParameter("amount"));

            Investment inv = new Investment();
            inv.setProductId(productId);
            inv.setMonth(month);
            inv.setInvestmentType(investmentType);
            inv.setAmount(amount);

            dao.addInvestment(inv);

            // ✅ Redirect so that table refreshes with updated data
            response.sendRedirect("InvestmentServlet");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error while saving investment", e);
        }
    }

    // ✅ GET: Show all Investments
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Investment> investments = dao.getAllInvestments();
            request.setAttribute("investments", investments);
            request.getRequestDispatcher("investment.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error while fetching investment list", e);
        }
    }
}
