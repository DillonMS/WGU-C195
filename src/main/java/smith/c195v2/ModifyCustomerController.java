package smith.c195v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import smith.c195v2.helper.CustomerQuery;
import smith.c195v2.helper.FLDQuery;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * methods for modify customer screen
 */
public class ModifyCustomerController {
    public Button cancelButton;
    public ComboBox countryCombo;
    public ComboBox stateCombo;
    public Button saveButton;
    public TextField nameTextBox;
    public TextField addressTextBox;
    public TextField zipTextBox;
    public TextField phoneTextBox;
    public TextField iDTextBox;

    /**
     * cancels the modify and returns to customer page
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

            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }

    /**
     * initializes the country combo box
     */
    public void initialize(){

        countryCombo.getItems().addAll( "U.S.", "UK", "Canada");


    }

    /**
     * fills state combo box based on which country was selected.
     * @param actionEvent
     * @throws SQLException
     */
    public void onCountryClick(ActionEvent actionEvent) throws SQLException {

        if (countryCombo.getSelectionModel().getSelectedIndex() == 0) {
            stateCombo.getItems().clear();
            stateCombo.getItems().addAll(FLDQuery.getStateList(1));

        }
        else if (countryCombo.getSelectionModel().getSelectedIndex() == 1) {
            stateCombo.getItems().clear();
            stateCombo.getItems().addAll(FLDQuery.getStateList(2));

        }
        else if (countryCombo.getSelectionModel().getSelectedIndex() == 2) {
            stateCombo.getItems().clear();
            stateCombo.getItems().addAll(FLDQuery.getStateList(3));
        }

    }

    /**
     * updates selected customer to the database, or throws error if not all fields are filled
     * @param actionEvent
     * @throws SQLException
     */
    public void onSaveClick(ActionEvent actionEvent) throws SQLException {
        try {
            String iDString = iDTextBox.getText();
            int ID = Integer.parseInt(iDString);
            String name = nameTextBox.getText();
            String address = addressTextBox.getText();
            String zip = zipTextBox.getText();
            String phone = phoneTextBox.getText();
            String division = (String) stateCombo.getSelectionModel().getSelectedItem();
            int dID = CustomerQuery.getDivisionID(division);

            if (name.isEmpty() || address.isEmpty() || zip.isEmpty() || phone.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Attributes");
                alert.setContentText("Please make sure all text fields are filled");
                alert.showAndWait();
                return;
            }

            if (dID == -1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Attributes");
                alert.setContentText("Please select a country and state");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to Save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                CustomerQuery.updateCustomer(ID, name, address, zip, phone, dID);

                Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            }
        }
        catch(Exception e){
            System.out.println("error");
        }
    }

    /**
     * method used to insert customer data into the text boxes
     * @param customer
     * @throws SQLException
     */
    public void passCustomer(Customer customer) throws SQLException {

        String id = Integer.toString(customer.getCustomerID());
        String name = customer.getCustomerName();
        String address = customer.getAddress();
        String postal = customer.getPostalCode();
        String phone = customer.getPhone();
        int dID = customer.getDivisionID();
        String country = CustomerQuery.getCountry(dID);
        String fld = CustomerQuery.getFirstLevelDivision(dID);

        iDTextBox.setText(id);
        nameTextBox.setText(name);
        addressTextBox.setText(address);
        zipTextBox.setText(postal);
        phoneTextBox.setText(phone);
        countryCombo.setValue(country);
        stateCombo.setValue(fld);

        if (country.equals("U.S")){
            stateCombo.getItems().addAll(FLDQuery.getStateList(1));
        }
        else if (country.equals("UK")){
            stateCombo.getItems().addAll(FLDQuery.getStateList(2));
        }
        else {
            stateCombo.getItems().addAll(FLDQuery.getStateList(3));
        }
    }

}
