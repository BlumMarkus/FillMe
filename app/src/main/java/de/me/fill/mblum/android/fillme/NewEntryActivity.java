package de.me.fill.mblum.android.fillme;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewEntryActivity extends AppCompatActivity {

    FillEntry fillEntryObject;
    FillEntryDataSource fillEntryDataSource;
    SettingsDataSource settingsDataSource;
    private String LOGTAG = "newEntryActivity";

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private int status; // 1 = Usereingabe | 0 = generiert
    private int mileage;
    private double price;
    private double liter;

    private int currentDateYear;
    private int currentDateMonth;
    private int currentDateDayOfMonth;

    private EditText input_newEntry_mileage;
    private EditText input_newEntry_literPrice;
    private EditText input_newEntry_fullPrice;
    private EditText input_price;
    private EditText input_newEntry_liter;

    private TextView tv_newEntry_number_date;
    private TextView tv_newEntry_number_mileage;
    private TextView tv_newEntry_number_price;
    private TextView tv_newEntry_number_liter;

    private TextView tv_newEntry_selected_date_field;
    private TextView tv_newEntry_entered_mileage_field;
    private TextView tv_newEntry_entered_price_field;
    private TextView tv_newEntry_entered_liter_field;
    private TextView tv_newEntry_entered_mileage_unit;
    private TextView tv_newEntry_entered_price_unit;
    private TextView tv_newEntry_entered_liter_unit;

    private View layout_newEntry_date_input_buttons;
    private View layout_newEntry_mileage_inputs;
    private View layout_newEntry_price_inputs;
    private View layout_newEntry_liter_inputs;

    private Button btn_newEntry_confirm_mileage;
    private Button btn_newEntry_confirm_price;
    private Button btn_newEntry_confirm_liter;

    private Boolean dateInputConfirmed;
    private Boolean mileageInputConfirmed;
    private Boolean priceInputConfirmed;
    private Boolean literInputConfirmed;

    private final String INPUT_STEP_DATE = "date";
    private final String INPUT_STEP_MILEAGE = "mileage";
    private final String INPUT_STEP_PRICE = "price";
    private final String INPUT_STEP_LITER = "liter";

    private Setting literFormatSetting;

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
        btn_newEntry_confirm_mileage = findViewById(R.id.btn_newEntry_confirm_mileage);
        btn_newEntry_confirm_price = findViewById(R.id.btn_newEntry_confirm_price);
        btn_newEntry_confirm_liter = findViewById(R.id.btn_newEntry_confirm_liter);

        Button btn_newEntry_confirm = findViewById(R.id.btn_newEntry_confirm);
        ImageButton btn_newEntry_cancel = findViewById(R.id.btn_newEntry_cancel);

        TextView tv_newEntry_price_title = findViewById(R.id.tv_newEntry_price_title);

        layout_newEntry_date_input_buttons = findViewById(R.id.layout_newEntry_date_buttons);
        input_newEntry_mileage = findViewById(R.id.input_newEntry_mileage);
        input_price = findViewById(R.id.input_newEntry_price);
        input_newEntry_liter = findViewById(R.id.input_newEntry_liter);

        tv_newEntry_number_date = findViewById(R.id.tv_newEntry_number_date);
        tv_newEntry_number_mileage = findViewById(R.id.tv_newEntry_number_mileage);
        tv_newEntry_number_price = findViewById(R.id.tv_newEntry_number_price);
        tv_newEntry_number_liter = findViewById(R.id.tv_newEntry_number_liter);

        tv_newEntry_selected_date_field = findViewById(R.id.tv_newEntry_selected_date);
        tv_newEntry_entered_mileage_field = findViewById(R.id.tv_newEntry_entered_mileage);
        tv_newEntry_entered_price_field = findViewById(R.id.tv_newEntry_entered_price);
        tv_newEntry_entered_liter_field = findViewById(R.id.tv_newEntry_entered_liter);

        tv_newEntry_entered_mileage_unit = findViewById(R.id.tv_newEntry_entered_mileage_unit);
        tv_newEntry_entered_price_unit = findViewById(R.id.tv_newEntry_entered_price_unit);
        tv_newEntry_entered_liter_unit = findViewById(R.id.tv_newEntry_entered_liter_unit);

        layout_newEntry_mileage_inputs = findViewById(R.id.layout_newEntry_mileage_inputs);
        layout_newEntry_price_inputs = findViewById(R.id.layout_newEntry_price_inputs);
        layout_newEntry_liter_inputs = findViewById(R.id.layout_newEntry_liter_inputs);

        fillEntryDataSource = new FillEntryDataSource(this);
        settingsDataSource = new SettingsDataSource(this);

        literFormatSetting = settingsDataSource.getByName(SettingsDataSource.SETTING_LITERFORMAT);
        String priceTitle = literFormatSetting.getChoices()[Integer.parseInt(literFormatSetting.getValue())] + ":";
        tv_newEntry_price_title.setText(priceTitle);

        dateInputConfirmed = false;
        mileageInputConfirmed = true;
        priceInputConfirmed = false;
        literInputConfirmed = false;

        Calendar currentDateCalendar = Calendar.getInstance();
        currentDateYear = currentDateCalendar.get(Calendar.YEAR);
        currentDateMonth = currentDateCalendar.get(Calendar.MONTH);
        currentDateDayOfMonth = currentDateCalendar.get(Calendar.DAY_OF_MONTH);

        // Button OnClickListener
        btn_newEntry_select_today.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = String.format("%02d", currentDateDayOfMonth) + "." + String.format("%02d", currentDateMonth + 1) + "." + String.format("%02d", currentDateYear);

                changeInputState(true, value, layout_newEntry_date_input_buttons, tv_newEntry_selected_date_field, tv_newEntry_number_date, INPUT_STEP_DATE, input_newEntry_mileage, tv_newEntry_entered_mileage_unit);
                dateInputConfirmed = true;
            }
        });

        btn_newEntry_select_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewEntryActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener, currentDateYear, currentDateMonth, currentDateDayOfMonth);
                dialog.show();
            }
        });

        btn_newEntry_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_newEntry_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateInput = tv_newEntry_selected_date_field.getText().toString();
                String mileageInput = tv_newEntry_entered_mileage_field.getText().toString();
                String priceInput = tv_newEntry_entered_price_field.getText().toString();
                String literInput = tv_newEntry_entered_liter_field.getText().toString();

                if (!dateInputConfirmed) {
                    Toast.makeText(NewEntryActivity.this, "Datumfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                } else if (!mileageInputConfirmed) {
                    Toast.makeText(NewEntryActivity.this, "Kilometerstand darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                } else if (!priceInputConfirmed) {
                    Toast.makeText(NewEntryActivity.this, "Preisfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                } else if (!literInputConfirmed) {
                    Toast.makeText(NewEntryActivity.this, "Literfeld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                } else {
                    mileage = Integer.parseInt(mileageInput);
                    price = Double.parseDouble(priceInput);
                    liter = Double.parseDouble(literInput);

                    if (literFormatSetting.getValue().equals(SettingsDataSource.VALUE_LITERFORMAT_LITERPRICE_DEFAULT)) {
                        price = price * liter;
                    }

                    int now = (int) Calendar.getInstance().getTime().getTime();
                    fillEntryObject = new FillEntry(dateInput, mileage, liter, price, 1, now);
                    boolean result = fillEntryDataSource.writeEntry(fillEntryObject);

                    if (result) {
                        Toast.makeText(NewEntryActivity.this, "Datenbankeintrag erfolgreich", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewEntryActivity.this, "Datenbankeintrag fehlgeschlagen", Toast.LENGTH_SHORT).show();
                    }

                    Log.d(LOGTAG, "Activity wird nun geschlossen.");
                    finish();
                }
            }
        });

        btn_newEntry_confirm_mileage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String mileageInput = input_newEntry_mileage.getText().toString();

                if (!mileageInput.equals("")) {
                    changeInputState(true, mileageInput, layout_newEntry_mileage_inputs, tv_newEntry_entered_mileage_field, tv_newEntry_number_mileage, INPUT_STEP_MILEAGE, input_newEntry_mileage, tv_newEntry_entered_mileage_unit);
                    mileageInputConfirmed = true;
                }
            }
        });

        btn_newEntry_confirm_price.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceInput = input_price.getText().toString();

                if (!priceInput.equals("")) {
                    changeInputState(true, priceInput, layout_newEntry_price_inputs, tv_newEntry_entered_price_field, tv_newEntry_number_price, INPUT_STEP_PRICE, input_price, tv_newEntry_entered_price_unit);
                    priceInputConfirmed = true;
                }
            }
        });

        btn_newEntry_confirm_liter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String literInput = input_newEntry_liter.getText().toString();

                if (!literInput.equals("")) {
                    changeInputState(true, literInput, layout_newEntry_liter_inputs, tv_newEntry_entered_liter_field, tv_newEntry_number_liter, INPUT_STEP_LITER, input_newEntry_liter, tv_newEntry_entered_liter_unit);
                    literInputConfirmed = true;
                }
            }
        });

        // TextView OnClickListener
        tv_newEntry_selected_date_field.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInputState(false, "", layout_newEntry_date_input_buttons, tv_newEntry_selected_date_field, tv_newEntry_number_date, INPUT_STEP_DATE, input_newEntry_mileage, tv_newEntry_entered_mileage_unit);
                dateInputConfirmed = false;
            }
        });

        tv_newEntry_entered_mileage_field.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInputState(false, "", layout_newEntry_mileage_inputs, tv_newEntry_entered_mileage_field, tv_newEntry_number_mileage, INPUT_STEP_MILEAGE, input_newEntry_mileage, tv_newEntry_entered_mileage_unit);
                mileageInputConfirmed = false;
            }
        });

        tv_newEntry_entered_price_field.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInputState(false, "", layout_newEntry_price_inputs, tv_newEntry_entered_price_field, tv_newEntry_number_price, INPUT_STEP_PRICE, input_price, tv_newEntry_entered_price_unit);
                priceInputConfirmed = false;
            }
        });

        tv_newEntry_entered_liter_field.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInputState(false, "", layout_newEntry_liter_inputs, tv_newEntry_entered_liter_field, tv_newEntry_number_liter, INPUT_STEP_LITER, input_newEntry_liter, tv_newEntry_entered_liter_unit);
                literInputConfirmed = false;
            }
        });

        // OnFocusChange Listener
        input_newEntry_mileage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String mileageInput = input_newEntry_mileage.getText().toString();

                    btn_newEntry_confirm_mileage.setVisibility(View.INVISIBLE);
                    setWidthOf(input_newEntry_mileage, LinearLayout.LayoutParams.MATCH_PARENT);

                    if (!mileageInput.equals("")) {
                        changeInputState(true, mileageInput, layout_newEntry_mileage_inputs, tv_newEntry_entered_mileage_field, tv_newEntry_number_mileage, INPUT_STEP_MILEAGE, input_newEntry_mileage, tv_newEntry_entered_mileage_unit);
                        mileageInputConfirmed = true;
                    }
                } else {
                    btn_newEntry_confirm_mileage.setVisibility(View.VISIBLE);
                    setWidthOf(input_newEntry_mileage, 0);
                }
            }
        });

        input_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String priceInput = input_price.getText().toString();

                    btn_newEntry_confirm_price.setVisibility(View.INVISIBLE);
                    setWidthOf(input_price, LinearLayout.LayoutParams.MATCH_PARENT);

                    if (!priceInput.equals("")) {
                        changeInputState(true, priceInput, layout_newEntry_price_inputs, tv_newEntry_entered_price_field, tv_newEntry_number_price, INPUT_STEP_PRICE, input_price, tv_newEntry_entered_price_unit);
                        priceInputConfirmed = true;
                    }
                } else {
                    btn_newEntry_confirm_price.setVisibility(View.VISIBLE);
                    setWidthOf(input_price, 0);
                }
            }
        });

        input_newEntry_liter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String literInput = input_newEntry_liter.getText().toString();

                    btn_newEntry_confirm_liter.setVisibility(View.INVISIBLE);
                    setWidthOf(input_newEntry_liter, LinearLayout.LayoutParams.MATCH_PARENT);

                    if (!literInput.equals("")) {
                        changeInputState(true, literInput, layout_newEntry_liter_inputs, tv_newEntry_entered_liter_field, tv_newEntry_number_liter, INPUT_STEP_LITER, input_newEntry_liter, tv_newEntry_entered_liter_unit);
                        literInputConfirmed = true;
                    }
                } else {
                    btn_newEntry_confirm_liter.setVisibility(View.VISIBLE);
                    setWidthOf(input_newEntry_liter, 0);
                }
            }
        });

        // OnDateSetListener
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String value = String.format("%02d", dayOfMonth) + "." + String.format("%02d", month) + "." + String.format("%04d", year);

                changeInputState(true, value, layout_newEntry_date_input_buttons, tv_newEntry_selected_date_field, tv_newEntry_number_date, INPUT_STEP_DATE, input_newEntry_mileage, tv_newEntry_entered_mileage_unit);
                dateInputConfirmed = true;
            }
        };
    }

    /**
     * Sets or unsets the selected variable and displays/hides the option/input fields.
     *
     * @param insertion   True if the user wants to insert and not to edit the entry
     * @param value       The input value
     * @param layout      The layout which should be displayed/hidden
     * @param tv_selected TextView where the value will be displayed
     * @param number      TextView number in front of the input - for design changes
     * @param step        Step in order to process further
     */
    private void changeInputState(Boolean insertion, String value, View layout, TextView tv_selected, TextView number, String step, EditText input_field, TextView unit) {
        tv_selected.setText(value);

        if (insertion) {
            setHeightOf(layout, 0);
            number.setBackgroundResource(R.drawable.shape_radius_layout_rounded_green);

            if (!step.equals(INPUT_STEP_DATE)) {
                unit.setVisibility(View.VISIBLE);
                input_field.setEnabled(false);
            }
        } else {
            setHeightOf(layout, LinearLayout.LayoutParams.WRAP_CONTENT);
            number.setBackgroundResource(R.drawable.shape_radius_layout_rounded);

            if (!step.equals(INPUT_STEP_DATE)) {
                input_field.requestFocus();
                unit.setVisibility(View.INVISIBLE);
                input_field.setEnabled(true);
            }
        }
    }

    /**
     * Set width of a view to given value
     *
     * @param layout View to change to
     * @param width  Width
     */
    private void setWidthOf(View layout, int width) {
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.width = width;
        layout.setLayoutParams(layoutParams);
    }

    /**
     * Set height of a view to given value
     *
     * @param layout View to change to
     * @param height Height
     */
    private void setHeightOf(View layout, int height) {
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.height = height;
        layout.setLayoutParams(layoutParams);
    }
}
