package Servlets.web;

import Servlets.dao.SalesDAO;
import Servlets.model.Sale;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
 
@WebServlet("/SaleServlet")
public class SaleServlet extends HttpServlet {
    private SalesDAO dao = new SalesDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            double revenue = Double.parseDouble(request.getParameter("revenue"));
            Date saleDate = Date.valueOf(request.getParameter("saleDate"));

            Sale s = new Sale();
            s.setProductId(productId);
            s.setQuantity(quantity);
            s.setRevenue(revenue);
            s.setSaleDate(saleDate);

            dao.addSale(s);

            response.sendRedirect("SaleServlet"); // ✅ table update hoga
        } catch(Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Sale> sales = dao.getAllSales();
            request.setAttribute("sales", sales);
            request.getRequestDispatcher("sales.jsp").forward(request, response);
        } catch(Exception e) {
            throw new ServletException(e);
        }
    }
}
