package smith.c195v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import smith.c195v2.helper.CustomerQuery;
import smith.c195v2.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddAppointmentController {

    public TableView customerTable;
    public TableColumn customerIDCol;
    public TableColumn customerNamecol;
    public TextField iDTextBox;
    public TextField titleTextBox;
    public TextField descriptionTextBox;
    public TextField locationTextBox;
    public TextField typeTextBox;
    public TextField CustomerIDTextBox;
    public ComboBox contactCombo;
    public ComboBox startCombo;
    public ComboBox endCombo;
    public ComboBox userIDCombo;
    public DatePicker dateTextBox;

    ObservableList<Customer> customerList = FXCollections.observableArrayList();


    public void initialize() throws SQLException {
        customerTable.setItems(customerList);

        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNamecol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        String sql = "SELECT * FROM customers ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Customer sqlCustomer = new Customer();
            sqlCustomer.setCustomerID(rs.getInt("Customer_ID"));
            sqlCustomer.setCustomerName(rs.getString("Customer_Name"));


            customerList.add(sqlCustomer);
        }
    }
}
