package de.me.fill.mblum.android.fillme;

/**
 * Class FillEntry
 *
 * Stores the data of an input.
 */
class FillEntry {

    private int id;
    private String date;
    private int mileage;
    private double liter;
    private double price;
    private int status;

    /**
     * Constructor with id.
     *
     * @param id      The object ID
     * @param date    The added date
     * @param mileage The current mileage of add date
     * @param liter   The liter amount
     * @param price   The price
     * @param status  Status if user input or auto generated
     */
    FillEntry(int id, String date, int mileage, double liter, double price, int status) {

        this.id = id;
        this.date = date;
        this.mileage = mileage;
        this.liter = liter;
        this.price = price;
        this.status = status;
    }

    /**
     * Constructor without id.
     *
     * @param date    The added date
     * @param mileage The current mileage of add date
     * @param liter   The liter amount
     * @param price   The price
     * @param status  Status if user input or auto generated
     */
    FillEntry(String date, int mileage, double liter, double price, int status) {

        this.date = date;
        this.mileage = mileage;
        this.liter = liter;
        this.price = price;
        this.status = status;
    }

    int getID() {
        return id;
    }

    String getDate() {
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

    double getLiter() {
        liter = liter * 100;
        liter = Math.round(liter);
        liter = liter / 100;
        return liter;
    }

    double getPrice() {
        price = price * 100;
        price = Math.round(price);
        price = price / 100;
        return price;
    }

    int getStatus() {
        return status;
    }
}
