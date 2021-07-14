
import java.sql.*;

public class DbConnection {
    String imeBaze = "test.db";
    String url = "jdbc:sqlite:" + imeBaze;

    void createTables() {
        String sql = """
                 CREATE TABLE IF NOT EXISTS graphs ( 
                 id INTEGER PRIMARY KEY,
                 algorithm TEXT NOT NULL,
                 elapsed_time TEXT NOT NULL
                 );
                """;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}