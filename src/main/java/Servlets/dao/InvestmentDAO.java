package Servlets.dao;

import Servlets.ConnectDB;
import Servlets.model.Investment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestmentDAO {

    public void addInvestment(Investment inv) throws Exception {
        Connection conn = ConnectDB.getConnectDB();
        String sql = "INSERT INTO Investment(product_id, month, investment_type, amount) VALUES(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, inv.getProductId());
        ps.setString(2, inv.getMonth());
        ps.setString(3, inv.getInvestmentType());
        ps.setDouble(4, inv.getAmount());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public List<Investment> getAllInvestments() throws Exception {
        Connection conn = ConnectDB.getConnectDB();
        String sql = "SELECT * FROM Investment";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Investment> list = new ArrayList<>();
        while(rs.next()) {
            Investment inv = new Investment();
            inv.setInvId(rs.getInt("inv_id"));
            inv.setProductId(rs.getInt("product_id"));
            inv.setMonth(rs.getString("month"));
            inv.setInvestmentType(rs.getString("investment_type"));
            inv.setAmount(rs.getDouble("amount"));
            list.add(inv);
        }
        rs.close();
        stmt.close();
        conn.close();
        return list;
    }
}
