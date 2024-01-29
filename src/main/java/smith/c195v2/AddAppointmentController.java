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

        //startCombo.getItems().addAll(timeList);
        //endCombo.getItems().addAll((timeList));
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
}
