package smith.c195v2;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConversions {

    public static LocalDateTime convertToEST(LocalDateTime lt){
        ZoneId myZone = ZoneId.systemDefault();
        ZonedDateTime givenTime = ZonedDateTime.of(lt, myZone);
        ZoneId zID = ZoneId.of("America/New_York");
        ZonedDateTime zDT = ZonedDateTime.ofInstant(givenTime.toInstant(), zID);
        LocalDateTime lDT = zDT.toLocalDateTime();
        return lDT;
    }

}
