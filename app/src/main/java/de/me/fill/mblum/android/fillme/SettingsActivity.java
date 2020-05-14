package de.me.fill.mblum.android.fillme;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private FillMeDataSource dataSource;
    private StringBuilder data;

    public static final int rquestcode = 1;

    public Intent selectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageButton btn_settings_cancel = findViewById(R.id.btn_settings_cancel);
        Button btn_settings_exportData = findViewById(R.id.btn_settings_exportData);
        Button btn_settings_importData = findViewById(R.id.btn_settings_importData);

        dataSource = new FillMeDataSource(this);

        btn_settings_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_settings_exportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportData();
            }
        });

        btn_settings_importData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData();
            }
        });
    }

    @Override
    protected void onActivityResult(int rquestcode, int resultCode, Intent intent) {
        super.onActivityResult(resultCode, resultCode, intent);

        if (intent == null)
            return;
        if (rquestcode == 1) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                generateCSVOnSD(this, "Export_Database.csv", data);
            case 1:
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("*/*");
                startActivityForResult(fileIntent, rquestcode);
        }
    }

    /**
     * Import database data from installed SD-Card
     */
    private void importData() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("text/*");
                startActivityForResult(fileIntent, rquestcode);
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            Toast.makeText(this, "Sie benötigen ein SD-Karte", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Export database data as csv to Downloads/FillMe_Database folder
     */
    private void exportData() {
        data = new StringBuilder();
        ArrayList<FillEntry> list = dataSource.getAllEntries(false);

        //Header
        data.append(String.format("%s,%s,%s,%s,%s", FillMeDbHelper.FILLENTRY_COLUMN_DATE, FillMeDbHelper.FILLENTRY_COLUMN_MILEAGE, FillMeDbHelper.FILLENTRY_COLUMN_LITER, FillMeDbHelper.FILLENTRY_COLUMN_PRICE, FillMeDbHelper.FILLENTRY_COLUMN_STATUS));

        //data
        for (FillEntry entry : list) {
            String fillEntryString = String.format("\n%s,%s,%s,%s,%s", entry.getStringDate(), entry.getMileage(), entry.getLiter(), entry.getPrice(), entry.getStatus());
            data.append(fillEntryString);
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                generateCSVOnSD(this, "Export_Database.csv", data);
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        } else {
            Toast.makeText(this, "Sie benötigen ein SD-Karte", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates the .csv file and fills it with the given data.
     *
     * @param context   Current context
     * @param sFileName Name of the csv file
     * @param sBody     Content of the file
     */
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
            Toast.makeText(context, "Datenbank wurde in " + '"' + "Downloads/FillMe_Database" + '"' + " exportiert!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAndImportFile(Intent intent) {

        if (intent == null)
            return;

        try {
            Uri uri = intent.getData();
            String filePath = uri.getPath();
            filePath = filePath.substring(filePath.indexOf(":") + 1);

            // Quick and dirty...
            if (Build.VERSION.SDK_INT < 29) {
                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath().replace('/' + filePath, "");
                filePath = absolutePath + '/' + filePath;
            }

            File csvFile = new File(filePath);
            FileReader reader = new FileReader(csvFile);
            BufferedReader buffer = new BufferedReader(reader);

            dataSource.deleteAllData();

            String line;
            buffer.readLine();

            while ((line = buffer.readLine()) != null) {
                String[] tokens = line.split(",");
                FillEntry fillEntry = new FillEntry(tokens[0], Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Integer.parseInt(tokens[4]));
                dataSource.writeEntry(fillEntry);
            }
            Toast.makeText(this, "Datenbank wurde erfolreich Importiert!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
