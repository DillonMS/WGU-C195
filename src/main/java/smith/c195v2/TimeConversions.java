package smith.c195v2;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * methods for dealing with converting time zones
 */
public class TimeConversions {

    /**
     * converts time to eastern time to check for business hours
     * @param ldt localdatetime passed in
     * @return localdatetime that is in eastern time
     */
    public static LocalDateTime convertToEST(LocalDateTime ldt){
        ZoneId myZone = ZoneId.systemDefault();
        ZonedDateTime givenTime = ZonedDateTime.of(ldt, myZone);
        ZoneId zID = ZoneId.of("America/New_York");
        ZonedDateTime zDT = ZonedDateTime.ofInstant(givenTime.toInstant(), zID);
        return zDT.toLocalDateTime();
    }

    /**
     * converts current time into UTC to be stored into the database
     * @param ldt current time
     * @return current time in UTC
     */
    public static LocalDateTime convertToUTC(LocalDateTime ldt){
        ZoneId myZone = ZoneId.systemDefault();
        ZonedDateTime givenTime = ZonedDateTime.of(ldt, myZone);
        ZoneId zID = ZoneId.of("UTC");
        ZonedDateTime zDT = ZonedDateTime.ofInstant(givenTime.toInstant(), zID);
        return zDT.toLocalDateTime();
    }

    /**
     * converts UTC time to system default time.
     * @param ldt UTC Time
     * @return system time
     */
    public static LocalDateTime convertToUserTimeZone(LocalDateTime ldt){
        ZoneId myZone = ZoneId.systemDefault();
        ZoneId zID = ZoneId.of("UTC");
        ZonedDateTime givenTime = ZonedDateTime.of(ldt, zID);
        ZonedDateTime zDT = ZonedDateTime.ofInstant(givenTime.toInstant(), myZone);
        return zDT.toLocalDateTime();
    }

}
