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
import smith.c195v2.helper.JDBC;
import smith.c195v2.helper.SpaceAdder;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * methods for the reports screen
 */
public class ReportsController {
    public Button contactAppointmentsButton;
    public Button monthTotalButton;
    public Button customReportButton;
    public Button returnButton;
    public TextArea textAreaBox;

    /**
     * Contains 2nd lambda, which adds spaces to words to even out the columns in the
     * text field.  Displays a schedule for all contacts in the database
     * @param actionEvent
     * @throws SQLException
     */
    public void onContactClick(ActionEvent actionEvent) throws SQLException {

        // 2nd lambda
        SpaceAdder addingSpace = (StringBuilder word, int number) -> {
            while (word.length() < 20)
                word.append(" ");
            return word;
        };

        textAreaBox.clear();
        textAreaBox.appendText("Schedule for each contact in your organization that includes appointment ID, title, type and\n" +
                "description, start date and time, end date and time, and customer ID\n");
        textAreaBox.appendText("Contact\t\t\tAppointment ID\t\t\tStart\t\t\t\t\t\tEnd\t\tCustomer ID\tTitle\t\t\tType\t\t\tDescription\n");
        String sql = "SELECT con.Contact_Name, app.Appointment_ID, app.Title, app.Type, app.Description, app.Start, app.End, app.Customer_ID FROM client_schedule.contacts con INNER JOIN client_schedule.appointments app ON app.Contact_ID = con.Contact_ID ORDER BY Contact_Name, Start";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            StringBuilder name = new StringBuilder(rs.getString("Contact_Name"));
            name = addingSpace.wordWithSpaces(name, 20);
            textAreaBox.appendText(name + "\t\t");

            textAreaBox.appendText(rs.getString("Appointment_ID") +"\t\t\t");
            String start = rs.getString("Start");
            LocalDateTime ldtStart = LocalDateTime.parse(start,dtf);
            ldtStart = TimeConversions.convertToUserTimeZone(ldtStart);
            start = ldtStart.toString();
            textAreaBox.appendText(start +"\t\t\t\t");

            String end = rs.getString("End");
            LocalDateTime ldtEnd = LocalDateTime.parse(end, dtf);
            ldtEnd = TimeConversions.convertToUserTimeZone(ldtEnd);
            end = ldtEnd.toString();
            textAreaBox.appendText(end +"\t");
            textAreaBox.appendText(rs.getString("Customer_ID")+"\t"+"\t");

            StringBuilder title = new StringBuilder(rs.getString("Title"));
            title = addingSpace.wordWithSpaces(title,20);
            textAreaBox.appendText(title +"\t");

            StringBuilder type = new StringBuilder(rs.getString("Type"));
            type = addingSpace.wordWithSpaces(type,20);
            textAreaBox.appendText(type +"\t\t\t");

            StringBuilder description = new StringBuilder(rs.getString("Description"));
            description = addingSpace.wordWithSpaces(description,20);
            textAreaBox.appendText(description +"");
            textAreaBox.appendText("\n");
        }
    }

    /**
     * Shows month totals based on type
     * @param actionEvent
     * @throws SQLException
     */
    public void onMonthTotalClick(ActionEvent actionEvent) throws SQLException {
        textAreaBox.clear();
        textAreaBox.appendText("Total number of customer appointments by type and month\n");
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

    /**
     * custom report that shows appointments grouped by customer.
     * @param actionEvent
     * @throws SQLException
     */
    public void onCustomClick(ActionEvent actionEvent) throws SQLException {
        textAreaBox.clear();
        textAreaBox.appendText("Shows appointments grouped by customer\n");
        textAreaBox.appendText("Customer ID\tStart\t\t\t\t\t\t\tEnd\n");
        String sql = "SELECT Customer_ID, Start, End FROM client_schedule.appointments app ORDER BY Customer_ID, Start";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            textAreaBox.appendText(rs.getString("Customer_ID") + "\t\t\t");

            String start = rs.getString("Start");
            LocalDateTime ldtStart = LocalDateTime.parse(start,dtf);
            ldtStart = TimeConversions.convertToUserTimeZone(ldtStart);
            start = ldtStart.toString();
            textAreaBox.appendText(start + "\t\t\t");


            String end = rs.getString("End");
            LocalDateTime ldtEnd = LocalDateTime.parse(end, dtf);
            ldtEnd = TimeConversions.convertToUserTimeZone(ldtEnd);
            end = ldtEnd.toString();
            textAreaBox.appendText(end  + "\n");
        }
    }

    /**
     * returns user to the main screen.
     * @param actionEvent
     * @throws IOException
     */
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
