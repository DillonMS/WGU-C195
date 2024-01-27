package smith.c195v2.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AppointmentQuery {

    public static String getContact(int contactID) throws SQLException {
        String sql = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,contactID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getString("Contact_Name");
        }
        else
            return "";
    }

}
