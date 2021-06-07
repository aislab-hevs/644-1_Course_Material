package ch.hevs.aislab.intro.database.converter;

import androidx.room.TypeConverter;

import java.time.ZonedDateTime;

public class LocalDateTimeConverter {

    @TypeConverter
    public static ZonedDateTime toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return ZonedDateTime.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(ZonedDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}