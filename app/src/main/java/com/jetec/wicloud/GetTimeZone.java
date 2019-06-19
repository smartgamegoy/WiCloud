package com.jetec.wicloud;

import android.annotation.SuppressLint;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class GetTimeZone {

    private static final String TAG = "GetTimeZone";

    public GetTimeZone() {
        super();
    }

    public String getTime_date() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat time_ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        time_ymd.setTimeZone(TimeZone.getTimeZone("GMT 00"));
        return time_ymd.format(new Date());
    }

    public String getTime_Day(String day) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date nowDate;
        String reset_date = "";
        try {
            nowDate = today.parse(day);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            reset_date = simpleDateFormat.format(nowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reset_date;
    }

    public String setTime(String day) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat yesterday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date nowDate;
        String get_olddate = "";
        try {
            nowDate = yesterday.parse(day);
            Date oldDate = new Date(nowDate.getTime() - (long) (24 * 60 * 60 * 1000));
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            get_olddate = simpleDateFormat.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return get_olddate;
    }

    public String resetTime(String day, int GMT) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat yesterday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date oldDate;
        String get_nowdate = "";
        try {
            oldDate = yesterday.parse(day);
            Date nowDate = new Date(oldDate.getTime() + (long) (GMT * 60 * 60 * 1000));
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            get_nowdate = simpleDateFormat.format(nowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return get_nowdate;
    }

    public int getTimeZone() {
        TimeZone GMT = TimeZone.getDefault();
        String str = GMT.getDisplayName(false, TimeZone.SHORT);
        int i = GMT.getRawOffset();
        i = (int)TimeUnit.HOURS.convert(i, TimeUnit.MILLISECONDS);
        Log.d(TAG,"i = " + i);
        Log.d(TAG,"str = " + str);
        return i;
    }
}