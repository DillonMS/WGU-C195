package smith.c195v2;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import smith.c195v2.helper.CustomerQuery;
import smith.c195v2.helper.JDBC;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class customerController {
    public Button returnButton;
    public TableColumn idColumn;
    public TableColumn nameColumn;
    public TableColumn addressColumn;
    public TableColumn cityColumn;
    public TableColumn stateColumn;
    public TableColumn countryColumn;
    public TableColumn zipColumn;
    public TableColumn phoneColumn;
    public TableView customerTable;

    ObservableList<Customer> customerTableList = FXCollections.observableArrayList();

    public void onReturnClick(ActionEvent actionEvent) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }

    public void initialize() throws SQLException {

        customerTable.setItems(customerTableList);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("firstLevelDivision"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        String sql = "SELECT * FROM customers ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            Customer sqlCustomer = new Customer();
            sqlCustomer.setCustomerID(rs.getInt("Customer_ID"));
            sqlCustomer.setCustomerName(rs.getString("Customer_Name"));
            sqlCustomer.setAddress(rs.getString("Address"));
            sqlCustomer.setPostalCode(rs.getString("Postal_Code"));
            sqlCustomer.setPhone(rs.getString("Phone"));
            sqlCustomer.setDivisionID(rs.getInt("Division_ID"));

            int divisionID = sqlCustomer.getDivisionID();

            String fLevelDivision = CustomerQuery.getFirstLevelDivision(divisionID);
            sqlCustomer.setFirstLevelDivision(fLevelDivision);

            String country = CustomerQuery.getCountry(divisionID);
            sqlCustomer.setCountry(country);


            customerTableList.add(sqlCustomer);
        }






    }


}
