package com.neon.vyaan.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Mayank on 14/07/2016.
 */
public class DateTimeUtils {

    public static String GenerateRandomDate(int minAge, int maxAge) {

        Calendar calendarMinDate = Calendar.getInstance();

        calendarMinDate.add(Calendar.YEAR, -maxAge);


        Calendar calendarMaxDate = Calendar.getInstance();

        calendarMaxDate.add(Calendar.YEAR, -minAge);

        int year = randomNo(calendarMinDate.get(Calendar.YEAR) + 1, calendarMaxDate.get(Calendar.YEAR) - 1);

        int month = randomNo(1, 12);

        int day = randomNo(10, 28);

        Log.e("gen birth date", day + "/" + month + "/" + year);


        return day + "/" + month + "/" + year;


    }


    private static int randomNo(int low, int high) {
        Random r = new Random();

        return r.nextInt(high - low) + low;
    }


    public static long currentTimeMills() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();

    }

    public static String currentDate(String format, Calendar calendar) {


        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(calendar.getTime());
    }

    public static Calendar calendarByDate(String format, String stringDate) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    public static String dateInParticularFormat(String format, String requiredFormat, String stringDate) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(requiredFormat, Locale.US);
        return sdf.format(calendar.getTime());

    }


    public static int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }


}
