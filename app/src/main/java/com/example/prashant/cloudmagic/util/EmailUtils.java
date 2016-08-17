package com.example.prashant.cloudmagic.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Prashant on 17/08/2016.
 */
public class EmailUtils {
    public static String getDate(long ts) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
        Date resultdate = new Date(ts);
        return sdf.format(resultdate);
    }

    public static String getFullDateTime(long ts) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM, yyyy, HH:mm:ss a", Locale.getDefault());
        Date resultdate = new Date(ts);
        return sdf.format(resultdate);
    }

}
