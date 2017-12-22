package com.study.yang.lifehelper.utils;

import android.text.format.DateFormat;

/**
 * Created by Administrator on 2017/3/30/030.
 */

public class DataFormatUtils {
    public static String getFormatDate(long currentTime) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
 //      String format = simpleDateFormat.format(new Date(currentTime));
        CharSequence format = DateFormat.format("yyyy年MM月dd日 HH:mm:ss", currentTime);
        return format.toString();
    }

    public static String getFormatTime(long duration) {
        CharSequence format = DateFormat.format("mm:ss", duration);
        return format.toString();
    }
}
