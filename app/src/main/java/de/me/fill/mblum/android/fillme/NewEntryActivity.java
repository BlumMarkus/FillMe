package de.me.fill.mblum.android.fillme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private Calendar actualDateCalendar;

    private int actualDateYear;
    private int actualDateMonth;
    private int actualDateDayOfMonth;
    private String date;

    private int status; // 1 = Usereingabe | 0 = generiert
    private int mileage;
    private double amount;
    private double liter;

    private TextView tv_newEntry_date_field;

    private Button btn_newEntry_select_date;
    private ImageButton btn_newEntry_save;
    private ImageButton btn_newEntry_back;

    private ImageButton btn_menu_statistic_list;
    private ImageButton btn_menu_statistic_diagram;
    private ImageButton btn_menu_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_newEntry_select_date = (Button) findViewById(R.id.btn_newEntry_date_select);
        btn_newEntry_save = (ImageButton) findViewById(R.id.btn_newEntry_save);
        btn_newEntry_back = (ImageButton) findViewById(R.id.btn_newEntry_back);

        btn_menu_statistic_list = (ImageButton) findViewById(R.id.btn_menu_show_statistic_list);
        btn_menu_statistic_diagram = (ImageButton) findViewById(R.id.btn_menu_show_statistic_diagram);

        tv_newEntry_date_field = (TextView) findViewById(R.id.tv_newEntry_date_field);

        fmds = new FillMeDataSource(this);

        btn_newEntry_select_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actualDateCalendar = Calendar.getInstance();
                actualDateYear = actualDateCalendar.get(Calendar.YEAR);
                actualDateMonth = actualDateCalendar.get(Calendar.MONTH);
                actualDateDayOfMonth = actualDateCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NewEntryActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,mDateSetListener,actualDateYear,actualDateMonth,actualDateDayOfMonth);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                date = String.format("%02d", dayOfMonth) + "." + String.format("%02d", month) + "." + String.format("%04d", year);
                tv_newEntry_date_field.setText(date);
                Log.d(LOGTAG,"Datum an TextView übergeben: Jahr: " + year + ", Monat: " + month + ", Tag: " + dayOfMonth);
            }
        };

        btn_newEntry_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String workerDate = tv_newEntry_date_field.getText().toString();

                if (workerDate.equals("")) {
                    Toast.makeText(NewEntryActivity.this, "Datumfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                    Log.d(LOGTAG, "Usereingabe im Feld 'tv_date' nicht erfüllt!");
                } else {
                    Log.d(LOGTAG, workerDate + " wurde erfolgreich aus dem TextView tv_date übertragen.");

                    EditText input_mileage = (EditText) findViewById(R.id.input_newEntry_mileage);
                    String workerMileage = input_mileage.getText().toString();
                    if ( workerMileage.equals("") ) {
                        Toast.makeText(NewEntryActivity.this, "Kilometerstand darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                        Log.d(LOGTAG, "Usereingabe im Feld 'input_Mileage' nicht erfüllt!");
                    } else {
                        Log.d(LOGTAG, workerMileage + " wurde erfolgreich aus dem EditText input_mileage übertragen.");

                        EditText input_amount = (EditText) findViewById(R.id.input_newEntry_cost);
                        String workerAmount = input_amount.getText().toString();
                        if ( workerAmount.equals("") ) {
                            Toast.makeText(NewEntryActivity.this, "Betrag darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                            Log.d(LOGTAG, "Usereingabe im Feld 'input_Amount' nicht erfüllt!");
                        } else {
                            Log.d(LOGTAG, workerAmount + " wurde erfolgreich aus dem EditText input_amount übertragen.");

                            EditText input_liter = (EditText) findViewById(R.id.input_newEntry_liter);
                            String workerLiter = input_liter.getText().toString();
                            if ( workerLiter.equals("") ) {
                                Toast.makeText(NewEntryActivity.this, "Literfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                                Log.d(LOGTAG, "Usereingabe im Feld 'input_Liter' nicht erfüllt!");
                            } else {
                                Log.d(LOGTAG, workerLiter + " wurde erfolgreich aus dem EditText input_liter übertragen.");

                                mileage = Integer.parseInt(workerMileage);
                                amount = Double.parseDouble(workerAmount);
                                liter = Double.parseDouble(workerLiter);
                                status = 1; // = Usereingabe

                                fillEntry = new FillEntry(date, mileage, liter, amount, status);
                                Log.d(LOGTAG, "Neues Objekt fillEntry (" + date + "," + mileage + "," + liter + "," + amount + ", " + status +") erstellt.");
                                boolean result = fmds.writeEntry(fillEntry);

                                if (result) {
                                    Toast.makeText(NewEntryActivity.this, "Datenbankeintrag erfolgreich", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewEntryActivity.this, "Datenbankeintrag fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                }

                                Log.d(LOGTAG,"Activity wird nun geschlossen.");
                                finish();
                            }
                        }
                    }
                }
            }
        });

        btn_newEntry_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_menu_statistic_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEntryActivity.this, ShowEntriesActivity.class);
                startActivity(intent);
            }
        });

        btn_menu_statistic_diagram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEntryActivity.this, ShowDiagramActivity.class);
                startActivity(intent);
            }
        });
    }
}
