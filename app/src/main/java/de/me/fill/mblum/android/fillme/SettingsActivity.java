package de.me.fill.mblum.android.fillme;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SettingsActivity extends AppCompatActivity {

    private TextView tv_Import_Data;
    private TextView tv_Export_Data;

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
    }

    private void import_Data()
    {

    }

    private void export_Data()
    {
    }
}
