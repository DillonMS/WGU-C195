package smith.c195v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smith.c195v2.helper.CustomerQuery;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class AddCustomerController {


    public Button cancelButton;
    public ComboBox countryCombo;
    public ComboBox stateCombo;

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

    public void initialize(){

        countryCombo.getItems().addAll("", "U.S.", "UK", "Canada");
    }

}
