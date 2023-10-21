package APIChatGPT.util;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class AppUtil {
    public static String getTimeFormat(long timeStamp) {
        try {
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTimeInMillis(timeStamp);
            String date = DateFormat.format("HH:mm", cal).toString();
            return date;
        } catch (Exception e) {
        }
        return "";
    }

}
