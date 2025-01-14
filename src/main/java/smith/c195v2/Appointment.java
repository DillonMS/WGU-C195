package smith.c195v2;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * creates object appointment and contains related methods
 */
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String contact;

    private int contactID;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int userID;
    private int customerID;

    public Appointment() {
    }



    public Appointment(int appointmentID, String title, String description, String location, String contact, String type, LocalDateTime start, LocalDateTime end, int userID, int customerID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.userID = userID;
        this.customerID = userID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * checks whether time and date is within business hours, defined as weekdays 8:00 AM - 10:00 PM ET
     * @param time
     * @return true or false
     */
    public static boolean withinBusinessHours(LocalDateTime time) {
        LocalDateTime convertedLDT = TimeConversions.convertToEST(time);
        LocalTime lt = convertedLDT.toLocalTime();

        DayOfWeek day = convertedLDT.getDayOfWeek();

        LocalTime businessStart = LocalTime.of(8, 0,0);
        LocalTime businessEnd = LocalTime.of(22,0,0);

        return !lt.isBefore(businessStart) && !lt.isAfter(businessEnd) && day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    public static boolean startBeforeEnd (LocalDateTime start, LocalDateTime end){
        if (start.isAfter(end) || start.equals(end)){
            return false;
        }
        else return true;
    }
}