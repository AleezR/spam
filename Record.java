package carbonfp;
import java.sql.*;
public class Record { 
     final String DB_URL;
     final String USER;
     final String PASS;

 
    public Record(String dbUrl, String user, String pass) {
        this.DB_URL = dbUrl;
        this.USER = user;
        this.PASS = pass;
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
    public boolean saveRecord(CarbonRecord record) { 
        String SQL = "INSERT INTO carbon_records (user_name, transport_mode, transport_km, electricity_kwh, garbage_kg, total_co2) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, record.name);
            pstmt.setString(2, record.mode);
            pstmt.setDouble(3, record.transportKm);
            pstmt.setDouble(4, record.electricityKwh);
            pstmt.setDouble(5, record.garbageKg);
            pstmt.setDouble(6, record.totalCo2);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; 

        } catch (SQLException ex) {
            ex.printStackTrace(); 
            return false;
        }
    }
    	public String getHistory() {
        String SQL = "SELECT user_name, transport_mode, total_co2 FROM carbon_records ORDER BY id DESC";
        String records = "HISTORY\n";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            boolean foundRecords = false;
            while (rs.next()) {
                foundRecords = true;
                String name = rs.getString("user_name");
                String mode = rs.getString("transport_mode");
                double co2 = rs.getDouble("total_co2");

                records += name + " (Mode: " + mode + ")" + " - Total CO2: " + String.format("%.2f", co2) + " kg\n";
            }
            if (!foundRecords) {
                return "Error: No records found in the database.";
            }
            return records;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error: Could not connect to Supabase or execute query: " + ex.getMessage();
        }
    }
}