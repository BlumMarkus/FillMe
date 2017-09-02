package de.me.fill.mblum.android.fillme;

/**
 * Created by mblum on 11.07.2017.
 */

public class FillEntry {

    private int id;
    private String date;
    private int mileage;
    private double liter;
    private double price;
    private int status;

    public FillEntry(int id, String date, int mileage, double liter, double price, int status) {

        this.id = id;
        this.date = date;
        this.mileage = mileage;
        this.liter = liter;
        this.price = price;
        this.status = status;
    }

    public FillEntry(String date, int mileage, double liter, double price, int status) {

        this.date = date;
        this.mileage = mileage;
        this.liter = liter;
        this.price = price;
        this.status = status;
    }

    public int getID() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getMileage() {
        return mileage;
    }

    public double getLiter() {
        return liter;
    }

    public double getPrice() {
        return price;
    }

    public int getStatus() {
        return status;
    }
}
