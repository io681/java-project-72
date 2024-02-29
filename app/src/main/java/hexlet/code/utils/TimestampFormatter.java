package hexlet.code.utils;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class TimestampFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getTimeStampToString(Timestamp timestamp) {
        return timestamp.toLocalDateTime().format(FORMATTER);
    }
    public static Timestamp getCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
