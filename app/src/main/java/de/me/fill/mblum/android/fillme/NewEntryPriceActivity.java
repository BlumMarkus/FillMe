package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class NewEntryPriceActivity extends AppCompatActivity {

    private NumberPicker numberPicker_euro;
    private NumberPicker numberPicker_cent;
    private NumberPicker numberPicker_hundredthCent;

    private int year;
    private int month;
    private int dayOfMonth;
    private int mileage;

    String twoDigitsFormat = "%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry_price);

        numberPicker_euro = findViewById(R.id.numberPicker_newEntryPrice_euro);
        numberPicker_cent = findViewById(R.id.numberPicker_newEntryPrice_cent);
        numberPicker_hundredthCent = findViewById(R.id.numberPicker_newEntryPrice_hundredthCent);

        // set number picker configuration
        numberPicker_euro.setMinValue(0);
        numberPicker_euro.setMaxValue(10);
        numberPicker_cent.setMinValue(0);
        numberPicker_cent.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format(twoDigitsFormat, i);
            }
        });
        numberPicker_cent.setMaxValue(99);
        numberPicker_hundredthCent.setMinValue(0);
        numberPicker_hundredthCent.setMaxValue(9);

        // set number picker last inputs
        FillEntryDataSource dataSource = new FillEntryDataSource(this);
        FillEntry lastFillEntry = dataSource.getLastEntry();

        int euro;
        int cent;
        int hundredthCent;

        if (lastFillEntry != null) {
            // Use last database entry prices
            double lastFillEntryPrice = lastFillEntry.getLiterPrice();
            euro = (int) lastFillEntryPrice;
            cent = (int) ((lastFillEntryPrice - euro) * 100);
            hundredthCent = (int) ((((lastFillEntryPrice - euro) * 100) - cent) * 10);
        } else {
            // No older database entries
            euro = 1;
            cent = 10;
            hundredthCent = 9;
        }

        numberPicker_euro.setValue(euro);
        numberPicker_cent.setValue(cent);
        numberPicker_hundredthCent.setValue(hundredthCent);


        // Get current saved variables
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // NewEntryDate variables
            year = extras.getInt("newEntryDateYear");
            month = extras.getInt("newEntryDateMonth");
            dayOfMonth = extras.getInt("newEntryDateDay");
            mileage = extras.getInt("newEntryMileage");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageButton btn_cancel = findViewById(R.id.btn_newEntryPrice_cancel);
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_next = findViewById(R.id.btn_newEntryPrice_next);
        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                double literPrice = ((double) numberPicker_hundredthCent.getValue() / 1000) + ((double) numberPicker_cent.getValue() / 100) + numberPicker_euro.getValue();

                Intent intent = new Intent(NewEntryPriceActivity.this, NewEntryLiterActivity.class);
                intent.putExtra("newEntryDateYear", year);
                intent.putExtra("newEntryDateMonth", month);
                intent.putExtra("newEntryDateDay", dayOfMonth);
                intent.putExtra("newEntryMileage", mileage);
                intent.putExtra("newEntryPrice", literPrice);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right_center, R.anim.slide_out_center_left);
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
