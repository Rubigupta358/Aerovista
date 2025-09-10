package Servlets;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectDB {
    private static Connection con;

    public static Connection getConnectDB() throws Exception {
        if (con == null || con.isClosed()) {
            // Load properties file from classpath
            Properties prop = new Properties();
            try  (InputStream input = ConnectDB.class.getClassLoader().getResourceAsStream("config.properties")){
            		System.out.println("DEBUG >> config.properties stream = " + input);
 
            	
                if (input == null) {
                    throw new RuntimeException("config.properties not found in classpath!");
                }
                prop.load(input);
            }

            String DB_USER = prop.getProperty("DB_USER");
            String DB_PASSWORD = prop.getProperty("DB_PASSWORD");

            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/company_project",
                DB_USER,
                DB_PASSWORD
            );
        }
        return con;
    }
}
