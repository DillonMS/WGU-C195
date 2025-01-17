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
import smith.c195v2.helper.JDBC;
import smith.c195v2.helper.utconversion;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.Optional;

/**
 * methods used for the add appointment screen
 */
public class AddAppointmentController {

    public TableView customerTable;
    public TableColumn customerIDCol;
    public TableColumn customerNameCol;
    public TextField iDTextBox;
    public TextField titleTextBox;
    public TextField descriptionTextBox;
    public TextField locationTextBox;
    public TextField typeTextBox;
    public TextField CustomerIDTextBox;
    public ComboBox startCombo;
    public ComboBox endCombo;
    public ComboBox userIDCombo;
    public DatePicker dateTextBox;
    public Button selectButton;
    public ComboBox contactComboBox;
    public Button saveButton;

    ObservableList<Customer> customerList = FXCollections.observableArrayList();


    /**
     * sets up tables and combo boxes
     * @throws SQLException
     */
    public void initialize() throws SQLException {

        customerTable.setItems(customerList);

        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

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

    /**
     * Cancels the add appointment and returns user to main screen
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * selects a customer from the table, or displays an error if no customer is selected
     * @param actionEvent
     */
    public void onSelectClick(ActionEvent actionEvent) {
        try {
            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            String customerID = Integer.toString(customer.getCustomerID());
            CustomerIDTextBox.setText(customerID);
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Selection Error");
            alert.setContentText("Please select a customer.");
            alert.showAndWait();
        }
    }

    /**
     * Contains first lambda method.  The method converts time to UTC so that it may be stored to
     * the database correctly. Saves the appointment to the database, or throws an error if
     * any info is missing, or if the appointment is out of business hours, or if the appointment overlaps
     * with other appointments.
     * @param actionEvent
     */
    public void onSaveClick(ActionEvent actionEvent) {

        /**
         * lambda expression.  takes a localdatetime and converts it to utc
         */
        utconversion convertingutc = (LocalDateTime lodati) -> {
            ZoneId myZone = ZoneId.systemDefault();
            ZonedDateTime givenTime = ZonedDateTime.of(lodati, myZone);
            ZoneId zID = ZoneId.of("UTC");
            ZonedDateTime zDT = ZonedDateTime.ofInstant(givenTime.toInstant(), zID);
            return zDT.toLocalDateTime();
        };


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

            LocalDate startEndDate = dateTextBox.getValue();
            LocalTime startTime = (LocalTime) startCombo.getValue();
            LocalTime endTime = (LocalTime) endCombo.getValue();


            LocalDateTime ldtStart = LocalDateTime.of(startEndDate,startTime);
            LocalDateTime ldtEnd = LocalDateTime.of(startEndDate,endTime);


            LocalDateTime ldtStartUTC = convertingutc.conversion(ldtStart);
            LocalDateTime ldtEndUTC = convertingutc.conversion(ldtEnd);


            String startDTString = ldtStartUTC.toString();
            String endDTString = ldtEndUTC.toString();

            boolean startWithin = Appointment.withinBusinessHours(ldtStart);
            boolean endWithin = Appointment.withinBusinessHours(ldtEnd);

            if (!startWithin || !endWithin){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Business Hours");
                alert.setContentText("Must be within business hours. (Monday-Friday 8:00AM-10:00PM EST)");
                alert.showAndWait();
                return;
            }

            boolean overlap = AppointmentQuery.overlappingAppointments(ldtStartUTC, ldtEndUTC);

            if (overlap){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Overlap");
                alert.setContentText("An appointment already exists during this time.  Please choose a different time.");
                alert.showAndWait();
                return;
            }

            if (!Appointment.startBeforeEnd(ldtStartUTC,ldtEndUTC)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("End before start");
                alert.setContentText("The end time is equal to or before the start time.");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to Save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                AppointmentQuery.insertAppointment(title, description, location, type, startDTString, endDTString, customerID, userID, contactID);

                Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Attributes");
            alert.setContentText("Please make sure all fields are filled");
            alert.showAndWait();
        }
    }
}
