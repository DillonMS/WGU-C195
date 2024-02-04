package smith.c195v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static smith.c195v2.helper.LoginQuery.checkLogin;

/**
 * methods dealing with login screen
 */
public class LoginController {

    public Text zoneIDText;
    public Label localeText;
    @FXML private PasswordField passwordTextBox;
    @FXML private TextField userNameTextBox;
    @FXML private Label loginScreenText;
    @FXML private Label userNameText;
    @FXML private Label passwordText;
    @FXML private Button loginText;
    @FXML private Button clearText;

    String alertTitle;
    String alertContent;
    String alertHeader;

    /**
     * displays text in French if default language is French. Displays English otherwise.
     */
    @FXML
    private void initialize() {
        
        ZoneId zID = ZoneId.systemDefault();
        zoneIDText.setText("Zone ID: " + zID);
        Locale localeID = Locale.getDefault();
        localeText.setText("Locale: " + localeID);

        if (Locale.getDefault().getLanguage().equals("fr")){
            ResourceBundle rb = ResourceBundle.getBundle("Nat_fr", Locale.getDefault());
            passwordText.setText(rb.getString("Password"));
            userNameText.setText(rb.getString("UserName"));
            loginScreenText.setText(rb.getString("LoginScreen"));
            loginText.setText(rb.getString("Login"));
            clearText.setText(rb.getString("Clear"));
            alertTitle = rb.getString("Title");
            alertContent = rb.getString("Content");
            alertHeader = rb.getString("Header");
            localeText.setText(rb.getString("Locale") + localeID);
            zoneIDText.setText(rb.getString("Zone") + zID);
        }
        else{
            ResourceBundle rb = ResourceBundle.getBundle("Nat_en", Locale.getDefault());
            passwordText.setText(rb.getString("Password"));
            userNameText.setText(rb.getString("UserName"));
            loginScreenText.setText(rb.getString("LoginScreen"));
            loginText.setText(rb.getString("Login"));
            clearText.setText(rb.getString("Clear"));
            alertTitle = rb.getString("Title");
            alertContent = rb.getString("Content");
            alertHeader = rb.getString("Header");
            localeText.setText(rb.getString("Locale") + localeID);
            zoneIDText.setText(rb.getString("Zone") + zID);
        }

    }

    /**
     * Checks if username and password is correct, then shows main screen.  Throws
     * error if incorrect
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onLoginClick(ActionEvent actionEvent) throws SQLException, IOException {

        String userName = userNameTextBox.getText();
        String password = passwordTextBox.getText();
        boolean correctUser = checkLogin(userName, password);
        writeToText(correctUser);
        if (correctUser){
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
        else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(alertTitle);
                alert.setContentText(alertContent);
                alert.setHeaderText(alertHeader);
                alert.showAndWait();
                userNameTextBox.setText("");
                passwordTextBox.setText("");
        }
    }

    /**
     * clears text box
     * @param actionEvent
     */
    public void onClearClick(ActionEvent actionEvent) {
        userNameTextBox.setText("");
        passwordTextBox.setText("");
    }

    /**
     * Checks if username and password is correct, then shows main screen.  Throws
     * error if incorrect
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onuserNameClick(ActionEvent actionEvent) throws SQLException, IOException {
        String userName = userNameTextBox.getText();
        String password = passwordTextBox.getText();
        boolean correctUser = checkLogin(userName, password);
        writeToText(correctUser);
        if (correctUser){
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertTitle);
            alert.setContentText(alertContent);
            alert.setHeaderText(alertHeader);
            alert.showAndWait();
            userNameTextBox.setText("");
            passwordTextBox.setText("");
        }
    }

    /**
     * Checks if username and password is correct, then shows main screen.  Throws
     * error if incorrect
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onPasswordClick(ActionEvent actionEvent) throws SQLException, IOException {
        String userName = userNameTextBox.getText();
        String password = passwordTextBox.getText();
        boolean correctUser = checkLogin(userName, password);
        writeToText(correctUser);
        if (correctUser){
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertTitle);
            alert.setContentText(alertContent);
            alert.setHeaderText(alertHeader);
            alert.showAndWait();
            userNameTextBox.setText("");
            passwordTextBox.setText("");
        }
    }

    /**
     * Writes login attempts to text file
     * @param pass determines whether login was successful or not
     * @throws IOException
     */
    public void writeToText(boolean pass) throws IOException {
        FileWriter myWriter = new FileWriter("login_activity.txt", true);
        LocalDateTime lDT = LocalDateTime.now(ZoneOffset.UTC);
        lDT = lDT.truncatedTo(ChronoUnit.SECONDS);
        myWriter.append(lDT.toLocalDate().toString()).append("\t");
        myWriter.append(lDT.toLocalTime().toString()).append("\t");
        if (pass)
            myWriter.append("Success");
        else
            myWriter.append("Fail");
        myWriter.append("\n");
        myWriter.close();
    }

}
