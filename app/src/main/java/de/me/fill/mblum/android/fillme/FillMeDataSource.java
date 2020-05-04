package de.me.fill.mblum.android.fillme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mblum on 11.07.2017.
 */

class FillMeDataSource {
    private SQLiteDatabase db;
    private FillMeDbHelper dbHelper;
    private FillEntry o;
    private String logTag = "FillMeDataSource";

    FillMeDataSource(Context context) {
        dbHelper = new FillMeDbHelper(context);
        Log.d(logTag, "DataSource: DbHelper wurde erstellt.");
    }

    private void open() {
        Log.d(logTag, "Referenz auf die Datenbank wird nun angefragt.");
        db = dbHelper.getWritableDatabase();
        Log.d(logTag, "Datenbankreferenz erhalten. Pfad der Datenbank: " + db.getPath());
    }

    private void close() {
        dbHelper.close();
        Log.d(logTag, "Datenbank mit Hilfe des dbHelper geschlossen.");
    }

    boolean writeEntry(FillEntry m) {
        o = new FillEntry(9, null, 9, 9.9, 9.9, 9);
        this.o = m;
        Log.d(logTag, "Übergebenens Objekt (m) wurde mit Objekt (o) überschrieben und hat nun folgende Werte:");
        Log.d(logTag, "Wert für Datum: " + o.getDate() + ", Wert Mileage: " + o.getMileage() + ", Wert Liter: " + o.getLiter() + ", Wert Price: " + o.getPrice() + ", Wert status " + o.getStatus() + ".");

        open();

        ContentValues values = new ContentValues();

        values.put(FillMeDbHelper.COLUMN_DATE, o.getDate());
        values.put(FillMeDbHelper.COLUMN_MILEAGE, o.getMileage());
        values.put(FillMeDbHelper.COLUMN_LITER, o.getLiter());
        values.put(FillMeDbHelper.COLUMN_PRICE, o.getPrice());
        values.put(FillMeDbHelper.COLUMN_STATUS, o.getStatus());

        Log.d(logTag, "Value erstellt: " + FillMeDbHelper.COLUMN_DATE + " mit " + o.getDate() + ".");
        Log.d(logTag, "Value erstellt: " + FillMeDbHelper.COLUMN_MILEAGE + " mit " + o.getMileage() + ".");
        Log.d(logTag, "Value erstellt: " + FillMeDbHelper.COLUMN_LITER + " mit " + o.getLiter() + ".");
        Log.d(logTag, "Value erstellt: " + FillMeDbHelper.COLUMN_PRICE + " mit " + o.getPrice() + ".");
        Log.d(logTag, "Value erstellt: " + FillMeDbHelper.COLUMN_STATUS + " mit " + o.getStatus() + ".");

        long result = db.insert(FillMeDbHelper.TABLE_FILLENTRY, null, values);

        if (result == -1) {
            Log.d(logTag, "Das schreiben der Values (" + values + ") in die Datenbanktabelle " + FillMeDbHelper.TABLE_FILLENTRY + " ist fehlgeschlagen!");
            close();
            return false;
        } else {
            Log.d(logTag, "Values (" + values + ") wurden erfolgreich in die Datenbanktabelle " + FillMeDbHelper.TABLE_FILLENTRY + " geschrieben (FILL_ID = " + result + ").");
            close();
            return true;
        }
    }

