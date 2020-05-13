package de.me.fill.mblum.android.fillme;

public class DateHelper {
    String getMonthShort(int monthNumber) {
        String monthShort = "";

        switch (monthNumber) {
            case 1:
                monthShort = "JAN";
                break;
            case 2:
                monthShort = "FEB";
                break;
            case 3:
                monthShort = "MAR";
                break;
            case 4:
                monthShort = "APR";
                break;
            case 5:
                monthShort = "MAI";
                break;
            case 6:
                monthShort = "JUN";
                break;
            case 7:
                monthShort = "JUL";
                break;
            case 8:
                monthShort = "AUG";
                break;
            case 9:
                monthShort = "SEP";
                break;
            case 10:
                monthShort = "OKT";
                break;
            case 11:
                monthShort = "NOV";
                break;
            case 12:
                monthShort = "DEZ";
                break;
        }

        return monthShort;
    }
}
