package de.me.fill.mblum.android.fillme;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class NewEntryActivity extends AppCompatActivity {

    FillEntry fillEntry;
    FillMeDataSource fmds;
    private String LOGTAG = "newEntryActivity";

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private int status; // 1 = Usereingabe | 0 = generiert
    private int mileage;
    private double amount;
    private double liter;

    private int currentDateYear;
    private int currentDateMonth;
    private int currentDateDayOfMonth;

    private TextView tv_newEntry_date_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button btn_newEntry_select_today = findViewById(R.id.btn_newEntry_date_today);
        Button btn_newEntry_select_date = findViewById(R.id.btn_newEntry_date_select);
        Button btn_newEntry_confirm = findViewById(R.id.btn_newEntry_confirm);
        ImageButton btn_newEntry_cancel = findViewById(R.id.btn_newEntry_cancel);
        tv_newEntry_date_field = findViewById(R.id.tv_newEntry_selected_date);

        fmds = new FillMeDataSource(this);

        Calendar currentDateCalendar = Calendar.getInstance();
        currentDateYear = currentDateCalendar.get(Calendar.YEAR);
        currentDateMonth = currentDateCalendar.get(Calendar.MONTH);
        currentDateDayOfMonth = currentDateCalendar.get(Calendar.DAY_OF_MONTH);

        btn_newEntry_select_today.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_newEntry_date_field.setText(String.format("%02d", currentDateDayOfMonth) + "." + String.format("%02d", currentDateMonth + 1) + "." + String.format("%02d", currentDateYear));
                Log.d(LOGTAG, "Datum an TextView übergeben: Jahr: " + currentDateYear + ", Monat: " + currentDateMonth + ", Tag: " + currentDateDayOfMonth);
            }
        });

        btn_newEntry_select_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewEntryActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener, currentDateYear, currentDateMonth, currentDateDayOfMonth);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                tv_newEntry_date_field.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month) + "." + String.format("%04d", year));
                Log.d(LOGTAG, "Datum an TextView übergeben: Jahr: " + year + ", Monat: " + month + ", Tag: " + dayOfMonth);
            }
        };

        btn_newEntry_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_newEntry_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input_mileage = findViewById(R.id.input_newEntry_mileage);
                EditText input_amount = findViewById(R.id.input_newEntry_cost);
                EditText input_liter = findViewById(R.id.input_newEntry_liter);

                String dateInput = tv_newEntry_date_field.getText().toString();
                String mileageInput = input_mileage.getText().toString();
                String amountInput = input_amount.getText().toString();
                String literInput = input_liter.getText().toString();

                if (dateInput.equals("")) {
                    Toast.makeText(NewEntryActivity.this, "Datumfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                    Log.d(LOGTAG, "Usereingabe im Feld 'tv_date' nicht erfüllt!");
                } else {
                    Log.d(LOGTAG, dateInput + " wurde erfolgreich aus dem TextView tv_date übertragen.");

                    if (mileageInput.equals("")) {
                        Toast.makeText(NewEntryActivity.this, "Kilometerstand darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                        Log.d(LOGTAG, "Usereingabe im Feld 'input_Mileage' nicht erfüllt!");
                    } else {
                        Log.d(LOGTAG, mileageInput + " wurde erfolgreich aus dem EditText input_mileage übertragen.");

                        if (amountInput.equals("")) {
                            Toast.makeText(NewEntryActivity.this, "Betrag darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                            Log.d(LOGTAG, "Usereingabe im Feld 'input_Amount' nicht erfüllt!");
                        } else {
                            Log.d(LOGTAG, amountInput + " wurde erfolgreich aus dem EditText input_amount übertragen.");

                            if (literInput.equals("")) {
                                Toast.makeText(NewEntryActivity.this, "Literfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                                Log.d(LOGTAG, "Usereingabe im Feld 'input_Liter' nicht erfüllt!");
                            } else {
                                Log.d(LOGTAG, literInput + " wurde erfolgreich aus dem EditText input_liter übertragen.");

                                mileage = Integer.parseInt(mileageInput);
                                amount = Double.parseDouble(amountInput);
                                liter = Double.parseDouble(literInput);
                                status = 1; // = Usereingabe

                                fillEntry = new FillEntry(dateInput, mileage, liter, amount, status);
                                Log.d(LOGTAG, "Neues Objekt fillEntry (" + dateInput + "," + mileage + "," + liter + "," + amount + ", " + status + ") erstellt.");
                                boolean result = fmds.writeEntry(fillEntry);

                                if (result) {
                                    Toast.makeText(NewEntryActivity.this, "Datenbankeintrag erfolgreich", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewEntryActivity.this, "Datenbankeintrag fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                }

                                Log.d(LOGTAG, "Activity wird nun geschlossen.");
                                finish();
                            }
                        }
                    }
                }
            }
        });
    }
}
