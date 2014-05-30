package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrey
 * 25.04.14.
 */
public class TimeHelper {

    private final static DateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");
    public static String getTimeString()
    {
        return FORMATTER.format(new Date());
    }

    public static Date getTime()
    {
        return new Date();
    }
}
