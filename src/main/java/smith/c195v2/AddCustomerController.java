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
 * methods for the add customer controller.
 */
public class AddCustomerController {


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
     * cancels adding a customer and returns to the main screen.
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
     * sets the combobox for use
     */
    public void initialize(){

        countryCombo.getItems().addAll( "U.S.", "UK", "Canada");


    }

    /**
     * sets the state combo box depending on which country was selected.
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
     * saves the customer to the database, or throws an error if a field is blank.
     * @param actionEvent
     * @throws SQLException
     */
    public void onSaveClick(ActionEvent actionEvent) throws SQLException {
        try {
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

                CustomerQuery.insertCustomer(name, address, zip, phone, dID);

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

}
