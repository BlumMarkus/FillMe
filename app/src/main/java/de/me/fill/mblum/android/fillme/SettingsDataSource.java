package de.me.fill.mblum.android.fillme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class SettingsDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    static final String SETTING_LITERFORMAT = "setting_literFormat";
    static final String VALUE_LITERFORMAT_LITERPRICE_DEFAULT = "0";
    static final String VALUE_LITERFORMAT_FULLPRICE = "1";
    static final String DESCRIPTION_LITERFORMAT_LITERPRICE_DEFAULT = "Literpreis";
    static final String DESCRIPTION_LITERFORMAT_FULLPRICE = "Gesamtpreis";

    static final String SETTING_MENUSTATISTIC = "setting_menuStatistic";
    static final String VALUE_MENUSTATISTIC_CONSUMPTION_DEFAULT = "0";
    static final String DESCRIPTION_MENUSTATISTIC_CONSUMPTION_DEFAULT = "Verbrauch auf 100 km";

    static final String SETTING_LASTACTIVITY_NUMBEROFENTRIES = "setting_lastActivity_numberOfEntries";
    static final String VALUE_LASTACTIVITY_NUMBEROFENTRIES_3_DEFAULT = "0";
    static final String VALUE_LASTACTIVITY_NUMBEROFENTRIES_1 = "1";
    static final String VALUE_LASTACTIVITY_NUMBEROFENTRIES_5 = "2";
    static final String DESCRIPTION_LASTACTIVITY_NUMBEROFENTRIES_3_DEFAULT = "3";
    static final String DESCRIPTION_LASTACTIVITY_NUMBEROFENTRIES_1 = "1";
    static final String DESCRIPTION_LASTACTIVITY_NUMBEROFENTRIES_5 = "5";

    SettingsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Create Database reference.
     */
    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close database reference.
     */
    private void close() {
        dbHelper.close();
    }

    /**
     * Writes a new object object into the database.
     *
     * @param settingObject The setting object
     * @return if successful
     */
    boolean writeSetting(Setting settingObject) {
        open();

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.SETTINGS_COLUMN_NAME, settingObject.getName());
        values.put(DatabaseHelper.SETTINGS_COLUMN_VALUE, settingObject.getValue());

        long result = database.insert(DatabaseHelper.TABLE_SETTINGS, null, values);

        if (result == -1) {
            close();
            return false;
        } else {
            close();
            return true;
        }
    }

    /**
     * Returns all settings of the database table.
     *
     * @return settings array list
     */
    ArrayList<Setting> getAllSettings() {
        Cursor cursor;
        ArrayList<Setting> list = new ArrayList<>();

        open();

        String sql = "SELECT " +
                DatabaseHelper.SETTINGS_COLUMN_ID + ", " +
                DatabaseHelper.SETTINGS_COLUMN_NAME + ", " +
                DatabaseHelper.SETTINGS_COLUMN_VALUE +
                " FROM " + DatabaseHelper.TABLE_SETTINGS + ";";
        cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        Setting settingObject;

        while (!cursor.isAfterLast()) {
            settingObject = cursorToEntry(cursor);
            list.add(settingObject);
            cursor.moveToNext();
        }
        cursor.close();
        close();

        return list;
    }

    /**
     * Returns setting object by given name.
     *
     * @param name Given name
     * @return Setting object
     */
    Setting getByName(String name) {
        Cursor cursor;

        open();

        String sql = "SELECT " +
                DatabaseHelper.SETTINGS_COLUMN_ID + ", " +
                DatabaseHelper.SETTINGS_COLUMN_NAME + ", " +
                DatabaseHelper.SETTINGS_COLUMN_VALUE +
                " FROM " + DatabaseHelper.TABLE_SETTINGS +
                " WHERE " + DatabaseHelper.SETTINGS_COLUMN_NAME + " = '" + name + "';";
        cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        Setting settingObject = cursorToEntry(cursor);

        cursor.close();
        close();

        return settingObject;
    }

    /**
     * Returns setting object with given id.
     *
     * @param id Setting id
     * @return Setting object
     */
    Setting getById(int id) {
        Cursor cursor;

        open();

        String sql = "SELECT " +
                DatabaseHelper.SETTINGS_COLUMN_ID + ", " +
                DatabaseHelper.SETTINGS_COLUMN_NAME + ", " +
                DatabaseHelper.SETTINGS_COLUMN_VALUE +
                " FROM " + DatabaseHelper.TABLE_SETTINGS +
                " WHERE " + DatabaseHelper.SETTINGS_COLUMN_ID + " = " + id + ";";
        cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        Setting settingObject = cursorToEntry(cursor);

        cursor.close();
        close();

        return settingObject;
    }

    /**
     * Updates given setting object.
     *
     * @param settingObject Setting object
     */
    void updateByObject(Setting settingObject) {
        open();

        String sql = "UPDATE " + DatabaseHelper.TABLE_SETTINGS +
                " SET " + DatabaseHelper.SETTINGS_COLUMN_VALUE + " = '" + settingObject.getValue() +
                "' WHERE " + DatabaseHelper.SETTINGS_COLUMN_ID + " = " + settingObject.getId();

        database.execSQL(sql);

        close();
    }

    /**
     * Returns the setting object of the given cursor.
     *
     * @param cursor Cursor
     * @return Setting object
     */
    private Setting cursorToEntry(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DatabaseHelper.SETTINGS_COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(DatabaseHelper.SETTINGS_COLUMN_NAME);
        int valueIndex = cursor.getColumnIndex(DatabaseHelper.SETTINGS_COLUMN_VALUE);

        int id = cursor.getInt(idIndex);
        String name = cursor.getString(nameIndex);
        String value = cursor.getString(valueIndex);

        String[] choices = getChoicesByName(name);

        return new Setting(id, name, value, choices);
    }

    /**
     * Returns the choices of the given setting based on the setting name
     *
     * @param name Setting name
     * @return choices string array
     */
    private String[] getChoicesByName(String name) {
        String[] choices;

        switch (name) {
            case SETTING_LITERFORMAT:
                choices = new String[2];
                choices[Integer.parseInt(VALUE_LITERFORMAT_LITERPRICE_DEFAULT)] = DESCRIPTION_LITERFORMAT_LITERPRICE_DEFAULT;
                choices[Integer.parseInt(VALUE_LITERFORMAT_FULLPRICE)] = DESCRIPTION_LITERFORMAT_FULLPRICE;
                break;
            case SETTING_MENUSTATISTIC:
                choices = new String[1];
                choices[Integer.parseInt(VALUE_MENUSTATISTIC_CONSUMPTION_DEFAULT)] = DESCRIPTION_MENUSTATISTIC_CONSUMPTION_DEFAULT;
                break;
            case SETTING_LASTACTIVITY_NUMBEROFENTRIES:
                choices = new String[3];
                choices[Integer.parseInt(VALUE_LASTACTIVITY_NUMBEROFENTRIES_3_DEFAULT)] = DESCRIPTION_LASTACTIVITY_NUMBEROFENTRIES_3_DEFAULT;
                choices[Integer.parseInt(VALUE_LASTACTIVITY_NUMBEROFENTRIES_1)] = DESCRIPTION_LASTACTIVITY_NUMBEROFENTRIES_1;
                choices[Integer.parseInt(VALUE_LASTACTIVITY_NUMBEROFENTRIES_5)] = DESCRIPTION_LASTACTIVITY_NUMBEROFENTRIES_5;
                break;
            default:
                choices = new String[]{};
                break;
        }

        return choices;
    }
}
