package smith.c195v2.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * First Level Division Queries
 */
public abstract class FLDQuery {


    /**
     * get a list of states based on the country provided
     * @param countryID
     * @return list of states
     * @throws SQLException
     */
    public static ObservableList<String> getStateList (int countryID) throws SQLException {
        ObservableList<String> stateList = FXCollections.observableArrayList();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,countryID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            stateList.add(rs.getString("Division"));
        }
        return stateList;
    }

}
