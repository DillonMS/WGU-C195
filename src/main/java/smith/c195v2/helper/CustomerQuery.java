package smith.c195v2.helper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * all sql queries based on the customer table
 */
public abstract class CustomerQuery {

    /**
     * Inserts customer into database.
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param dID
     * @throws SQLException
     */
    public static void insertCustomer(String name, String address, String postalCode, String phoneNumber, int dID) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2, address);
        ps.setString(3,postalCode);
        ps.setString(4,phoneNumber);
        ps.setInt(5,dID);
        ps.executeUpdate();
    }

    /**
     * removes customer from database
     * @param customerID customer to delete.
     * @throws SQLException
     */
    public static void removeCustomer(int customerID) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = " + customerID +";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * Updates customer based on the ID given.
     * @param customerID
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param dID
     * @throws SQLException
     */
    public static void updateCustomer(int customerID, String name, String address, String postalCode, String phoneNumber, int dID) throws SQLException{
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2, address);
        ps.setString(3,postalCode);
        ps.setString(4,phoneNumber);
        ps.setInt(5,dID);
        ps.setInt(6,customerID);
        ps.executeUpdate();

    }

    /**
     * get the first level division based on the division ID provided
     * @param divisionID
     * @return String of the first level division
     * @throws SQLException
     */
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

    /**
     * returns ID based on the first level division provided
     * @param division
     * @return ID
     * @throws SQLException
     */
    public static int getDivisionID(String division) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,division);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getInt("Division_ID");
        }
        else
            return -1;
    }


    /**
     * get the country based on the division ID
     * @param divisionID used to get the country
     * @return String of country
     * @throws SQLException
     */
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

    /**
     * Checks whether there is an appointment for a given customer ID.  Cannot delete customer if
     * customer has appointment.
     * @param customerID
     * @return return true or false
     * @throws SQLException
     */
    public static boolean customerAppointments(int customerID) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,customerID);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}

