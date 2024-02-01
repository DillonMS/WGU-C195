package smith.c195v2;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConversions {

    public static LocalDateTime convertToEST(LocalDateTime ldt){
        ZoneId myZone = ZoneId.systemDefault();
        ZonedDateTime givenTime = ZonedDateTime.of(ldt, myZone);
        ZoneId zID = ZoneId.of("America/New_York");
        ZonedDateTime zDT = ZonedDateTime.ofInstant(givenTime.toInstant(), zID);
        LocalDateTime newlDT = zDT.toLocalDateTime();
        return newlDT;
    }

    public static LocalDateTime convertToUTC(LocalDateTime ldt){
        ZoneId myZone = ZoneId.systemDefault();
        ZonedDateTime givenTime = ZonedDateTime.of(ldt, myZone);
        ZoneId zID = ZoneId.of("UTC");
        ZonedDateTime zDT = ZonedDateTime.ofInstant(givenTime.toInstant(), zID);
        LocalDateTime newlDT = zDT.toLocalDateTime();
        return newlDT;
    }

    public static LocalDateTime convertToUserTimeZone(LocalDateTime ldt){
        ZoneId myZone = ZoneId.systemDefault();
        ZoneId zID = ZoneId.of("UTC");
        ZonedDateTime givenTime = ZonedDateTime.of(ldt, zID);
        ZonedDateTime zDT = ZonedDateTime.ofInstant(givenTime.toInstant(), myZone);
        LocalDateTime newlDT = zDT.toLocalDateTime();
        return newlDT;
    }

}
