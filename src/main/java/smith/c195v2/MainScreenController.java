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
import smith.c195v2.helper.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){

            Appointment sqlAppointment = new Appointment();

            sqlAppointment.setAppointmentID(rs.getInt("Appointment_ID"));
            sqlAppointment.setTitle(rs.getString("Title"));
            sqlAppointment.setDescription(rs.getString("Description"));
            sqlAppointment.setLocation(rs.getString("Location"));

            sqlAppointment.setContactID(rs.getInt("Contact_ID"));
            int cID = sqlAppointment.getContactID();
            sqlAppointment.setContact(AppointmentQuery.getContact(cID));

            sqlAppointment.setType(rs.getString("Type"));

            String stringStart = rs.getString("Start");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(stringStart, formatter);
            sqlAppointment.setStart(dateTime);

            String stringStart2 = rs.getString("Start");
            LocalDateTime dateTime2 = LocalDateTime.parse(stringStart2, formatter);
            sqlAppointment.setEnd(dateTime2);

            sqlAppointment.setCustomerID(rs.getInt("Customer_ID"));
            sqlAppointment.setUserID(rs.getInt("User_ID"));

            appointmentList.add(sqlAppointment);
        }
    }


    public void onCustomersClick(ActionEvent actionEvent) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }
}

