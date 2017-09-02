package de.me.fill.mblum.android.fillme;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by mblum on 11.07.2017.
 */

public class FillMeDbHelper extends SQLiteOpenHelper {

    private String logTag = "FillMeDbHelper";

    public static final String DB_NAME = "databaseVI.db"; //später ,wenn funktionsbereit: database.db
    public static final int DB_VERSION = 1;
    public static final String TABLE_FILLENTRY = "fillentries"; //entries
    public static final String COLUMN_ID = "fill_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MILEAGE = "mileage";
    public static final String COLUMN_LITER = "liter";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_STATUS = "status";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_FILLENTRY + "(" +
                    COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_MILEAGE + " INTEGER NOT NULL, " +
                    COLUMN_LITER + " REAL NOT NULL, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_STATUS + " INTEGER NOT NULL)"; // 1 = Usereingabe | 0 = Generiert

    public FillMeDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Log.d(logTag,"Tabelle wird mit folgenden SQL-Befehl erstellt: " + SQL_CREATE);
            sqLiteDatabase.execSQL(SQL_CREATE);
        } catch (Exception ex) {
            Log.d(logTag,"Anlegen der Tabelle fehlgeschlagen: " + ex.getMessage());
        }

        Log.d(logTag,"Datenbank " + DB_NAME + " wurde erfolgreich erstellt.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
