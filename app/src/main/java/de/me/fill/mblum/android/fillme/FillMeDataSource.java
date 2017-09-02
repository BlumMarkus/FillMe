package de.me.fill.mblum.android.fillme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by mblum on 11.07.2017.
 */

public class FillMeDataSource {
    private SQLiteDatabase db;
    private FillMeDbHelper dbHelper;
    private FillEntry o;
    private String logTag = "FillMeDataSource";

    public FillMeDataSource(Context context) {
        dbHelper = new FillMeDbHelper(context);
        Log.d(logTag,"DataSource: DbHelper wurde erstellt.");
    }

    public void open() {
        Log.d(logTag,"Referenz auf die Datenbank wird nun angefragt.");
        db = dbHelper.getWritableDatabase();
        Log.d(logTag,"Datenbankreferenz erhalten. Pfad der Datenbank: " + db.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(logTag,"Datenbank mit Hilfe des dbHelper geschlossen.");
    }

    public boolean writeEntry(FillEntry m) {
        o = new FillEntry(9,null,9,9.9,9.9,9);
        this.o = m;
        Log.d(logTag,"Übergebenens Objekt (m) wurde mit Objekt (o) überschrieben und hat nun folgende Werte:");
        Log.d(logTag,"Wert für Datum: " + o.getDate() + ", Wert Mileage: " + o.getMileage() + ", Wert Liter: " + o.getLiter() + ", Wert Price: " + o.getPrice() + ", Wert status " + o.getStatus() + ".");

        open();

        ContentValues values = new ContentValues();

        values.put(dbHelper.COLUMN_DATE, o.getDate());
        values.put(dbHelper.COLUMN_MILEAGE, o.getMileage());
        values.put(dbHelper.COLUMN_LITER, o.getLiter());
        values.put(dbHelper.COLUMN_PRICE, o.getPrice());
        values.put(dbHelper.COLUMN_STATUS, o.getStatus());

        Log.d(logTag,"Value erstellt: " + dbHelper.COLUMN_DATE + " mit " + o.getDate() + ".");
        Log.d(logTag,"Value erstellt: " + dbHelper.COLUMN_MILEAGE + " mit " + o.getMileage() + ".");
        Log.d(logTag,"Value erstellt: " + dbHelper.COLUMN_LITER + " mit " + o.getLiter() + ".");
        Log.d(logTag,"Value erstellt: " + dbHelper.COLUMN_PRICE + " mit " + o.getPrice() + ".");
        Log.d(logTag, "Value erstellt: " + dbHelper.COLUMN_STATUS + " mit " + o.getStatus() + ".");

        long result = db.insert(dbHelper.TABLE_FILLENTRY, null, values);

        if ( result == -1 ) {
            Log.d(logTag,"Das schreiben der Values (" + values + ") in die Datenbanktabelle " + dbHelper.TABLE_FILLENTRY + " ist fehlgeschlagen!");
            close();
            return false;
        } else {
            Log.d(logTag,"Values (" + values + ") wurden erfolgreich in die Datenbanktabelle " + dbHelper.TABLE_FILLENTRY + " geschrieben (FILL_ID = " + result + ").");
            close();
            return true;
        }
    }

    public ArrayList<FillEntry> getlastMonth(int month, int year) {
        Cursor cursor;
        ArrayList<FillEntry> list = new ArrayList<>();

        open();

        String sql = "SELECT " +
                dbHelper.COLUMN_ID + ", " +
                dbHelper.COLUMN_DATE + ", " +
                dbHelper.COLUMN_MILEAGE + ", " +
                dbHelper.COLUMN_LITER + ", " +
                dbHelper.COLUMN_PRICE + ", " +
                dbHelper.COLUMN_STATUS +
                " FROM " + dbHelper.TABLE_FILLENTRY +
                " WHERE " + dbHelper.COLUMN_DATE + " LIKE '%.%" + month + "." + year + "'" +
                " ORDER BY " + dbHelper.COLUMN_MILEAGE + " DESC ";
        Log.d(logTag, sql);
        cursor = db.rawQuery(sql, null);
        Log.d(logTag,"Eintrag wurden erfolgreich aus der Datenbanktabelle " + dbHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;

        while (!cursor.isAfterLast()) {
            m = cursorToEntry(cursor);
            list.add(m);
            Log.d(logTag,"Objekt m wurde der Liste hinzugefügt.");
            Log.d(logTag,"Datum: " + m.getDate() + ", Mileage: " + m.getMileage() + ", Liter: " + m.getLiter() + ", Price: " + m.getPrice() + ")");
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    public ArrayList<FillEntry> getlastYear(int year) {
        Cursor cursor;
        ArrayList<FillEntry> list = new ArrayList<>();

        open();

        String sql = "SELECT " +
                dbHelper.COLUMN_ID + ", " +
                dbHelper.COLUMN_DATE + ", " +
                dbHelper.COLUMN_MILEAGE + ", " +
                dbHelper.COLUMN_LITER + ", " +
                dbHelper.COLUMN_PRICE + ", " +
                dbHelper.COLUMN_STATUS +
                " FROM " + dbHelper.TABLE_FILLENTRY +
                " WHERE " + dbHelper.COLUMN_DATE + " LIKE '%." + year + "'" +
                " ORDER BY " + dbHelper.COLUMN_MILEAGE + " DESC ";
        cursor = db.rawQuery(sql, null);
        Log.d(logTag,"Eintrag wurden erfolgreich aus der Datenbanktabelle " + dbHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;

        while (!cursor.isAfterLast()) {
            m = cursorToEntry(cursor);
            list.add(m);
            Log.d(logTag,"Objekt m wurde der Liste hinzugefügt.");
            Log.d(logTag,"Datum: " + m.getDate() + ", Mileage: " + m.getMileage() + ", Liter: " + m.getLiter() + ", Price: " + m.getPrice() + ")");
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    public ArrayList<FillEntry> getAllEntries() {
        Cursor cursor;
        ArrayList<FillEntry> list = new ArrayList<>();

        open();

        String sql = "SELECT " +
                dbHelper.COLUMN_ID + ", " +
                dbHelper.COLUMN_DATE + ", " +
                dbHelper.COLUMN_MILEAGE + ", " +
                dbHelper.COLUMN_LITER + ", " +
                dbHelper.COLUMN_PRICE + ", " +
                dbHelper.COLUMN_STATUS +
                " FROM " + dbHelper.TABLE_FILLENTRY +
                " ORDER BY " + dbHelper.COLUMN_MILEAGE + " DESC ";
        cursor = db.rawQuery(sql, null);
        Log.d(logTag,"Eintrag wurden erfolgreich aus der Datenbanktabelle " + dbHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;

        while (!cursor.isAfterLast()) {
            m = cursorToEntry(cursor);
            list.add(m);
            Log.d(logTag,"Objekt m wurde der Liste hinzugefügt.");
            Log.d(logTag,"ID: " + m.getID() + ", Datum: " + m.getDate() + ", Mileage: " + m.getMileage() + ", Liter: " + m.getLiter() + ", Price: " + m.getPrice() + "Status: " + m.getStatus() + ")");
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    public FillEntry getEntriesById(int id) {
        Cursor cursor;

        open();

        String sql = "SELECT " +
                dbHelper.COLUMN_ID + ", " +
                dbHelper.COLUMN_DATE + ", " +
                dbHelper.COLUMN_MILEAGE + ", " +
                dbHelper.COLUMN_LITER + ", " +
                dbHelper.COLUMN_PRICE + ", " +
                dbHelper.COLUMN_STATUS +
                " FROM " + dbHelper.TABLE_FILLENTRY +
                " WHERE " + dbHelper.COLUMN_ID + " = " + id;
        cursor = db.rawQuery(sql, null);
        Log.d(logTag,"Eintrag wurden erfolgreich aus der Datenbanktabelle " + dbHelper.TABLE_FILLENTRY + " ausgelesen.");

        cursor.moveToFirst();
        FillEntry m;
        m = cursorToEntry(cursor);

        cursor.close();
        close();

        return m;
    }

    public void updateData (FillEntry updatedItem) {
        open();

        String sql = "UPDATE " + dbHelper.TABLE_FILLENTRY +
                " SET " + dbHelper.COLUMN_DATE + " = '" + updatedItem.getDate() + "', " +
                dbHelper.COLUMN_MILEAGE + " = '" + updatedItem.getMileage() + "', " +
                dbHelper.COLUMN_LITER + " = '" + updatedItem.getLiter() + "', " +
                dbHelper.COLUMN_PRICE + " = '" + updatedItem.getPrice() + "', " +
                dbHelper.COLUMN_STATUS + " = '" + updatedItem.getStatus() +
                "' WHERE " + dbHelper.COLUMN_ID + " = " + updatedItem.getID();

        db.execSQL(sql);

        close();
    }

    public void deleteDataById (int entryId) {
        open();

        String sql = "DELETE " +
                " FROM " + dbHelper.TABLE_FILLENTRY +
                " WHERE " + dbHelper.COLUMN_ID + " = " + entryId;

        db.execSQL(sql);

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

        FillEntry m = new FillEntry(id, date, mileage, liter, price, status);

        return m;
    }
}
