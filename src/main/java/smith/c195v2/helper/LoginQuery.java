package smith.c195v2.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class LoginQuery {

    public static boolean checkLogin(String userName, String password) throws SQLException{
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,userName);
        ps.setString(2,password);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return true;
        else
            return false;


    }



}
