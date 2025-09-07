 package Servlets.dao;

import Servlets.ConnectDB;
import Servlets.model.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class SalesDAO {

    public void addSale(Sale s) throws Exception {
        Connection conn = ConnectDB.getConnectDB();
        String sql = "INSERT INTO Sales(product_id, quantity, revenue, sale_date) VALUES(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, s.getProductId());
        ps.setInt(2, s.getQuantity());
        ps.setDouble(3, s.getRevenue());
        ps.setDate(4, s.getSaleDate());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public List<Sale> getAllSales() throws Exception {
        Connection conn = ConnectDB.getConnectDB();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");
        List<Sale> list = new ArrayList<>();
        while(rs.next()) {
            Sale s = new Sale();
            s.setSaleId(rs.getInt("sale_id"));
            s.setProductId(rs.getInt("product_id"));
            s.setQuantity(rs.getInt("quantity"));
            s.setRevenue(rs.getDouble("revenue"));
            s.setSaleDate(rs.getDate("sale_date"));
            list.add(s);
        }
        rs.close();
        stmt.close();
        conn.close();
        return list;
    }
}
