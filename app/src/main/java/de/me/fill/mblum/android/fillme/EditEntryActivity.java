package de.me.fill.mblum.android.fillme;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditEntryActivity extends AppCompatActivity {

    private final String LOG_TAG = "EditEntryActivity";

    private int clickedItemId;

    private TextView tv_date;
    private EditText tv_mileage;
    private EditText tv_amount;
    private EditText tv_liter;

    private Button btn_delete;
    private Button btn_save;
    private Button btn_datePicker;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int yearCal;
    private int monthCal;
    private int dayCal;
    private String date;

    private FillEntry fillEntry;
    private FillEntry clickedEntry;

    private FillEntryDataSource fmds;

    private AlertDialog.Builder deleteAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            clickedItemId = Integer.parseInt((String) bundle.get("clickedItemID"));
        }

        fmds = new FillEntryDataSource(this);
        clickedEntry = fmds.getEntryById(clickedItemId);

        tv_date = (TextView) findViewById(R.id.tv_Date);
        tv_mileage = (EditText) findViewById(R.id.put_Mileage);
        tv_amount = (EditText) findViewById(R.id.put_Amount);
        tv_liter = (EditText) findViewById(R.id.put_Liter);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_save = (Button) findViewById(R.id.btn_accept);
        btn_datePicker = (Button) findViewById(R.id.btn_SelectDate);

        Log.d(LOG_TAG, clickedEntry.getStringDate());

        tv_date.setText(String.valueOf(clickedEntry.getStringDate()));
        tv_mileage.setText(String.valueOf(clickedEntry.getMileage()));
        tv_amount.setText(String.valueOf(clickedEntry.getPrice()));
        tv_liter.setText(String.valueOf(clickedEntry.getLiter()));

        btn_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getDateFromTextView = tv_date.getText().toString();
                Log.d(LOG_TAG, "getDateFromTextView: " + getDateFromTextView);
                String[] splittedDateFromTextView = getDateFromTextView.split("\\.");
                Log.d(LOG_TAG, "splittedDateFromTextView Größe: " + splittedDateFromTextView.length);
                yearCal = Integer.parseInt(splittedDateFromTextView[2]);
                monthCal = Integer.parseInt(splittedDateFromTextView[1]) - 1;
                dayCal = Integer.parseInt(splittedDateFromTextView[0]);

                Log.d(LOG_TAG, "splittedDateFromTextView 0: " + splittedDateFromTextView[0] + "splittedDateFromTextView 1: " + splittedDateFromTextView[1] + "splittedDateFromTextView 2: " + splittedDateFromTextView[2]);
                Log.d(LOG_TAG, "yearCal: " + yearCal + ", monthCal: " + monthCal + ", dayCal: " + dayCal);


                DatePickerDialog dialog = new DatePickerDialog(EditEntryActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener, yearCal, monthCal, dayCal);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                date = String.format("%02d", dayOfMonth) + "." + String.format("%02d", month) + "." + String.format("%04d", year);
                tv_date.setText(date);
            }
        };


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    deleteAlert = new AlertDialog.Builder(EditEntryActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    deleteAlert = new AlertDialog.Builder(EditEntryActivity.this);
                }
                deleteAlert.setTitle("Wirklich löschen?!");
                deleteAlert.setMessage("Sind Sie sich sicher, dass Sie diesen Eintrag löschen möchten?");
                deleteAlert.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int id = clickedItemId;
                        fmds.deleteDataById(id);
                        Log.d(LOG_TAG, "Daten mit ID ( " + id + " ) erfolgreich gelöscht!");
                        Toast.makeText(EditEntryActivity.this, "Daten erfolgreich gelöscht!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                deleteAlert.setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                deleteAlert.setIcon(R.drawable.ic_warning_yellow_24dp);
                deleteAlert.show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = clickedItemId;
                String date = tv_date.getText().toString();
                int mileage = Integer.parseInt(tv_mileage.getText().toString());
                double liter = Double.parseDouble(tv_liter.getText().toString());
                double price = Double.parseDouble(tv_amount.getText().toString());
                int status = 1;

                int now = (int) Calendar.getInstance().getTime().getTime();
                fillEntry = new FillEntry(id, date, mileage, liter, price, status, now);
                fmds.updateData(fillEntry);
                Log.d(LOG_TAG, "Daten erolfgreich abgeändert");
                finish();
            }
        });

    }
}
