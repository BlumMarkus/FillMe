package de.me.fill.mblum.android.fillme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String logTag = "FillMeDbHelper";

    private static final String DB_NAME = "databaseVI.db"; //später ,wenn funktionsbereit: database.db
    private static final int DB_VERSION = 3;

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

    private static final String SQL_CREATE_FILLENTRY =
            "CREATE TABLE " + TABLE_FILLENTRY + "(" +
                    FILLENTRY_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    FILLENTRY_COLUMN_DATE + " TEXT NOT NULL, " +
                    FILLENTRY_COLUMN_MILEAGE + " INTEGER NOT NULL, " +
                    FILLENTRY_COLUMN_LITER + " REAL NOT NULL, " +
                    FILLENTRY_COLUMN_PRICE + " REAL NOT NULL, " +
                    FILLENTRY_COLUMN_STATUS + " INTEGER NOT NULL, " +
                    FILLENTRY_COLUMN_LASTCHANGED + " INTEGER NOT NULL)";

    private static final String SQL_CREATE_SETTINGS =
            "CREATE TABLE " + TABLE_SETTINGS + "(" +
                    SETTINGS_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    SETTINGS_COLUMN_NAME + " TEXT NOT NULL, " +
                    SETTINGS_COLUMN_VALUE + " TEXT NOT NULL);";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(SQL_CREATE_FILLENTRY);
            sqLiteDatabase.execSQL(SQL_CREATE_SETTINGS);

            setInitialDefaultSettings(sqLiteDatabase);
        } catch (Exception ex) {
            Log.d(logTag, "Anlegen der Tabelle fehlgeschlagen: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int currentVersion, int newVersion) {
        if (currentVersion < 2) {
            // Added new table named settings with columns idSetting, name, value
            // Added new column for table fillEntry
            version2(sqLiteDatabase);
        }

        if (currentVersion < 3) {
            // Added literFormat to settings.
            version3(sqLiteDatabase);
        }
    }

    /**
     * Fills the settings table with setting values
     *
     * @param sqLiteDatabase database
     */
    private void setInitialDefaultSettings(SQLiteDatabase sqLiteDatabase) {
        String sqlDefaultSettings =
                "INSERT INTO " + TABLE_SETTINGS + " (" + SETTINGS_COLUMN_NAME + ", " + SETTINGS_COLUMN_VALUE + ")" +
                        " VALUES " +
                        "('" + SettingsDataSource.SETTING_LITERFORMAT + "', '" + SettingsDataSource.VALUE_LITERFORMAT_LITERPRICE_DEFAULT + "')," +
                        "('" + SettingsDataSource.SETTING_MENUSTATISTIC + "', '" + SettingsDataSource.VALUE_MENUSTATISTIC_CONSUMPTION_DEFAULT + "')," +
                        "('" + SettingsDataSource.SETTING_LASTACTIVITY_NUMBEROFENTRIES + "', '" + SettingsDataSource.VALUE_LASTACTIVITY_NUMBEROFENTRIES_3_DEFAULT + "');";

        try {
            sqLiteDatabase.execSQL(sqlDefaultSettings);
        } catch (Exception ex) {
            Log.d(logTag, "Anlegen der Default-Werte fehlgeschlagen: " + ex.getMessage());
        }
    }

    /**
     * Added new table named settings with columns idSetting, name, value
     * Added new column for table fillEntry
     *
     * @param sqLiteDatabase database
     */
    private void version2(SQLiteDatabase sqLiteDatabase) {
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
            sqLiteDatabase.execSQL(sqlNewTableAndColumns);
            sqLiteDatabase.execSQL(sqlNewColumn);
        } catch (Exception ex) {
            Log.d(logTag, "Version 2: Anlegen der Tabelle fehlgeschlagen: " + ex.getMessage());
        }
    }

    /**
     * Added literFormat to settings.
     *
     * @param sqLiteDatabase database
     */
    private void version3(SQLiteDatabase sqLiteDatabase) {
        String sqlDefaultSettings =
                "INSERT INTO " + TABLE_SETTINGS + " (" + SETTINGS_COLUMN_NAME + ", " + SETTINGS_COLUMN_VALUE + ")" +
                        " VALUES " +
                        "('" + SettingsDataSource.SETTING_LITERFORMAT + "', '" + SettingsDataSource.VALUE_LITERFORMAT_LITERPRICE_DEFAULT + "')," +
                        "('" + SettingsDataSource.SETTING_MENUSTATISTIC + "', '" + SettingsDataSource.VALUE_MENUSTATISTIC_CONSUMPTION_DEFAULT + "')," +
                        "('" + SettingsDataSource.SETTING_LASTACTIVITY_NUMBEROFENTRIES + "', '" + SettingsDataSource.VALUE_LASTACTIVITY_NUMBEROFENTRIES_3_DEFAULT + "');";

        try {
            sqLiteDatabase.execSQL(sqlDefaultSettings);
        } catch (Exception ex) {
            Log.d(logTag, "Version 3: Anlegen der Default-Werte fehlgeschlagen: " + ex.getMessage());
        }
    }
}
