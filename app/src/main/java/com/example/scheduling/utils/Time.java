package com.example.scheduling.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Time {
    public static String[] month = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static String getMonth(int m){
        return month[m];
    }

    public static Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar nCalendar = Calendar.getInstance();
        nCalendar.set(year, month, day);
        nCalendar.set(Calendar.HOUR_OF_DAY, 0);
        nCalendar.set(Calendar.MINUTE, 0);
        nCalendar.set(Calendar.SECOND, 0);
        nCalendar.set(Calendar.MILLISECOND, 0);

        return nCalendar;
    }

    public static Calendar getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        Calendar nCalendar = Calendar.getInstance();

        nCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        nCalendar.set(Calendar.MINUTE, minute);

        return nCalendar;
    }

    public static String date2string(int day, int month, int year) {
        String dayStr = Integer.toString(day);
        String monthStr = getMonth(month+1);
        String yearStr = Integer.toString(year);
        return monthStr + " " + dayStr + ", " + yearStr;
    }

    public static int[] string2date(String date) {
        String[] splited_1 = date.split("\\s+");
        String[] splited_2 = splited_1[1].split(",");
        int[] date_arary = {Integer.valueOf(splited_1[2]), new ArrayList<String>(Arrays.asList(month)).indexOf(splited_1[0])-1, Integer.valueOf(splited_2[0])};
        return date_arary;
    }

    public static Long string2format(String date) {
        String[] splited_1 = date.split("\\s+");
        String[] splited_2 = splited_1[1].split(",");

        Calendar calendar = Time.getDate();
        calendar.set(Integer.valueOf(splited_1[2]), new ArrayList<String>(Arrays.asList(month)).indexOf(splited_1[0])-1, Integer.valueOf(splited_2[0]));

        return calendar.getTimeInMillis();
    }

    public static String format2string(Long date) {
        Calendar calendar = Time.getDate();
        calendar.setTimeInMillis(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return Time.date2string(day, month, year);
    }

    public static Long date2format(int day, int month, int year) {
        Calendar calendar = Time.getDate();
        calendar.set(year, month, day);

        return calendar.getTimeInMillis();
    }

    public static int[] format2date(Long date) {
        Calendar calendar = Time.getDate();
        calendar.setTimeInMillis(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] date_arary = {day, month, year};
        return date_arary;
    }

    public static int string2time(String str) {
        String[] splited_1 = str.split("\\s+");
        String[] splited_2 = splited_1[0].split(":");
        int unit = 0;
        if(splited_1[1].equals("PM")){
            unit = 1200;
        }
        return Integer.parseInt(splited_2[0]) * 100 + Integer.parseInt(splited_2[1]) + unit;
    }
}
