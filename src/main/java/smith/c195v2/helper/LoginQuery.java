package smith.c195v2.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * login sql queries
 */
public abstract class LoginQuery {

    /**
     * Checks if user name and password are approved/in the database
     * @param userName
     * @param password
     * @return true/false depending on whether the input matches or not
     * @throws SQLException
     */
    public static boolean checkLogin(String userName, String password) throws SQLException{
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,userName);
        ps.setString(2,password);
        ResultSet rs = ps.executeQuery();
        return rs.next();


    }

}
