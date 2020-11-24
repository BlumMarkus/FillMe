package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewEntryMileageActivity extends AppCompatActivity {

    private int year;
    private int month;
    private int dayOfMonth;

    private EditText editText_newEntry_mileage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry_mileage);

        editText_newEntry_mileage = findViewById(R.id.editText_newEntry_mileage);
        editText_newEntry_mileage.setTransformationMethod(null);
        editText_newEntry_mileage.requestFocus();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // NewEntryDate variables
            year = extras.getInt("newEntryDateYear");
            month = extras.getInt("newEntryDateMonth");
            dayOfMonth = extras.getInt("newEntryDateDay");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageButton btn_cancel = findViewById(R.id.btn_newEntryMileage_cancel);
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_next = findViewById(R.id.btn_newEntryMileage_next);
        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String mileageString = editText_newEntry_mileage.getText().toString();

                if (mileageString.equals("")) {
                    Toast toast = Toast.makeText(NewEntryMileageActivity.this, "Kilometerstand darf nicht leer sein.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    int mileage = Integer.parseInt(mileageString);

                    Intent intent = new Intent(NewEntryMileageActivity.this, NewEntryPriceActivity.class);
                    intent.putExtra("newEntryDateYear", year);
                    intent.putExtra("newEntryDateMonth", month);
                    intent.putExtra("newEntryDateDay", dayOfMonth);
                    intent.putExtra("newEntryMileage", mileage);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right_center, R.anim.slide_out_center_left);
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
