package Servlets.web;

import Servlets.dao.InvestmentDAO;
import Servlets.model.Investment;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

  
@WebServlet("/InvestmentServlet")
public class InvestmentServlet extends HttpServlet {

    private InvestmentDAO dao = new InvestmentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();

        try {
            Investment inv = new Investment();

            if (contentType != null && contentType.contains("application/json")) {
                // ✅ JSON request (chatbot)
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = request.getReader()) {
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                }

                JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
                JsonObject params = json.getAsJsonObject("params");

                inv.setProductId(params.get("productId").getAsInt());
                inv.setMonth(params.get("month").getAsString());
                inv.setInvestmentType(params.get("investmentType").getAsString());
                inv.setAmount(params.get("amount").getAsDouble());

                dao.addInvestment(inv);

                // ✅ JSON Response
                JsonObject respJson = new JsonObject();
                respJson.addProperty("status", "success");
                respJson.addProperty("message", "✅ Investment added successfully!");
                response.setContentType("application/json");
                response.getWriter().write(respJson.toString());

            } else {
                // ✅ Normal HTML form submission
                inv.setProductId(Integer.parseInt(request.getParameter("productId")));
                inv.setMonth(request.getParameter("month"));
                inv.setInvestmentType(request.getParameter("investmentType"));
                inv.setAmount(Double.parseDouble(request.getParameter("amount")));

                dao.addInvestment(inv);

                // Redirect to updated list
                response.sendRedirect("InvestmentServlet");
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            List<Investment> investments = dao.getAllInvestments();
            request.setAttribute("investments", investments);
            request.getRequestDispatcher("investment.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error while fetching investment list", e);
        }
    }
}
