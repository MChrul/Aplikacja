package com.example.malgo.ewidencja.helpersExtendsAdapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static final CharSequence[] MONTHS_STRING = { "styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"};

    public String parseDate(String textDate) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date formatDate = Calendar.getInstance().getTime();

        try{
            formatDate = formatter.parse(textDate);
        } catch (java.text.ParseException e)
        {
            e.printStackTrace();
        }

        String date = new SimpleDateFormat("yyyy-MM-dd").format(formatDate);
        return date;
    }

    public String getMonthAsNumber(String month) {
        switch (month) {
            case "styczeń":
                return "01";
            case "luty":
                return "02";
            case "marzec":
                return "03";
            case "kwiecień":
                return "04";
            case "maj":
                return "05";
            case "czerwiec":
                return "06";
            case "lipiec":
                return "07";
            case "sierpień":
                return "08";
            case "wrzesień":
                return "09";
            case "październik":
                return "10";
            case "listopad":
                return "11";
            case "grudzień":
                return "12";
            default:
                return "";
        }
    }

    public String getMonthFromNumber(String month) {
        switch (month) {
            case "01":
                return "styczeń";
            case "02":
                return "luty";
            case "03":
                return "marzec";
            case "04":
                return "kwiecień";
            case "05":
                return "maj";
            case "06":
                return "czerwiec";
            case "07":
                return "lipiec";
            case "08":
                return "sierpień";
            case "09":
                return "wrzesień";
            case "10":
                return "październik";
            case "11":
                return "listopad";
            case "12":
                return "grudzień";
            default:
                return "";
        }
    }

    public String getMonthAsNumber(int month) {
        switch (month) {
            case 1:
                return "01";
            case 2:
                return "02";
            case 3:
                return "03";
            case 4:
                return "04";
            case 5:
                return "05";
            case 6:
                return "06";
            case 7:
                return "07";
            case 8:
                return "08";
            case 9:
                return "09";
            case 10:
                return "10";
            case 11:
                return "11";
            case 12:
                return "12";
            default:
                return "";
        }
    }
}
