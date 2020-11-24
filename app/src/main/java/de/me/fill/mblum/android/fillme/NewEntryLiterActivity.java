package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewEntryLiterActivity extends AppCompatActivity {

    private FillEntryDataSource fillEntryDataSource;

    private int year;
    private int month;
    private int dayOfMonth;
    private int mileage;
    private double literPrice;

    private EditText editText_liter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry_liter);

        editText_liter = findViewById(R.id.editText_newEntry_liter);

        // Get current saved variables
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // NewEntryDate variables
            year = extras.getInt("newEntryDateYear");
            month = extras.getInt("newEntryDateMonth");
            dayOfMonth = extras.getInt("newEntryDateDay");
            mileage = extras.getInt("newEntryMileage");
            literPrice = extras.getDouble("newEntryPrice");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        fillEntryDataSource = new FillEntryDataSource(this);

        ImageButton btn_cancel = findViewById(R.id.btn_newEntryLiter_cancel);
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_next = findViewById(R.id.btn_newEntryLiter_next);
        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String literString = editText_liter.getText().toString();

                if (literString.equals("")) {
                    Toast toast = Toast.makeText(NewEntryLiterActivity.this, "Die Literanzahl darf nicht leer sein.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    double liter = Double.parseDouble(literString);

                    String date = dayOfMonth + "." + month + "." + year;
                    double price = literPrice * liter;

                    int now = (int) Calendar.getInstance().getTime().getTime();
                    FillEntry fillEntryObject = new FillEntry(date, mileage, liter, price, 1, now);
                    fillEntryDataSource.writeEntry(fillEntryObject);

                    Intent intent = new Intent(NewEntryLiterActivity.this, MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_out_top_bottom);
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        // Animation for closing activity
        overridePendingTransition(R.anim.slide_in_left_center, R.anim.slide_out_center_right);
    }
}
