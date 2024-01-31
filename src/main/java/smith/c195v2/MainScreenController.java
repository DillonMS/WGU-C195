package smith.c195v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController {
    public Button logoutButton;
    public TableColumn tableID;
    public TableColumn tableTitle;
    public TableColumn tableDescription;
    public TableColumn tableLocation;
    public TableColumn tableContact;
    public TableColumn tableType;
    public TableColumn tableStart;
    public TableColumn tableEnd;
    public TableColumn tableCustomerID;
    public TableColumn tableUserID;
    public RadioButton weekViewRadio;
    public RadioButton monthViewRadio;
    public Button customersButton;
    public DatePicker datePicker;
    public RadioButton allViewRadio;
    public TableView appointmentTable;
    public Label localTimeLabel;
    public Button addButton;
    public Button deleteButton;
    public Button modifyButton;

    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }


    public void initialize() throws SQLException {
        ToggleGroup group = new ToggleGroup();
        weekViewRadio.setToggleGroup(group);
        monthViewRadio.setToggleGroup(group);
        allViewRadio.setToggleGroup(group);
        allViewRadio.fire();
        appointmentList.clear();
        datePicker.setValue(LocalDate.now());

        LocalDateTime cTime = LocalDateTime.now();
        fifteenMinuteWarning(cTime);

        String timeZone = System.getProperty("user.timezone");

        localTimeLabel.setText("Time Zone: " + timeZone);

        appointmentTable.setItems(appointmentList);

        tableID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        tableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tableContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        tableEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        tableCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tableUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));

        appointmentList.addAll(AppointmentQuery.getAllAppointments());
    }

    public void fifteenMinuteWarning(LocalDateTime cTime) throws SQLException {
        Appointment appointment = AppointmentQuery.returnClosestAppointment();

        if (appointment != null){
            LocalDateTime appTime = appointment.getStart();
            LocalDateTime plusFifteen = cTime.plusMinutes(15);
            if (appTime.isAfter(cTime) && appTime.isBefore(plusFifteen)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment");
                alert.setContentText("There is an appointment starting soon!");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment");
                alert.setContentText("There are no appointments starting soon.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment");
            alert.setContentText("There are no appointments starting soon");
            alert.showAndWait();
        }
    }


    public void onCustomersClick(ActionEvent actionEvent) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }

    public void onPickDateClick(ActionEvent actionEvent) throws SQLException {

        if (allViewRadio.isSelected()){
            appointmentList.clear();
            appointmentList.addAll(AppointmentQuery.getAllAppointments());
        }
        else if (monthViewRadio.isSelected()){
            appointmentList.clear();
            appointmentList.addAll(AppointmentQuery.getMonthAppointments(datePicker.getValue()));
        }
        else if (weekViewRadio.isSelected()){
            appointmentList.clear();
            appointmentList.addAll(AppointmentQuery.getWeekAppointments(datePicker.getValue()));
        }
    }


    public void onWeekClick(ActionEvent actionEvent) throws SQLException {
        appointmentList.clear();
        appointmentList.addAll(AppointmentQuery.getWeekAppointments(datePicker.getValue()));
    }

    public void onMonthClick(ActionEvent actionEvent) throws SQLException {
        appointmentList.clear();
        appointmentList.addAll(AppointmentQuery.getMonthAppointments(datePicker.getValue()));
    }

    public void onAllClick(ActionEvent actionEvent) throws SQLException {
        appointmentList.clear();
        appointmentList.addAll(AppointmentQuery.getAllAppointments());
    }

    public void onAddClick(ActionEvent actionEvent) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addAppointment-view.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }

    public void onDeleteClick(ActionEvent actionEvent) throws SQLException, IOException {
        Appointment selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        int iD = selectedAppointment.getAppointmentID();
        String type = selectedAppointment.getType();

        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to delete this appointment?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int cID = selectedAppointment.getAppointmentID();
            AppointmentQuery.removeAppointment(cID);
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Information");
            alert2.setHeaderText("Appointment Deleted");
            alert2.setContentText("The appointment with ID: " + iD + " and type: " + type + " has been deleted");
            alert2.showAndWait();
        }
    }

    public void onModifyClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyAppointment-view.fxml"));
            Parent root = loader.load();

            ModifyAppointmentController mpController = loader.getController();
            Appointment selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
            mpController.passAppointmentCustomer(selectedAppointment);

            Scene mainScreenScene = new Scene(root);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
        catch (Exception e){
            System.out.println("error");
        }
    }
}

