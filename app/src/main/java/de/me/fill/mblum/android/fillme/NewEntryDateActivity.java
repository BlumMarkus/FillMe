package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewEntryDateActivity extends AppCompatActivity {
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry_date);

        calendarView = findViewById(R.id.calendarView_newEntryDate);
        Calendar currentDateCalendar = Calendar.getInstance();

        // Set calendarView variables
        calendarView.setDate(currentDateCalendar.getTimeInMillis());
        calendarView.setMaxDate(currentDateCalendar.getTimeInMillis());
        currentDateCalendar.add(Calendar.YEAR, -2);
        calendarView.setMinDate(currentDateCalendar.getTimeInMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageButton btn_newEntry_cancel = findViewById(R.id.btn_newEntryDate_cancel);
        btn_newEntry_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(NewEntryDateActivity.this, NewEntryMileageActivity.class);
                intent.putExtra("newEntryDateYear", year);
                intent.putExtra("newEntryDateMonth", month + 1);
                intent.putExtra("newEntryDateDay", dayOfMonth);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right_center, R.anim.slide_out_center_left);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        // Animation for closing activity
        overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_out_top_bottom);
    }
}
