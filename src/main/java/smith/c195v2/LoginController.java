package smith.c195v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static smith.c195v2.helper.LoginQuery.checkLogin;

public class LoginController {

    @FXML private PasswordField passwordTextBox;
    @FXML private TextField userNameTextBox;
    @FXML private Label loginScreenText;
    @FXML private Label userNameText;
    @FXML private Label passwordText;
    @FXML private Button loginText;
    @FXML private Button clearText;

    @FXML
    private void initialize() {

        if (Locale.getDefault().getLanguage().equals("fr")){

            ResourceBundle rb = ResourceBundle.getBundle("Nat_fr", Locale.getDefault());
            passwordText.setText(rb.getString("Password"));
            userNameText.setText(rb.getString("UserName"));
            loginScreenText.setText(rb.getString("LoginScreen"));
            loginText.setText(rb.getString("Login"));
            clearText.setText(rb.getString("Clear"));
        }

    }

    public void onLoginClick(ActionEvent actionEvent) throws SQLException, IOException {
        String userName = userNameTextBox.getText();
        String password = passwordTextBox.getText();
        boolean correctUser = checkLogin(userName, password);
        if (correctUser){
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username/Password");
            alert.setContentText("Please enter a correct username and password.");
            alert.showAndWait();
            userNameTextBox.setText("");
            passwordTextBox.setText("");

        }
    }

    public void onClearClick(ActionEvent actionEvent) {
        userNameTextBox.setText("");
        passwordTextBox.setText("");
    }
    public void onuserNameClick(ActionEvent actionEvent) throws SQLException, IOException {
        String userName = userNameTextBox.getText();
        String password = passwordTextBox.getText();
        boolean correctUser = checkLogin(userName, password);
        if (correctUser){
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username/Password");
            alert.setContentText("Please enter a correct username and password.");
            alert.showAndWait();
            userNameTextBox.setText("");
            passwordTextBox.setText("");

        }
    }

    public void onPasswordClick(ActionEvent actionEvent) throws SQLException, IOException {
        String userName = userNameTextBox.getText();
        String password = passwordTextBox.getText();
        boolean correctUser = checkLogin(userName, password);
        if (correctUser){
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username/Password");
            alert.setContentText("Please enter a correct username and password.");
            alert.showAndWait();
            userNameTextBox.setText("");
            passwordTextBox.setText("");

        }
    }

}
