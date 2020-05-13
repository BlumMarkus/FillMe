package de.me.fill.mblum.android.fillme;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Provides functions based on fillEntry Object list
 */
class FillEntriesHelper {

    private final ArrayList<FillEntry> fillEntryList;

    FillEntriesHelper(ArrayList<FillEntry> fillEntryList) {
        this.fillEntryList = setOptionalFillEntryValues(fillEntryList);
    }

    /**
     * Returns the average of paid price for specific month
     *
     * @param month month (1 = januar, 12 = december)
     * @param year  year (e. g. 2020)
     * @return paid money
     */
    double getPriceAvgMonth(int month, int year) {
        ArrayList<FillEntry> listOfSpecificMonth = getObjectsOfMonth(month, year);

        int listSize = listOfSpecificMonth.size();
        double sumPrice = 0.00;
        double avgPrice;

        if (listSize == 0) {
            avgPrice = 0.00;
        } else {
            for (FillEntry fillEntryObject : listOfSpecificMonth) {
                sumPrice += fillEntryObject.getPrice();
            }

            avgPrice = sumPrice / listSize;
        }

        return avgPrice;
    }

    /**
     * Returns the average of filled liter for specific month
     *
     * @param month month (1 = januar, 12 = december)
     * @param year  year (e. g. 2020)
     * @return filled liter
     */
    double getLiterAvgMonth(int month, int year) {
        ArrayList<FillEntry> listOfSpecificMonth = getObjectsOfMonth(month, year);

        int listSize = listOfSpecificMonth.size();
        double sumLiter = 0.00;
        double avgLiter;

        if (listSize == 0) {
            avgLiter = 0.00;
        } else {
            for (FillEntry fillEntryObject : listOfSpecificMonth) {
                sumLiter += fillEntryObject.getPrice();
            }

            avgLiter = sumLiter / listSize;
        }

        return avgLiter;
    }

    /**
     * Returns the average of consumption for specific month
     *
     * @param month month (1 = january, 12 = december)
     * @param year  year (e. g. 2020)
     * @return consumption average of month
     */
    double getConsumptionAvgMonth(int month, int year) {
        ArrayList<FillEntry> listOfSpecificMonth = getObjectsOfMonth(month, year);

        int listSize = listOfSpecificMonth.size();
        double sumMileage = 0;
        double sumLiter = 0.00;
        double avgConsumption;

        if (listSize == 0) {
            avgConsumption = 0;
        } else {
            for (FillEntry fillEntryObject : listOfSpecificMonth) {
                sumMileage += fillEntryObject.getDrivenMileage();
                sumLiter += fillEntryObject.getLiter();
            }

            avgConsumption = sumLiter / (sumMileage / 100);
        }

        return avgConsumption;
    }

    /**
     * Sets optional fields of fillEntry object
     *
     * @param fillEntryList ArrayList of FillEntry Objects
     * @return ArrayList FillEntry
     */
    ArrayList<FillEntry> setOptionalFillEntryValues(ArrayList<FillEntry> fillEntryList) {
        int drivenMileage;
        int maxDrivenMileage = 0;
        int listSize = fillEntryList.size();

        if (listSize == 0) {
            return fillEntryList;
        }

        // listSize - 1, because last Entry cant use object before
        for (int i = 0; i < listSize - 1; i++) {
            drivenMileage = fillEntryList.get(i).getMileage() - fillEntryList.get(i + 1).getMileage();

            if (maxDrivenMileage < drivenMileage) {
                maxDrivenMileage = drivenMileage;
            }

            fillEntryList.get(i).setDrivenMileage(drivenMileage);
        }

        // Check if mileage of last Entry can be driven mileage => 50 % on top of max
        int lastObjectMileage = fillEntryList.get(listSize - 1).getMileage();
        if (lastObjectMileage < (maxDrivenMileage * 1.5)) {
            fillEntryList.get(listSize - 1).setDrivenMileage(lastObjectMileage);
        }

        return fillEntryList;
    }

    /**
     * Returns fillEntry-List for entries of specific month
     *
     * @param month month (1 = januar, 12 = december)
     * @param year  year (e. g. 2020)
     * @return object list for specific month
     */
    private ArrayList<FillEntry> getObjectsOfMonth(int month, int year) {
        ArrayList<FillEntry> monthList = new ArrayList<>();

        for (FillEntry fillEntryObject : this.fillEntryList) {
            int objectYear = fillEntryObject.getDate().get(Calendar.YEAR);
            int objectMonth = fillEntryObject.getDate().get(Calendar.MONTH);

            if (objectYear == year && objectMonth == month) {
                monthList.add(fillEntryObject);
            }
        }

        return monthList;
    }
}
