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

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

public class ModifyAppointmentController {

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

    public void onSaveClick(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            String apID = iDTextBox.getText();
            int aID = Integer.parseInt(apID);
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
            LocalDateTime ldtStartUTC = TimeConversions.convertToUTC(ldtStart);
            LocalDateTime ldtEndUTC = TimeConversions.convertToUTC(ldtEnd);


            String startDTString = ldtStartUTC.toString();
            String endDTString = ldtEndUTC.toString();

            boolean startWithin = Appointment.withinBusinessHours(ldtStart);
            boolean endWithin = Appointment.withinBusinessHours(ldtEnd);

            if (!startWithin || !endWithin){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Business Hours");
                alert.setContentText("Must be within business hours. (8:00AM-10:00PM EST)");
                alert.showAndWait();
                return;
            }

            boolean overlap = AppointmentQuery.overlappingAppointments(ldtStartUTC, ldtStartUTC);

            if (overlap){
                System.out.println("overlap");
                return;
            }



            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to Save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                AppointmentQuery.modifyAppointment(title, description, location, type, startDTString, endDTString, customerID, userID, contactID, aID);

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

   public void passAppointmentCustomer(Appointment selectedAppointment) throws SQLException {
        int appointmentID = selectedAppointment.getAppointmentID();
        String title = selectedAppointment.getTitle();
        String location = selectedAppointment.getLocation();
        String description = selectedAppointment.getDescription();
        String type = selectedAppointment.getType();
        int customerID = selectedAppointment.getCustomerID();

        LocalDateTime startDate = selectedAppointment.getStart();
        String date = startDate.toString();
        String startTime = date.substring(11);
        LocalTime stTime =LocalTime.parse(startTime);

        date = date.substring(0,10);
        LocalDate localDate = startDate.toLocalDate();

        LocalDateTime endDate = selectedAppointment.getEnd();
        String endString = endDate.toString();
        String endTime = endString.substring(11);
        LocalTime enTime = LocalTime.parse(endTime);

        int userID = selectedAppointment.getUserID();
        String userName = AppointmentQuery.getUserName(userID);
        userIDCombo.setValue(userName);
        int contactID = selectedAppointment.getContactID();
        String contactName = AppointmentQuery.getContact(contactID);
        contactComboBox.setValue(contactName);

        iDTextBox.setText(Integer.toString(appointmentID));
        titleTextBox.setText(title);
        locationTextBox.setText(location);
        descriptionTextBox.setText(description);
        typeTextBox.setText(type);
        CustomerIDTextBox.setText(Integer.toString(customerID));
        dateTextBox.setValue(localDate);
        startCombo.setValue(stTime);
        endCombo.setValue(enTime);





   }

}
