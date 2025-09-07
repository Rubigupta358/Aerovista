package Servlets.dao;

import Servlets.ConnectDB;
import Servlets.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void addProduct(Product p) throws Exception {
        String sql = "INSERT INTO Products(name, category, price, launch_date) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setDate(4, p.getLaunchDate()); // java.sql.Date

            ps.executeUpdate();
        }
    }

    public List<Product> getAllProducts() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product_id, name, category, price, launch_date FROM Products ORDER BY product_id";
        try (Connection con = ConnectDB.getConnectDB();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setCategory(rs.getString("category"));
                p.setPrice(rs.getDouble("price"));
                p.setLaunchDate(rs.getDate("launch_date"));
                list.add(p);
            }
        }
        return list;
    }

    public Product getProductById(int id) throws Exception {
        String sql = "SELECT product_id, name, category, price, launch_date FROM Products WHERE product_id = ?";
        try (Connection con = ConnectDB.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setCategory(rs.getString("category"));
                    p.setPrice(rs.getDouble("price"));
                    p.setLaunchDate(rs.getDate("launch_date"));
                    return p;
                }
            }
        }
        return null;
    }
}
