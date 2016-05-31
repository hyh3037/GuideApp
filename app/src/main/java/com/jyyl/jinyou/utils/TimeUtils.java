package com.jyyl.jinyou.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * TimeUtils
 */
public class TimeUtils {

    private TimeUtils() {
        throw new UnsupportedOperationException("T cannot be instantiated");
    }

    /**
     * 获取当前时间
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String dateString;
        dateString = df.format(Calendar.getInstance().getTime());
        return dateString;
    }

}
