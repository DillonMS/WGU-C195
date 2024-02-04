package smith.c195v2.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import smith.c195v2.Appointment;
import smith.c195v2.TimeConversions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * Contains all methods containing sql queries involving the appointments table
 */
public abstract class AppointmentQuery {

    /**
     * Inserts appointment into the database.
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contactID
     * @throws SQLException
     */
    public static void insertAppointment(String title, String description, String location, String type, String start, String end, int customerID, int userID, int contactID) throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,title);
        ps.setString(2, description);
        ps.setString(3,location);
        ps.setString(4,type);
        ps.setString(5,start);
        ps.setString(6,end);
        ps.setInt(7,customerID);
        ps.setInt(8,userID);
        ps.setInt(9,contactID);
        ps.executeUpdate();
    }

    /**
     * Updates existing appointment in the database. Does not change ID.
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contactID
     * @param appointmentID
     * @throws SQLException
     */
    public static void modifyAppointment(String title, String description, String location, String type, String start, String end, int customerID, int userID, int contactID, int appointmentID) throws SQLException {
        String sql = "Update appointments SET Title = ?, Description = ?, Location = ?, type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID =? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,title);
        ps.setString(2, description);
        ps.setString(3,location);
        ps.setString(4,type);
        ps.setString(5,start);
        ps.setString(6,end);
        ps.setInt(7,customerID);
        ps.setInt(8,userID);
        ps.setInt(9,contactID);
        ps.setInt(10,appointmentID);
        ps.executeUpdate();
    }

    /**
     * Removes appointment from the database.
     * @param appointmentID selects which appointment to remove.
     * @throws SQLException
     */
    public static void removeAppointment(int appointmentID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = " + appointmentID +";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * Gets the contact name based on its ID.
     * @param contactID used to get String
     * @return returns Name
     * @throws SQLException
     */
    public static String getContact(int contactID) throws SQLException {
        String sql = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("Contact_Name");
        } else
            return "";
    }

    /**
     * returns All appointments for the main screen all view.
     * @return Observable list of all apointments.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

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
            LocalDateTime convertedTimeStart = TimeConversions.convertToUserTimeZone(dateTime);
            sqlAppointment.setStart(convertedTimeStart);

            String stringStart2 = rs.getString("End");
            LocalDateTime dateTime2 = LocalDateTime.parse(stringStart2, formatter);
            LocalDateTime convertedTimeEnd = TimeConversions.convertToUserTimeZone(dateTime2);
            sqlAppointment.setEnd(convertedTimeEnd);

            sqlAppointment.setCustomerID(rs.getInt("Customer_ID"));
            sqlAppointment.setUserID(rs.getInt("User_ID"));

            appointmentList.add(sqlAppointment);
        }
        return appointmentList;
    }

    /**
     * Returns only the appointments for the month specified.
     * @param date Used for year and month.
     * @return All appointments for the month.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getMonthAppointments(LocalDate date) throws SQLException {
        ObservableList<Appointment> monthList = FXCollections.observableArrayList();

        String dateString = date.toString();
        String year = dateString.substring(0, 4);
        String month = dateString.substring(5, 7);

        String sql = "SELECT * FROM appointments WHERE MONTH(Start) = ? AND YEAR(Start) = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, month);
        ps.setString(2, year);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

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

            monthList.add(sqlAppointment);
        }
        return monthList;
    }

    /**
     * Returns all appointments based on the week given
     * @param date Used to extract which week in the year it is.
     * @return All appointments within the week.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getWeekAppointments(LocalDate date) throws SQLException {
        ObservableList<Appointment> weekList = FXCollections.observableArrayList();

        TemporalField week = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = date.get(week);
        String weekString = Integer.toString(weekNumber);

        String dateString = date.toString();
        String year = dateString.substring(0, 4);


        String sql = "SELECT * FROM appointments WHERE WEEK(DATE(start))+1 = ? AND YEAR(Start) = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, weekString);
        ps.setString(2, year);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

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

            weekList.add(sqlAppointment);
        }
        return weekList;
    }

    /**
     * Returns the closest appointment to the current date and time. This will be used to determine
     * if an appointment is starting in 15 minutes or less.
     * @return Single appointment
     * @throws SQLException
     */
    public static Appointment returnClosestAppointment() throws SQLException {
        LocalDateTime lDT = LocalDateTime.now();
        LocalDateTime lDTUTC = TimeConversions.convertToUTC(lDT);
        String sql = "SELECT * FROM appointments WHERE Start > ?  LIMIT 1";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(lDTUTC));
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {

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

            return sqlAppointment;
        }
        return null;
    }

    /**
     * Get the user ID based on the name provided
     * @param userName name provided.
     * @return user ID.
     * @throws SQLException
     */
    public static int getUserID(String userName) throws SQLException {
        String sql = "SELECT User_ID FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt("User_ID");
        else
            return -1;
    }

    /**
     * Get the username based on the ID provided
     * @param userID used to get name
     * @return String username
     * @throws SQLException
     */
    public static String getUserName(int userID) throws SQLException {
        String sql = "SELECT User_Name FROM users WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getString("User_Name");
        else
            return "";
    }

    /**
     * Get the Contact ID based on the name
     * @param contactName Name provided.
     * @return ID.
     * @throws SQLException
     */
    public static int getContactID(String contactName) throws SQLException {
        String sql = "SELECT Contact_ID FROM contacts WHERE contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt("Contact_ID");
        else
            return -1;
    }

    /**
     * Checks if a program already exists during the current time.
     * @param start start of appointment in question
     * @param end end of appointment in question
     * @return true or false if there is an overlapping appointment
     * @throws SQLException
     */
    public static Boolean overlappingAppointments(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE Start BETWEEN ? AND ? OR End Between ? AND ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(start));
        ps.setString(2, String.valueOf(end));
        ps.setString(3, String.valueOf(start));
        ps.setString(4,String.valueOf(end));
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * Checks if a program already exists during the current time.
     * @param start start of appointment in question
     * @param end end of appointment in question
     * @param id used to exclude the current appointment when modifying
     * @return true or false if there is an overlapping appointment
     * @throws SQLException
     */
    public static Boolean overlappingAppointments(LocalDateTime start, LocalDateTime end, int id) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE Start BETWEEN ? AND ? AND Appointment_ID != ? OR End Between ? AND ? AND Appointment_ID != ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(start));
        ps.setString(2, String.valueOf(end));
        ps.setInt(3,id);
        ps.setString(4, String.valueOf(start));
        ps.setString(5,String.valueOf(end));
        ps.setInt(6,id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
