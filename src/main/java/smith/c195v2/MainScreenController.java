package smith.c195v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public Button logoutButton;
    public TableColumn tableID;
    public TableColumn tableTitle;
    public TableColumn tableDescription;
    public TableColumn TableLocation;
    public TableColumn tableContact;
    public TableColumn tableType;
    public TableColumn tableStart;
    public TableColumn tableEnd;
    public TableColumn tableCustomerID;
    public TableColumn tableUserID;
    public RadioButton weekViewRadio;
    public RadioButton monthViewRadio;
    public Button customersButton;

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup group = new ToggleGroup();
        weekViewRadio.setToggleGroup(group);
        monthViewRadio.setToggleGroup(group);
        weekViewRadio.fire();





    }


    public void onCustomersClick(ActionEvent actionEvent) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }
}

