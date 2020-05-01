package de.me.fill.mblum.android.fillme;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private TextView tv_Import_Data;
    private TextView tv_Export_Data;
    private FillMeDataSource fmds;
    private FillMeDbHelper fmdh;
    private StringBuilder data;
    public static final int rquestcode = 1;
    public Intent selectedFile;


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
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("text/*");
                startActivityForResult(fileIntent, rquestcode);
            }
            else {
                // Request permission from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        else {
            Toast.makeText(this, "Sie benötigen ein SD-Karte", Toast.LENGTH_SHORT).show();
        }

    }

    private void export_Data()
    {
        data = new StringBuilder();
        ArrayList<FillEntry> list = new ArrayList<>();
        list = fmds.getAllEntries(false);

        //Header
        data.append(String.format("%s,%s,%s,%s,%s", fmdh.COLUMN_DATE,fmdh.COLUMN_MILEAGE,fmdh.COLUMN_LITER,fmdh.COLUMN_PRICE, fmdh.COLUMN_STATUS));

        //data
        for (FillEntry entry : list){
            String fillEntryString = String.format("\n%s,%s,%s,%s,%s", entry.getDate(), entry.getMileage(),entry.getLiter(),entry.getPrice(),entry.getStatus());
            data.append(fillEntryString);
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                generateCSVOnSD(this, "Export_Database.csv", data);
            }
            else {
                // Request permission from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
        else {
            Toast.makeText(this, "Sie benötigen ein SD-Karte", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateCSVOnSD(Context context, String sFileName, StringBuilder sBody) {
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAndImportFile (Intent intent){
        if (intent == null)
            return;
        fmds.deleteAllData();
        ArrayList<FillEntry> fillEntryArrayList = new ArrayList<>();
        try {
            Uri uri = intent.getData();
            File myFile = new File(uri.getPath());
            String filepath = myFile.getAbsolutePath();
            filepath = filepath.replace("/document/raw:", "");

            File csvFile = new File(filepath);
            FileReader reader = new FileReader(csvFile);
            BufferedReader buffer = new BufferedReader(reader);

            String line = "";
            buffer.readLine();

            while ((line = buffer.readLine()) != null) {
                String[] tokens = line.split(",");
                FillEntry fillEntry = new FillEntry(tokens[0], Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Integer.parseInt(tokens[4]));
                fillEntryArrayList.add(fillEntry);
                fmds.writeEntry(fillEntry);
            }
            Toast.makeText(this, "Datenbank wurde erfolreich Importiert!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int rquestcode, int resultCode, Intent intent)
    {
        if (intent == null)
            return;
        switch (rquestcode) {
            case 1:
                selectedFile = intent;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warnung");
                builder.setMessage("Wollen sie wirklich fortfahren diese Datei zu importieren?\nHierbei werden alle Daten ersetzt!");
                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        readAndImportFile(selectedFile);
                    }
                });

                builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                generateCSVOnSD(this, "Export_Database.csv", data);
            case 1:
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("*/*");
                startActivityForResult(fileIntent, rquestcode);
        }
    }
}
