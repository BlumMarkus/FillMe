package de.me.fill.mblum.android.fillme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mblum on 11.07.2017.
 */

public class FillMeDbHelper extends SQLiteOpenHelper {

    private String logTag = "FillMeDbHelper";

    private static final String DB_NAME = "databaseVI.db"; //später ,wenn funktionsbereit: database.db
    private static final int DB_VERSION = 1;

    static final String TABLE_FILLENTRY = "fillentries"; //entries
    static final String FILLENTRY_COLUMN_ID = "fill_id";
    static final String FILLENTRY_COLUMN_DATE = "date";
    static final String FILLENTRY_COLUMN_MILEAGE = "mileage";
    static final String FILLENTRY_COLUMN_LITER = "liter";
    static final String FILLENTRY_COLUMN_PRICE = "price";
    static final String FILLENTRY_COLUMN_STATUS = "status";

    static final String TABLE_SETTINGS = "settings";
    static final String SETTINGS_COLUMN_ID = "idSettings";
    static final String SETTINGS_COLUMN_NAME = "name";
    static final String SETTINGS_COLUMN_VALUE = "value";

    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_FILLENTRY + "(" +
                    FILLENTRY_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    FILLENTRY_COLUMN_DATE + " TEXT NOT NULL, " +
                    FILLENTRY_COLUMN_MILEAGE + " INTEGER NOT NULL, " +
                    FILLENTRY_COLUMN_LITER + " REAL NOT NULL, " +
                    FILLENTRY_COLUMN_PRICE + " REAL NOT NULL, " +
                    FILLENTRY_COLUMN_STATUS + " INTEGER NOT NULL)"; // 1 = Usereingabe | 0 = Generiert

    FillMeDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Log.d(logTag, "Tabelle wird mit folgenden SQL-Befehl erstellt: " + SQL_CREATE);
            sqLiteDatabase.execSQL(SQL_CREATE);
        } catch (Exception ex) {
            Log.d(logTag, "Anlegen der Tabelle fehlgeschlagen: " + ex.getMessage());
        }

        Log.d(logTag, "Datenbank " + DB_NAME + " wurde erfolgreich erstellt.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
