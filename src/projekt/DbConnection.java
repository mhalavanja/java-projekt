package projekt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnection {
    static String imeBaze = "graphs.db";
    static String url = "jdbc:sqlite:" + imeBaze;
    static Connection conn = null;

    private static void setConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url);
            } catch (
                    SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void createTables() {
        String sql = """
                 CREATE TABLE IF NOT EXISTS graphs ( 
                 graphName TEXT PRIMARY KEY,
                 nodes TEXT NOT NULL
                 );
                """;
        try {
            Connection conn = DriverManager.getConnection(url);
            setConnection();
            Statement stmt = conn.createStatement();
            if (conn != null) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void upsertGraph(String graphName, String nodes) {
        String sql = "INSERT INTO graphs (graphName, nodes) VALUES(?,?) ON CONFLICT(graphName) DO UPDATE SET nodes = ?;";
        try {
            setConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, graphName);
            pstmt.setString(2, nodes);
            pstmt.setString(3, nodes);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getGraphByName(String graphName) {
        String sql = "SELECT graphName, nodes FROM graphs WHERE graphName = ?";
        try {
            setConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, graphName);
            ResultSet rs = pstmt.executeQuery(sql);
            return rs.getString("nodes");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ArrayList<String> getAllGraphNames() {
        String sql = "SELECT graphName FROM graphs";
        try {
            setConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<String> graphNameList = new ArrayList<>();
            while (rs.next()){
                graphNameList.add(rs.getString("graphName"));
            }
            return graphNameList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}