package de.me.fill.mblum.android.fillme;

import java.util.Calendar;

/**
 * Class FillEntry
 * <p>
 * Stores the data of an input.
 */
class FillEntry {

    private int id;
    private String date;
    private int mileage;
    private double liter;
    private double price;
    private int status;
    private int lastChanged;
    private int drivenMileage;

    /**
     * Constructor with id.
     *
     * @param id          The object ID
     * @param date        The added date
     * @param mileage     The current mileage of add date
     * @param liter       The liter amount
     * @param price       The price
     * @param status      Status if user input or auto generated
     * @param lastChanged dateTime for last changed
     */
    FillEntry(int id, String date, int mileage, double liter, double price, int status, int lastChanged) {

        this.id = id;
        this.date = date;
        this.mileage = mileage;
        this.liter = liter;
        this.price = price;
        this.status = status;
        this.lastChanged = lastChanged;
        this.drivenMileage = 0;
    }

    /**
     * Constructor without id.
     *
     * @param date        The added date
     * @param mileage     The current mileage of add date
     * @param liter       The liter amount
     * @param price       The price
     * @param status      Status if user input or auto generated
     * @param lastChanged dateTime for last changed
     */
    FillEntry(String date, int mileage, double liter, double price, int status, int lastChanged) {

        this.date = date;
        this.mileage = mileage;
        this.liter = liter;
        this.price = price;
        this.status = status;
        this.lastChanged = lastChanged;
        this.drivenMileage = 0;
    }

    int getID() {
        return id;
    }

    String getStringDate() {
        return date;
    }

    Calendar getDate() {
        Calendar date = Calendar.getInstance();

        int year = Integer.parseInt(this.date.substring(6, 10));
        int month = Integer.parseInt(this.date.substring(3, 5)) - 1;
        int day = Integer.parseInt(this.date.substring(0, 2));

        date.set(year, month, day);

        return date;
    }

    /**
     * Returns the date of the month (e. g. 21 for 21.12.2020)
     *
     * @return String
     */
    String getDayOfMonth() {
        return this.date.substring(0, 2);
    }

    String getMonthShort() {
        String monthShort = "";
        String numberOfMonth = this.date.substring(3, 5);

        switch (numberOfMonth) {
            case "01":
                monthShort = "JAN";
                break;
            case "02":
                monthShort = "FEB";
                break;
            case "03":
                monthShort = "MAR";
                break;
            case "04":
                monthShort = "APR";
                break;
            case "05":
                monthShort = "MAI";
                break;
            case "06":
                monthShort = "JUN";
                break;
            case "07":
                monthShort = "JUL";
                break;
            case "08":
                monthShort = "AUG";
                break;
            case "09":
                monthShort = "SEP";
                break;
            case "10":
                monthShort = "OKT";
                break;
            case "11":
                monthShort = "NOV";
                break;
            case "12":
                monthShort = "DEZ";
                break;
        }

        return monthShort;
    }

    int getMileage() {
        return mileage;
    }

    /**
     * Returns the amount of liter
     *
     * @return liter amount
     */
    double getLiter() {
        liter = liter * 100;
        liter = Math.round(liter);
        liter = liter / 100;

        return liter;
    }

    /**
     * Returns the price paid for the hole fuel tank
     *
     * @return full price
     */
    double getPrice() {
        price = price * 100;
        price = Math.round(price);
        price = price / 100;

        return price;
    }

    /**
     * Returns the price paid for a liter
     *
     * @return price paid for liter
     */
    double getLiterPrice() {
        double literPrice = price / liter;
        literPrice = literPrice * 1000;
        literPrice = Math.round(literPrice);
        literPrice = literPrice / 1000;

        return literPrice;
    }

    int getStatus() {
        return status;
    }

    /**
     * Returns driven mileage if set before (current mileage - mileage new entry)
     *
     * @return driven mileage
     */
    int getDrivenMileage() {
        return drivenMileage;
    }

    /**
     * Sets last mileage (current mileage - mileage new entry)
     *
     * @param drivenMileage last
     */
    void setDrivenMileage(int drivenMileage) {
        this.drivenMileage = drivenMileage;
    }

    int getLastChanged() {
        return lastChanged;
    }
}
