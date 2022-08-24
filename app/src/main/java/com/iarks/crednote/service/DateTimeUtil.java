package com.iarks.crednote.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

    public static final DateFormat defaultFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat[] acceptedPatterns = new SimpleDateFormat[] {
            new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH),
            new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH),
            new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    };

    public static final Date defaultDate = new Date(1990, 1, 1);

    public static Date tryParseDate(String value) {

        if(value == null || value.trim().equals(""))
        {
            return defaultDate;
        }

        for (SimpleDateFormat acceptedPattern: acceptedPatterns){
            try {
                return acceptedPattern.parse(value);
            } catch (ParseException e) {
            }
        }

        return defaultDate;
    }
}
