package com.example.webwerks.autosms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    public static String Date(String date){

        String mytime= date;

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM-dd-yyyy");

        Date myDate = null;
        try {
            myDate = dateFormat.parse(mytime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String finalDate = timeFormat.format(myDate);

        System.out.println(finalDate);

        return finalDate;
    }
}
