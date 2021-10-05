package ch.hevs.aislab.intro.database.converter;

import androidx.room.TypeConverter;

import java.time.Instant;

public class LocalDateTimeConverter {

    @TypeConverter
    public static Instant toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return Instant.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(Instant date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}