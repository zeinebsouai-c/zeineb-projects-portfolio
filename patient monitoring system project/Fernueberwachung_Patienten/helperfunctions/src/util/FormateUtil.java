package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormateUtil {

    public static int safeUnbox(Integer value){
        return value == null ? 0 : value;
    }

    public static String safeUnbox(String value){
        return value == null ? "" : value;
    }

    public static double safeUnbox(Double value) {
        return value == null ? 0.0 : value;
    }
    public static Date getEuropeanDateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.format(date);
        return date;
    }
}