    ArrayList<FillEntry> getlastMonth(int month, int year) {
        Cursor cursor;
        ArrayList<FillEntry> list = new ArrayList<>();

        open();

        String sql = "SELECT " +
                FillMeDbHelper.COLUMN_ID + ", " +
                FillMeDbHelper.COLUMN_DATE + ", " +
                FillMeDbHelper.COLUMN_MILEAGE + ", " +
                FillMeDbHelper.COLUMN_LITER + ", " +
                FillMeDbHelper.COLUMN_PRICE + ", " +
                FillMeDbHelper.COLUMN_STATUS +
                " FROM " + FillMeDbHelper.TABLE_FILLENTRY +
                " WHERE " + FillMeDbHelper.COLUMN_DATE + " LIKE '%.%" + month + "." + year + "'" +
                " ORDER BY " + FillMeDbHelper.COLUMN_MILEAGE + " DESC ";
        Log.d(logTag, sql);
        cursor = db.rawQuery(sql, null);
        Log.d(logTag, "Eintrag wurden erfolgreich aus der Datenbanktabelle " + FillMeDbHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;

        while (!cursor.isAfterLast()) {
            m = cursorToEntry(cursor);
            list.add(m);
            Log.d(logTag, "Objekt m wurde der Liste hinzugefügt.");
            Log.d(logTag, "Datum: " + m.getDate() + ", Mileage: " + m.getMileage() + ", Liter: " + m.getLiter() + ", Price: " + m.getPrice() + ")");
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    ArrayList<FillEntry> getlastYear(int year) {
        Cursor cursor;
        ArrayList<FillEntry> list = new ArrayList<>();

        open();

        String sql = "SELECT " +
                FillMeDbHelper.COLUMN_ID + ", " +
                FillMeDbHelper.COLUMN_DATE + ", " +
                FillMeDbHelper.COLUMN_MILEAGE + ", " +
                FillMeDbHelper.COLUMN_LITER + ", " +
                FillMeDbHelper.COLUMN_PRICE + ", " +
                FillMeDbHelper.COLUMN_STATUS +
                " FROM " + FillMeDbHelper.TABLE_FILLENTRY +
                " WHERE " + FillMeDbHelper.COLUMN_DATE + " LIKE '%." + year + "'" +
                " ORDER BY " + FillMeDbHelper.COLUMN_MILEAGE + " DESC ";
        cursor = db.rawQuery(sql, null);
        Log.d(logTag, "Eintrag wurden erfolgreich aus der Datenbanktabelle " + FillMeDbHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;

        while (!cursor.isAfterLast()) {
            m = cursorToEntry(cursor);
            list.add(m);
            Log.d(logTag, "Objekt m wurde der Liste hinzugefügt.");
            Log.d(logTag, "Datum: " + m.getDate() + ", Mileage: " + m.getMileage() + ", Liter: " + m.getLiter() + ", Price: " + m.getPrice() + ")");
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    /**
     * Returns last N entries of the database
     *
     * @param amount amount of entries
     *
     * @return The N entries
     */
    ArrayList<FillEntry> getLastEntries(int amount) {
        Cursor cursor;
        ArrayList<FillEntry> list = new ArrayList<>();

        open();

        String sqlQuery = "SELECT " +
                FillMeDbHelper.COLUMN_ID + ", " +
                FillMeDbHelper.COLUMN_DATE + ", " +
                FillMeDbHelper.COLUMN_MILEAGE + ", " +
                FillMeDbHelper.COLUMN_LITER + ", " +
                FillMeDbHelper.COLUMN_PRICE + ", " +
                FillMeDbHelper.COLUMN_STATUS +
                " FROM " + FillMeDbHelper.TABLE_FILLENTRY +
                " ORDER BY " + FillMeDbHelper.COLUMN_MILEAGE + " DESC " +
                " LIMIT " + amount;
        cursor = db.rawQuery(sqlQuery, null);

        cursor.moveToFirst();
        FillEntry object;

        while (!cursor.isAfterLast()) {
            object = cursorToEntry(cursor);
            list.add(object);
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    ArrayList<FillEntry> getAllEntries(Boolean isDesc) {
        Cursor cursor;
        ArrayList<FillEntry> list = new ArrayList<>();
        String descOrAsc = "ASC";

        if (isDesc){
            descOrAsc = "DESC";
        }

        open();

        String sql = "SELECT " +
                FillMeDbHelper.COLUMN_ID + ", " +
                FillMeDbHelper.COLUMN_DATE + ", " +
                FillMeDbHelper.COLUMN_MILEAGE + ", " +
                FillMeDbHelper.COLUMN_LITER + ", " +
                FillMeDbHelper.COLUMN_PRICE + ", " +
                FillMeDbHelper.COLUMN_STATUS +
                " FROM " + FillMeDbHelper.TABLE_FILLENTRY +
                " ORDER BY " + FillMeDbHelper.COLUMN_MILEAGE + " " + descOrAsc;
        cursor = db.rawQuery(sql, null);
        Log.d(logTag, "Eintrag wurden erfolgreich aus der Datenbanktabelle " + FillMeDbHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;

        while (!cursor.isAfterLast()) {
            m = cursorToEntry(cursor);
            list.add(m);
            Log.d(logTag, "Objekt m wurde der Liste hinzugefügt.");
            Log.d(logTag, "ID: " + m.getID() + ", Datum: " + m.getDate() + ", Mileage: " + m.getMileage() + ", Liter: " + m.getLiter() + ", Price: " + m.getPrice() + "Status: " + m.getStatus() + ")");
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    FillEntry getEntriesById(int id) {
        Cursor cursor;

        open();

        String sql = "SELECT " +
                FillMeDbHelper.COLUMN_ID + ", " +
                FillMeDbHelper.COLUMN_DATE + ", " +
                FillMeDbHelper.COLUMN_MILEAGE + ", " +
                FillMeDbHelper.COLUMN_LITER + ", " +
                FillMeDbHelper.COLUMN_PRICE + ", " +
                FillMeDbHelper.COLUMN_STATUS +
                " FROM " + FillMeDbHelper.TABLE_FILLENTRY +
                " WHERE " + FillMeDbHelper.COLUMN_ID + " = " + id;
        cursor = db.rawQuery(sql, null);
        Log.d(logTag, "Eintrag wurden erfolgreich aus der Datenbanktabelle " + FillMeDbHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;
        m = cursorToEntry(cursor);

        cursor.close();
        close();

        return m;
    }

    void updateData(FillEntry updatedItem) {
        open();

        String sql = "UPDATE " + FillMeDbHelper.TABLE_FILLENTRY +
                " SET " + FillMeDbHelper.COLUMN_DATE + " = '" + updatedItem.getDate() + "', " +
                FillMeDbHelper.COLUMN_MILEAGE + " = '" + updatedItem.getMileage() + "', " +
                FillMeDbHelper.COLUMN_LITER + " = '" + updatedItem.getLiter() + "', " +
                FillMeDbHelper.COLUMN_PRICE + " = '" + updatedItem.getPrice() + "', " +
                FillMeDbHelper.COLUMN_STATUS + " = '" + updatedItem.getStatus() +
                "' WHERE " + FillMeDbHelper.COLUMN_ID + " = " + updatedItem.getID();

        db.execSQL(sql);

        close();
    }

    void deleteDataById(int entryId) {
        open();

        String sql = "DELETE " +
                " FROM " + FillMeDbHelper.TABLE_FILLENTRY +
                " WHERE " + FillMeDbHelper.COLUMN_ID + " = " + entryId;

        db.execSQL(sql);

        close();
    }

    public void deleteAllData()
    {
        open();
        db.execSQL("delete from "+ FillMeDbHelper.TABLE_FILLENTRY);
        close();
    }

    private FillEntry cursorToEntry(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(FillMeDbHelper.COLUMN_ID);
        int dateIndex = cursor.getColumnIndex(FillMeDbHelper.COLUMN_DATE);
        int mileageIndex = cursor.getColumnIndex(FillMeDbHelper.COLUMN_MILEAGE);
        int literIndex = cursor.getColumnIndex(FillMeDbHelper.COLUMN_LITER);
        int priceIndex = cursor.getColumnIndex(FillMeDbHelper.COLUMN_PRICE);
        int statusIndex = cursor.getColumnIndex(FillMeDbHelper.COLUMN_STATUS);

        int id = cursor.getInt(idIndex);
        String date = cursor.getString(dateIndex);
        int mileage = cursor.getInt(mileageIndex);
        double liter = cursor.getDouble(literIndex);
        double price = cursor.getDouble(priceIndex);
        int status = cursor.getInt(statusIndex);

        return new FillEntry(id, date, mileage, liter, price, status);
    }
}
