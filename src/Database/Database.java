package Database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author Gilang Ramadhan
 */
public class Database {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    
    static final String DB_HOST = "jdbc:mysql://localhost/car_rental";
    static final String DB_PASSWORD = "";
    static final String DB_USERNAME = "root";
    
    public static Connection conn;
    public static Statement stmt;
    public static ResultSet rs;
    
    public void Database() {
        try {
            Class.forName(JDBC_DRIVER);
            
            conn = DriverManager.getConnection(DB_HOST, DB_USERNAME, DB_PASSWORD);
            
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
