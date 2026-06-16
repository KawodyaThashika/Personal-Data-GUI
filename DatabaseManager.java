import java.sql.*;

public class DatabaseManager {
    private static final String URL  = "jdbc:mysql://localhost:3306/personal_db";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void insertData(String name, String dob, String gender, String favourites) {
        String sql = "INSERT INTO personal_data (name, dob, gender, favourites) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, dob);
            ps.setString(3, gender); ps.setString(4, favourites);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static ResultSet getAllData() throws SQLException {
        Connection con = getConnection();
        return con.createStatement().executeQuery("SELECT * FROM personal_data");
    }

    public static boolean updateData(int id, String name, String dob, String gender, String favourites) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE personal_data SET name=?, dob=?, gender=?, favourites=? WHERE id=?")) {
            ps.setString(1, name); ps.setString(2, dob);
            ps.setString(3, gender); ps.setString(4, favourites); ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteData(int id) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM personal_data WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
