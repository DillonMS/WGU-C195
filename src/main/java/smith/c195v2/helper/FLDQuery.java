package smith.c195v2.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class FLDQuery {


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
