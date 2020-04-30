package de.me.fill.mblum.android.fillme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private TextView tv_Import_Data;
    private TextView tv_Export_Data;
    private FillMeDataSource fmds;
    private FillMeDbHelper fmdh;
    private String sqlInsertStatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tv_Import_Data = (TextView) findViewById(R.id.tv_Settings_Import_Button);
        tv_Export_Data = (TextView) findViewById(R.id.tv_Settings_Export_Button);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tv_Import_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                import_Data();
            }
        });

        tv_Export_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export_Data();
            }
        });

        fmds = new FillMeDataSource(this);
        fmdh = new FillMeDbHelper(this);
    }

    private void import_Data()
    {

    }

    private void export_Data()
    {

        sqlInsertStatement = "";
        ArrayList<FillEntry> list = new ArrayList<>();
        list = fmds.getAllEntries("ASC");

        sqlInsertStatement = CreateSQLInsertStatement(sqlInsertStatement);

        for (int i = 0; i < list.size(); i++)
        {
            if (i < list.size()-1)
            {
                sqlInsertStatement = AddValueToStatement(sqlInsertStatement, list.get(i).getDate(), list.get(i).getMileage(), list.get(i).getLiter(), list.get(i).getPrice(), list.get(i).getStatus(), false);
            }
            else
            {
                sqlInsertStatement = AddValueToStatement(sqlInsertStatement, list.get(i).getDate(), list.get(i).getMileage(), list.get(i).getLiter(), list.get(i).getPrice(), list.get(i).getStatus(), true);
            };
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                GenerateNoteOnSD(this, "Export_Database.txt", sqlInsertStatement);
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
        else
        {
            Toast.makeText(this, "Sie benötigen ein SD-Karte", Toast.LENGTH_SHORT).show();
        }
    }

    private void GenerateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "FillMe_Database");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Datenbank wurde in "+ '"'+ "Downloads/FillMe_Database" + '"' + " exportiert!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String CreateSQLInsertStatement (String statement)
    {
        statement += "INSERT INTO " + fmdh.TABLE_FILLENTRY
                + " (" + fmdh.COLUMN_DATE
                + "," + fmdh.COLUMN_MILEAGE
                + "," + fmdh.COLUMN_LITER
                + "," + fmdh.COLUMN_PRICE
                + "," + fmdh.COLUMN_STATUS + ") "
                + "VALUES ";
        return statement;
    }

    private String AddValueToStatement (String statement, String date, int mileage, Double liter, Double price, int status, Boolean isLast)
    {
        statement += "('"
                + date + "','"
                + mileage + "','"
                + liter + "','"
                + price + "','"
                + status + "')";

        if(isLast)
        {
            statement += ";";
        }
        else
        {
            statement += ",";
        }

        return statement;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                GenerateNoteOnSD(this, "Export_Database.txt", sqlInsertStatement);
        }
    }
}
