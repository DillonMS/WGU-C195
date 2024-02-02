package smith.c195v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import smith.c195v2.helper.AppointmentQuery;
import smith.c195v2.helper.JDBC;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class ReportsController {
    public Button contactAppointmentsButton;
    public Button monthTotalButton;
    public Button customReportButton;
    public Button returnButton;
    public TextArea textAreaBox;

    public void onContactClick(ActionEvent actionEvent) throws SQLException {
        textAreaBox.clear();
        textAreaBox.appendText("Contact\t\t\tAppointment ID\t\t\tStart\t\t\t\t\t\tEnd\t\tCustomer ID\tTitle\t\t\tType\t\t\tDescription\n");
        String sql = "SELECT con.Contact_Name, app.Appointment_ID, app.Title, app.Type, app.Description, app.Start, app.End, app.Customer_ID FROM client_schedule.contacts con INNER JOIN client_schedule.appointments app ON app.Contact_ID = con.Contact_ID ORDER BY Contact_Name, Start";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            StringBuilder name = new StringBuilder(rs.getString("Contact_Name"));
            while (name.length() < 20)
                name.append(" ");
            textAreaBox.appendText(name + "\t\t");

            textAreaBox.appendText(rs.getString("Appointment_ID") +"\t\t\t");
            textAreaBox.appendText(rs.getString("Start")+"\t\t");
            textAreaBox.appendText(rs.getString("End")+"\t");
            textAreaBox.appendText(rs.getString("Customer_ID")+"\t"+"\t");

            StringBuilder title = new StringBuilder(rs.getString("Title"));
            while (title.length() < 20)
                title.append(" ");
            textAreaBox.appendText(title +"\t");

            StringBuilder type = new StringBuilder(rs.getString("Type"));
            while (title.length() < 20)
                title.append(" ");
            textAreaBox.appendText(type +"\t\t\t");

            StringBuilder description = new StringBuilder(rs.getString("Description"));
            while (title.length() < 20)
                title.append(" ");
            textAreaBox.appendText(description +"");


            textAreaBox.appendText("\n");
        }



    }

    public void onMonthTotalClick(ActionEvent actionEvent) throws SQLException {
        textAreaBox.clear();
        textAreaBox.appendText("Year\t\t\tMonth\t\t\tType\t\t\t\t\tNumber\n");
        String sql = "SELECT YEAR(Start) as Year, MONTHNAME(Start) as Month, type, COUNT(*) as Number FROM client_schedule.appointments group by YEAR, Month, Type;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            textAreaBox.appendText(rs.getString("Year") + "\t\t\t");
            StringBuilder month = new StringBuilder(rs.getString("Month"));
            while (month.length() < 9)
                month.append(" ");
            textAreaBox.appendText(month + "\t\t\t");
            StringBuilder type = new StringBuilder(rs.getString("Type"));
            while (type.length() < 20)
                type.append(" ");
            textAreaBox.appendText(type + "\t\t\t\t");
            textAreaBox.appendText(rs.getString("Number"));
            textAreaBox.appendText("\n");
        }
    }

    public void onCustomClick(ActionEvent actionEvent) {
    }

    public void onReturnClick(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to return to main menu?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainscreen-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }
}
