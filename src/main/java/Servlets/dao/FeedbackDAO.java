package Servlets.dao;

import Servlets.ConnectDB;
import Servlets.model.Feedback;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    public void addFeedback(Feedback f) throws Exception {
        Connection conn = ConnectDB.getConnectDB();
        String sql = "INSERT INTO survey(product_id, feature_name, rating, feedback_text) VALUES(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, f.getProductId());
        ps.setString(2, f.getFeatureName());
        ps.setInt(3, f.getRating());
        ps.setString(4, f.getFeedbackText());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public List<Feedback> getAllFeedbacks() throws Exception {
        Connection conn = ConnectDB.getConnectDB();
        String sql = "SELECT * FROM survey";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Feedback> list = new ArrayList<>();
        while(rs.next()) {
            Feedback f = new Feedback();
            f.setFeedbackId(rs.getInt("survey_id")); // survey table ka primary key
            f.setProductId(rs.getInt("product_id"));
            f.setFeatureName(rs.getString("feature_name"));
            f.setRating(rs.getInt("rating"));
            f.setFeedbackText(rs.getString("feedback_text"));
            list.add(f);
        }
        rs.close();
        stmt.close();
        conn.close();
        return list;
    }
}
