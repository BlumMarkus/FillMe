package de.me.fill.mblum.android.fillme;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class newEntryActivity extends AppCompatActivity {

    FillEntry fillEntry;
    FillMeDataSource fmds;
    private String logTag = "newEntryActivity";

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView tv_date;

    private int yearCal;
    private int monthCal;
    private int dayCal;
    private String date;

    private int status; // 1 = Usereingabe | 0 = generiert
    private int mileage;
    private double amount;
    private double liter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry);
        fmds = new FillMeDataSource(this);

        Button btn_accept = (Button) findViewById(R.id.btn_accept);
        Button btn_selectDate = (Button) findViewById(R.id.btn_SelectDate);

        tv_date = (TextView) findViewById(R.id.txt_Date);

        btn_selectDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                yearCal = cal.get(Calendar.YEAR);
                monthCal = cal.get(Calendar.MONTH);
                dayCal = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(newEntryActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,mDateSetListener,yearCal,monthCal,dayCal);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                date = String.format("%02d", dayOfMonth) + "." + String.format("%02d", month) + "." + String.format("%04d", year);
                tv_date.setText(date);
                Log.d(logTag,"Datum an TextView übergeben: Jahr: " + year + ", Monat: " + month + ", Tag: " + dayOfMonth);
            }
        };

        btn_accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String workerDate = tv_date.getText().toString();
                if (workerDate == "") {
                    Toast.makeText(newEntryActivity.this, "Datumfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                    Log.d(logTag, "Usereingabe im Feld 'tv_date' nicht erfüllt!");
                } else {
                    Log.d(logTag, workerDate + " wurde erfolgreich aus dem TextView tv_date übertragen.");

                    EditText input_mileage = (EditText) findViewById(R.id.input_Mileage);
                    String workerMileage = input_mileage.getText().toString();
                    if ( workerMileage == "" ) {
                        Toast.makeText(newEntryActivity.this, "Kilometerstand darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                        Log.d(logTag, "Usereingabe im Feld 'input_Mileage' nicht erfüllt!");
                    } else {
                        Log.d(logTag, workerMileage + " wurde erfolgreich aus dem EditText input_mileage übertragen.");

                        EditText input_amount = (EditText) findViewById(R.id.input_Amount);
                        String workerAmount = input_amount.getText().toString();
                        if ( workerAmount == "" ) {
                            Toast.makeText(newEntryActivity.this, "Betrag darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                            Log.d(logTag, "Usereingabe im Feld 'input_Amount' nicht erfüllt!");
                        } else {
                            Log.d(logTag, workerAmount + " wurde erfolgreich aus dem EditText input_amount übertragen.");

                            EditText input_liter = (EditText) findViewById(R.id.input_Liter);
                            String workerLiter = input_liter.getText().toString();
                            if ( workerLiter == "" ) {
                                Toast.makeText(newEntryActivity.this, "Literfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                                Log.d(logTag, "Usereingabe im Feld 'input_Liter' nicht erfüllt!");
                            } else {
                                Log.d(logTag, workerLiter + " wurde erfolgreich aus dem EditText input_liter übertragen.");

                                mileage = Integer.parseInt(workerMileage);
                                amount = Double.parseDouble(workerAmount);
                                liter = Double.parseDouble(workerLiter);
                                status = 1; // = Usereingabe

                                fillEntry = new FillEntry(date, mileage, liter, amount, status);
                                Log.d(logTag, "Neues Objekt fillEntry (" + date + "," + mileage + "," + liter + "," + amount + ", " + status +") erstellt.");
                                boolean result = fmds.writeEntry(fillEntry);

                                if (result == true) {
                                    Toast.makeText(newEntryActivity.this, "Datenbankeintrag erfolgreich", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(newEntryActivity.this, "Datenbankeintrag fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                }

                                Log.d(logTag,"Activity wird nun geschlossen.");
                                finish();
                            }
                        }
                    }
                }
            }
        });
    }
}
