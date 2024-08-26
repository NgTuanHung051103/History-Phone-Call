package com.example.myapplication;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Extension {
    public static String getFormattedDate(String callDate) {
        long dateMillis = Long.parseLong(callDate);
        Calendar callTime = Calendar.getInstance();
        callTime.setTimeInMillis(dateMillis);

        Calendar now = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        if (isSameDay(callTime, now)) {
            return timeFormat.format(callTime.getTime());
        } else if (isYesterday(callTime, now)) {
            return "Hôm qua";
        } else if (isWithinLastWeek(callTime, now)) {
            return dayFormat.format(callTime.getTime());
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return dateFormat.format(callTime.getTime());
        }
    }

    private static boolean isSameDay(Calendar callTime, Calendar now) {
        return now.get(Calendar.YEAR) == callTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == callTime.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isYesterday(Calendar callTime, Calendar now) {
        now.add(Calendar.DAY_OF_YEAR, -1);
        boolean isYesterday = isSameDay(callTime, now);
        now.add(Calendar.DAY_OF_YEAR, 1); // reset lại ngày hiện tại
        return isYesterday;
    }

    private static boolean isWithinLastWeek(Calendar callTime, Calendar now) {
        now.add(Calendar.DAY_OF_YEAR, -7);
        boolean withinLastWeek = callTime.after(now);
        now.add(Calendar.DAY_OF_YEAR, 7); // reset lại tuần hiện tại
        return withinLastWeek;
    }
}
