package Servlets.web;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Servlets.dao.ProductDAO;
import Servlets.model.Product;
 

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDAO dao = new ProductDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            double price = Double.parseDouble(request.getParameter("price"));
            String launchDateStr = request.getParameter("launch_date");
            Date launchDate = Date.valueOf(launchDateStr); 

            Product p = new Product(0, name, category, price, launchDate);
            dao.addProduct(p);

            // ✅ redirect to doGet() so updated list shows
            response.sendRedirect("ProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<Product> products = dao.getAllProducts();
            request.setAttribute("products", products);
            request.getRequestDispatcher("productForm.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
