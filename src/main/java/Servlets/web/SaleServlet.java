package Servlets.web;

import Servlets.dao.SalesDAO;
import Servlets.model.Sale;
import com.google.gson.*;
import java.io.*;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/SaleServlet")
public class SaleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SalesDAO dao = new SalesDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();

        try {
            Sale sale = new Sale();

            if (contentType != null && contentType.contains("application/json")) {
                // JSON request (chatbot)
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = request.getReader()) {
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                }

                JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
                JsonObject params = json.getAsJsonObject("params");

                sale.setProductId(params.get("productId").getAsInt());
                sale.setQuantity(params.get("quantity").getAsInt());
                sale.setRevenue(params.get("revenue").getAsDouble());
                sale.setSaleDate(Date.valueOf(params.get("sale_date").getAsString()));

                dao.addSale(sale);

                // Respond with JSON
                JsonObject respJson = new JsonObject();
                respJson.addProperty("status", "success");
                respJson.addProperty("message", "✅ Sale added successfully!");
                response.setContentType("application/json");
                response.getWriter().write(respJson.toString());

            } else {
                // Normal HTML form submission
                sale.setProductId(Integer.parseInt(request.getParameter("productId")));
                sale.setQuantity(Integer.parseInt(request.getParameter("quantity")));
                sale.setRevenue(Double.parseDouble(request.getParameter("revenue")));
                sale.setSaleDate(Date.valueOf(request.getParameter("saleDate")));

                dao.addSale(sale);

                // Redirect to show updated list
                response.sendRedirect("SaleServlet");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Send error JSON if chatbot request
            if (contentType != null && contentType.contains("application/json")) {
                JsonObject respJson = new JsonObject();
                respJson.addProperty("status", "error");
                respJson.addProperty("message", e.getMessage());
                response.setContentType("application/json");
                response.getWriter().write(respJson.toString());
            } else {
                throw new ServletException(e);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Sale> sales = dao.getAllSales();
            request.setAttribute("sales", sales);
            request.getRequestDispatcher("sales.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
