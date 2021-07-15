package projekt.db;

import java.sql.*;

public class DbConnection {
    static String imeBaze = "graphs.db";
    static String url = "jdbc:sqlite:" + imeBaze;

    public static void createTables() {
        String sql = """
                 CREATE TABLE IF NOT EXISTS graphs ( 
                 graphName TEXT PRIMARY KEY,
                 nodes TEXT NOT NULL
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

    public static void insert(String graphName, String nodes) {
        String sql = "INSERT INTO graphs (graphName, nodes) VALUES( ? ,?);";
        try {
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, graphName);
            pstmt.setString(2, nodes);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getGraphByName(String graphName) {
        String sql = "SELECT graphName, nodes FROM graphs WHERE graphName = ?";
        try {
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, graphName);
            ResultSet rs = pstmt.executeQuery(sql);
            return rs.getString("nodes");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}