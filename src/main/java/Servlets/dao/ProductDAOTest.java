package Servlets.dao;

import Servlets.model.Product;
import java.sql.Date;
import java.util.List;

 
public class ProductDAOTest {
    public static void main(String[] args) {
        try {
            ProductDAO dao = new ProductDAO();

            // --------- OPTIONAL: Insert a sample product ----------
            // If you DO NOT want to insert a new row, comment out or remove the block below.
            Product p = new Product();
            p.setName("Test Product X");        // change name if you want
            p.setCategory("Test Category");
            p.setPrice(999.00);
            p.setLaunchDate(Date.valueOf("2024-09-04")); // format YYYY-MM-DD
            dao.addProduct(p);
            System.out.println("Inserted test product (if insert block is enabled).");
            // -----------------------------------------------------

            // Fetch and print products
            List<Product> list = dao.getAllProducts();
            System.out.println("---- Products in DB ----");
            for (Product prod : list) {
                System.out.println(
                    prod.getProductId() + " | " +
                    prod.getName() + " | " +
                    prod.getCategory() + " | " +
                    prod.getPrice() + " | " +
                    prod.getLaunchDate()
                );
            }
            System.out.println("---- End ----");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}
