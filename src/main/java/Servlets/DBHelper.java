package Servlets;

import java.sql.*;
import java.util.*;

public class DBHelper {

    // Product
    public static boolean addProduct(String name, double price) {
        String sql = "INSERT INTO product (name, price) VALUES (?, ?)";
        try (Connection conn = ConnectDB.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Monthly sale
    public static boolean addSale(String month, double sales) {
        String sql = "INSERT INTO monthly_sales (month, sales) VALUES (?, ?)";
        try (Connection conn = ConnectDB.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, month);
            ps.setDouble(2, sales);
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Monthly investment
    public static boolean addInvestment(String month, double investment) {
        String sql = "INSERT INTO monthly_investment (month, investment) VALUES (?, ?)";
        try (Connection conn = ConnectDB.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, month);
            ps.setDouble(2, investment);
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Product ROI
    public static boolean addProductRoi(String product, double roi) {
        String sql = "INSERT INTO product_roi (product, roi) VALUES (?, ?)";
        try (Connection conn = ConnectDB.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product);
            ps.setDouble(2, roi);
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Feature rating
    public static boolean addFeatureRating(String feature, double rating) {
        String sql = "INSERT INTO feature_rating (feature, rating) VALUES (?, ?)";
        try (Connection conn = ConnectDB.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, feature);
            ps.setDouble(2, rating);
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Region sales
    public static boolean addRegionSales(String region, double sales) {
        String sql = "INSERT INTO region_sales (region, sales) VALUES (?, ?)";
        try (Connection conn = ConnectDB.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, region);
            ps.setDouble(2, sales);
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Query helpers (return list of maps)
    private static List<Map<String,Object>> runSelect(String sql, String... params) {
        List<Map<String,Object>> list = new ArrayList<>();
        try (Connection conn = ConnectDB.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setString(i+1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData m = rs.getMetaData();
                int c = m.getColumnCount();
                while (rs.next()) {
                    Map<String,Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= c; i++) row.put(m.getColumnName(i), rs.getObject(i));
                    list.add(row);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static List<Map<String,Object>> getMonthlySales() {
        return runSelect("SELECT month, sales FROM monthly_sales ORDER BY month");
    }
    public static List<Map<String,Object>> getMonthlyInvestment() {
        return runSelect("SELECT month, investment FROM monthly_investment ORDER BY month");
    }
    public static List<Map<String,Object>> getProductRoi() {
        return runSelect("SELECT product, roi FROM product_roi");
    }
    public static List<Map<String,Object>> getFeatureRating() {
        return runSelect("SELECT feature, rating FROM feature_rating");
    }
    public static List<Map<String,Object>> getRegionSales() {
        return runSelect("SELECT region, sales FROM region_sales");
    }
    public static List<Map<String,Object>> listProducts() {
        return runSelect("SELECT id, name, price FROM product");
    }
}
