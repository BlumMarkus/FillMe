package de.me.fill.mblum.android.fillme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mblum on 11.07.2017.
 */

class FillEntryDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String logTag = "FillMeDataSource";

    FillEntryDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
        Log.d(logTag, "DataSource: DbHelper wurde erstellt.");
    }

    private void open() {
        Log.d(logTag, "Referenz auf die Datenbank wird nun angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(logTag, "Datenbankreferenz erhalten. Pfad der Datenbank: " + database.getPath());
    }

    private void close() {
        dbHelper.close();
        Log.d(logTag, "Datenbank mit Hilfe des dbHelper geschlossen.");
    }

    boolean writeEntry(FillEntry fillEntryObject) {
        open();

        ContentValues values = new ContentValues();
        long now = Calendar.getInstance().getTime().getTime();

        values.put(DatabaseHelper.FILLENTRY_COLUMN_DATE, fillEntryObject.getStringDate());
        values.put(DatabaseHelper.FILLENTRY_COLUMN_MILEAGE, fillEntryObject.getMileage());
        values.put(DatabaseHelper.FILLENTRY_COLUMN_LITER, fillEntryObject.getLiter());
        values.put(DatabaseHelper.FILLENTRY_COLUMN_PRICE, fillEntryObject.getPrice());
        values.put(DatabaseHelper.FILLENTRY_COLUMN_STATUS, fillEntryObject.getStatus());
        values.put(DatabaseHelper.FILLENTRY_COLUMN_LASTCHANGED, now);

        long result = database.insert(DatabaseHelper.TABLE_FILLENTRY, null, values);

        if (result == -1) {
            Log.d(logTag, "Das schreiben der Values (" + values + ") in die Datenbanktabelle " + DatabaseHelper.TABLE_FILLENTRY + " ist fehlgeschlagen!");
            close();
            return false;
        } else {
            Log.d(logTag, "Values (" + values + ") wurden erfolgreich in die Datenbanktabelle " + DatabaseHelper.TABLE_FILLENTRY + " geschrieben (FILL_ID = " + result + ").");
            close();
            return true;
        }
    }

    ArrayList<FillEntry> getAllEntries(Boolean isDesc) {
        Cursor cursor;
        ArrayList<FillEntry> list = new ArrayList<>();
        String descOrAsc = "ASC";

        if (isDesc) {
            descOrAsc = "DESC";
        }

        open();

        String sql = "SELECT " +
                DatabaseHelper.FILLENTRY_COLUMN_ID + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_DATE + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_MILEAGE + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_LITER + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_PRICE + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_STATUS + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_LASTCHANGED +
                " FROM " + DatabaseHelper.TABLE_FILLENTRY +
                " ORDER BY " + DatabaseHelper.FILLENTRY_COLUMN_MILEAGE + " " + descOrAsc;
        cursor = database.rawQuery(sql, null);
        Log.d(logTag, "Eintrag wurden erfolgreich aus der Datenbanktabelle " + DatabaseHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry fillEntry;

        while (!cursor.isAfterLast()) {
            fillEntry = cursorToEntry(cursor);
            list.add(fillEntry);
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    FillEntry getEntryById(int id) {
        Cursor cursor;

        open();

        String sql = "SELECT " +
                DatabaseHelper.FILLENTRY_COLUMN_ID + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_DATE + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_MILEAGE + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_LITER + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_PRICE + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_STATUS + ", " +
                DatabaseHelper.FILLENTRY_COLUMN_LASTCHANGED +
                " FROM " + DatabaseHelper.TABLE_FILLENTRY +
                " WHERE " + DatabaseHelper.FILLENTRY_COLUMN_ID + " = " + id;
        cursor = database.rawQuery(sql, null);
        Log.d(logTag, "Eintrag wurden erfolgreich aus der Datenbanktabelle " + DatabaseHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;
        m = cursorToEntry(cursor);

        cursor.close();
        close();

        return m;
    }

    void updateData(FillEntry updatedItem) {
        open();

        long now = Calendar.getInstance().getTime().getTime();

        String sql = "UPDATE " + DatabaseHelper.TABLE_FILLENTRY +
                " SET " + DatabaseHelper.FILLENTRY_COLUMN_DATE + " = '" + updatedItem.getStringDate() + "', " +
                DatabaseHelper.FILLENTRY_COLUMN_MILEAGE + " = '" + updatedItem.getMileage() + "', " +
                DatabaseHelper.FILLENTRY_COLUMN_LITER + " = '" + updatedItem.getLiter() + "', " +
                DatabaseHelper.FILLENTRY_COLUMN_PRICE + " = '" + updatedItem.getPrice() + "', " +
                DatabaseHelper.FILLENTRY_COLUMN_STATUS + " = '" + updatedItem.getStatus() + "', " +
                DatabaseHelper.FILLENTRY_COLUMN_LASTCHANGED + " = '" + now +
                "' WHERE " + DatabaseHelper.FILLENTRY_COLUMN_ID + " = " + updatedItem.getID();

        database.execSQL(sql);

        close();
    }

    void deleteDataById(int entryId) {
        open();

        String sql = "DELETE " +
                " FROM " + DatabaseHelper.TABLE_FILLENTRY +
                " WHERE " + DatabaseHelper.FILLENTRY_COLUMN_ID + " = " + entryId;

        database.execSQL(sql);

        close();
    }

    void deleteAllData() {
        open();
        database.execSQL("delete from " + DatabaseHelper.TABLE_FILLENTRY);
        close();
    }

    private FillEntry cursorToEntry(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DatabaseHelper.FILLENTRY_COLUMN_ID);
        int dateIndex = cursor.getColumnIndex(DatabaseHelper.FILLENTRY_COLUMN_DATE);
        int mileageIndex = cursor.getColumnIndex(DatabaseHelper.FILLENTRY_COLUMN_MILEAGE);
        int literIndex = cursor.getColumnIndex(DatabaseHelper.FILLENTRY_COLUMN_LITER);
        int priceIndex = cursor.getColumnIndex(DatabaseHelper.FILLENTRY_COLUMN_PRICE);
        int statusIndex = cursor.getColumnIndex(DatabaseHelper.FILLENTRY_COLUMN_STATUS);
        int lastChangedIndex = cursor.getColumnIndex(DatabaseHelper.FILLENTRY_COLUMN_LASTCHANGED);

        int id = cursor.getInt(idIndex);
        String date = cursor.getString(dateIndex);
        int mileage = cursor.getInt(mileageIndex);
        double liter = cursor.getDouble(literIndex);
        double price = cursor.getDouble(priceIndex);
        int status = cursor.getInt(statusIndex);
        int lastChanged = cursor.getInt(lastChangedIndex);

        return new FillEntry(id, date, mileage, liter, price, status, lastChanged);
    }
}
