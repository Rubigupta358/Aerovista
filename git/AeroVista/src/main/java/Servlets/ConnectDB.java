 package Servlets;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    private static Connection con;

    public static Connection getConnectDB() throws Exception {
        if (con == null || con.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/company_project", 
                "root", 
                "root"
            );
        }
        return con;
    }
}
