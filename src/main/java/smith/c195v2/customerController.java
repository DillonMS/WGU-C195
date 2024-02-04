package smith.c195v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import smith.c195v2.helper.CustomerQuery;
import smith.c195v2.helper.JDBC;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * methods for the customer page
 */
public class customerController {
    public Button returnButton;
    public TableColumn idColumn;
    public TableColumn nameColumn;
    public TableColumn addressColumn;
    public TableColumn stateColumn;
    public TableColumn countryColumn;
    public TableColumn zipColumn;
    public TableColumn phoneColumn;
    public TableView customerTable;
    public Button deleteButton;
    public Button addCustomerButton;
    public Button modifyButton;

    ObservableList<Customer> customerTableList = FXCollections.observableArrayList();

    /**
     * returns user to the main page
     * @param actionEvent
     * @throws IOException
     */
    public void onReturnClick(ActionEvent actionEvent) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }

    /**
     * Deletes the selected customer, or throws error if no customer selected. Cannot delete user
     * if user has appointments.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onDeleteClick(ActionEvent actionEvent) throws IOException, SQLException {
        Customer selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        }
        boolean hasAppointments = CustomerQuery.customerAppointments(selectedCustomer.getCustomerID());
        if (hasAppointments){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer Appointments");
            alert.setContentText("Customer has appointments scheduled. Please delete all appointments associated with customer before deleting customer.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to delete this customer?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int cID = selectedCustomer.getCustomerID();
            CustomerQuery.removeCustomer(cID);
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Information");
            alert2.setHeaderText("Customer Deleted");
            alert2.setContentText("The Customer with ID: " + cID + " has been deleted");
            alert2.showAndWait();
        }
    }

    /**
     * takes user to add customer screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddClick(ActionEvent actionEvent) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addCustomer-view.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }

    /**
     * sets customer table
     * @throws SQLException
     */
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

    /**
     * takes user to modify screen, or throws error if no user is selected.
     * @param actionEvent
     */
    public void onModifyClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyCustomer-view.fxml"));
            Parent root = loader.load();

            ModifyCustomerController mpController = loader.getController();
            Customer selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            mpController.passCustomer(selectedCustomer);

            Scene mainScreenScene = new Scene(root);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Please select a customer to Modify.");
            alert.showAndWait();
        }
    }
}
