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
        String SQL = "INSERT INTO carbon_records (" +
                "user_name, total_co2, height, weight, body_type, gender, diet, " +
                "social_activity, transport_mode, vehicle_type, vehicle_monthly_km, " +
                "air_travel_frequency, waste_bag_size, waste_bag_count, recycled_materials, " +
                "cooking_source, cooking_systems, energy_efficiency, pc_tv_hours, " +
                "internet_hours, shower_frequency, grocery_bill, clothes_monthly) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, record.name);
            pstmt.setDouble(2, record.totalCo2);
            pstmt.setDouble(3, record.height);
            pstmt.setDouble(4, record.weight);
            pstmt.setString(5, record.bodyType);
            pstmt.setString(6, record.gender);
            pstmt.setString(7, record.diet);
            
            pstmt.setString(8, record.socialActivity);
            pstmt.setString(9, record.transportMode);
            pstmt.setString(10, record.vehicleType);
            pstmt.setDouble(11, record.vehicleMonthlyKm);
            
            pstmt.setString(12, record.airTravelFrequency);
            pstmt.setString(13, record.wasteBagSize);
            pstmt.setInt(14, record.wasteBagCount);
            pstmt.setString(15, record.recycledMaterials);
            
            pstmt.setString(16, record.cookingSource);
            pstmt.setString(17, record.cookingSystems);
            pstmt.setString(18, record.energyEfficiency); 
            pstmt.setInt(19, record.pcTvHours);
            
            pstmt.setInt(20, record.internetHours);
            pstmt.setString(21, record.showerFrequency);
            pstmt.setDouble(22, record.groceryBill);
            pstmt.setInt(23, record.clothesMonthly);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getHistory() {
        String SQL = "SELECT user_name, transport_mode, total_co2 FROM carbon_records ORDER BY id DESC";
        String records = "HISTORY (Annual CO2)\n";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            boolean foundRecords = false;
            while (rs.next()) {
                foundRecords = true;
                String name = rs.getString("user_name");
                String mode = rs.getString("transport_mode");
                double co2 = rs.getDouble("total_co2");

                records += name + " (Mode: " + mode + ")" + " - Total CO2: " + String.format("%.2f", co2) + " kg/year\n";
            }
            if (!foundRecords) {
                return "Error: No records found in the database.";
            }
            return records;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error: Could not connect to database or execute query: " + ex.getMessage();
        }
    }
}
