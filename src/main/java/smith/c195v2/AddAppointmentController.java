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
import smith.c195v2.helper.AppointmentQuery;
import smith.c195v2.helper.CustomerQuery;
import smith.c195v2.helper.JDBC;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

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
    public TextField contactEmail;
    public TextField contactTextBox;
    public Button selectButton;
    public ComboBox contactComboBox;
    public Button saveButton;

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

        // Combo Box Times
        LocalTime time1 = LocalTime.of(00, 00);
        LocalTime time2 = LocalTime.of(23, 45);
        while (!time1.equals(time2)) {
            startCombo.getItems().addAll(time1);
            endCombo.getItems().addAll(time1);
            time1 = time1.plusMinutes(15);
        }
        startCombo.getItems().addAll(time2);
        endCombo.getItems().addAll(time2);
        userIDCombo.getItems().addAll("test", "admin");
        contactComboBox.getItems().addAll("Anika Costa", "Daniel Garcia", "Li Lee");

    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainScreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }

    public void onSelectClick(ActionEvent actionEvent) {
        Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        String customerID = Integer.toString(customer.getCustomerID());
        CustomerIDTextBox.setText(customerID);
    }

    public void onSaveClick(ActionEvent actionEvent) {
        try {
            String title = titleTextBox.getText();
            String description = descriptionTextBox.getText();
            String location = locationTextBox.getText();
            String type = typeTextBox.getText();
            int customerID = Integer.parseInt(CustomerIDTextBox.getText());
            String userName = (String) userIDCombo.getSelectionModel().getSelectedItem();
            int userID = AppointmentQuery.getUserID(userName);
            String contactName = (String) contactComboBox.getSelectionModel().getSelectedItem();
            int contactID = AppointmentQuery.getContactID(contactName);

            LocalDate start = dateTextBox.getValue();

            String startDate = start.toString();
            LocalTime startTime = (LocalTime) startCombo.getValue();
            String startTimeString = startTime.toString();
            LocalTime endTime = (LocalTime) endCombo.getValue();
            String endTimeString = endTime.toString();
            String startDT = startDate + " " + startTimeString + ":00";
            String endDT = startDate + " " + endTimeString + ":00";


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to Save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                AppointmentQuery.insertAppointment(title, description, location, type, startDT, endDT, customerID, userID, contactID);

                Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            }
        } catch (Exception e) {
            System.out.println("error");
        }
    }
}
