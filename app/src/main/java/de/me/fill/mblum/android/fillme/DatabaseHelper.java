package de.me.fill.mblum.android.fillme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String logTag = "FillMeDbHelper";

    private static final String DB_NAME = "databaseVI.db"; //später ,wenn funktionsbereit: database.db
    private static final int DB_VERSION = 2;

    static final String TABLE_FILLENTRY = "fillentries"; //entries
    static final String FILLENTRY_COLUMN_ID = "fill_id";
    static final String FILLENTRY_COLUMN_DATE = "date";
    static final String FILLENTRY_COLUMN_MILEAGE = "mileage";
    static final String FILLENTRY_COLUMN_LITER = "liter";
    static final String FILLENTRY_COLUMN_PRICE = "price";
    static final String FILLENTRY_COLUMN_STATUS = "status";
    static final String FILLENTRY_COLUMN_LASTCHANGED = "last_changed";

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
                    FILLENTRY_COLUMN_STATUS + " INTEGER NOT NULL, " +
                    FILLENTRY_COLUMN_LASTCHANGED + " INTEGER NOT NULL)";

    DatabaseHelper(Context context) {
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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int currentVersion, int newVersion) {

        if (currentVersion == 1 && newVersion == 2) {
            // Added new table named settings with columns idSetting, name, value
            // Added new column for table fillEntry

            long now = Calendar.getInstance().getTime().getTime();

            String sqlNewTableAndColumns =
                    "CREATE TABLE " + TABLE_SETTINGS + "(" +
                            SETTINGS_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                            SETTINGS_COLUMN_NAME + " TEXT NOT NULL, " +
                            SETTINGS_COLUMN_VALUE + " TEXT NOT NULL);";

            String sqlNewColumn =
                    "ALTER TABLE " + TABLE_FILLENTRY +
                            " ADD " + FILLENTRY_COLUMN_LASTCHANGED + " INTEGER NOT NULL DEFAULT " + now + ";";

            try {
                Log.d(logTag, "Tabellenupgrade v1 auf v2 wird mit folgenden SQL-Befehl durchgeführt: " + sqlNewTableAndColumns);
                sqLiteDatabase.execSQL(sqlNewTableAndColumns);
                Log.d(logTag, "Tabellenupgrade v1 auf v2 wird mit folgenden SQL-Befehl durchgeführt: " + sqlNewColumn);
                sqLiteDatabase.execSQL(sqlNewColumn);
                Log.d(logTag, "Tabellenupgrade v1 auf v2 wurde durchgeführt.");
            } catch (Exception ex) {
                Log.d(logTag, "Anlegen der Tabelle fehlgeschlagen: " + ex.getMessage());
            }
        }
    }
}
