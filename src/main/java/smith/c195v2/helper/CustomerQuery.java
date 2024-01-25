package smith.c195v2.helper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerQuery {

    public static int insertCustomer(String name, String address, int postalCode, int phoneNumber, int dID) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2, address);
        ps.setInt(3,postalCode);
        ps.setInt(4,phoneNumber);
        ps.setInt(5,dID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int removeCustomer(int customerID) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = " + customerID +";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int updateCustomer(int customerID, String name, String address, int postalCode, int phoneNumber) throws SQLException{
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2, address);
        ps.setInt(3,postalCode);
        ps.setInt(4,phoneNumber);
        ps.setInt(5,customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static String getFirstLevelDivision(int divisionID) throws SQLException {
        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,divisionID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getString("Division");
        }
        else
            return "";
    }

    public static String getCountry(int divisionID) throws SQLException{
        String sql = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,divisionID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            int countryID = rs.getInt("Country_ID");
            String sql2 = "SELECT Country FROM countries WHERE Country_ID = ?";
            PreparedStatement ps2 = JDBC.connection.prepareStatement(sql2);
            ps2.setInt(1,countryID);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()){
                return rs2.getString("Country");
            }
            else
                return "";


        }
        else
            return "";

    }

}

