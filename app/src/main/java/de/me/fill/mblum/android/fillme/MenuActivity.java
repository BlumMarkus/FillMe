package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private String LOGTAG = "MenuActivity";

    private LayoutHelper layoutHelper;
    private FillEntriesHelper fillEntriesHelper;

    private LinearLayout layout_menu_lastActivity_1;
    private LinearLayout layout_menu_lastActivity_2;
    private LinearLayout layout_menu_lastActivity_3;

    private TextView tv_menu_last_entry_mileage;
    private TextView tv_menu_last_entry_date;

    DecimalFormat consumptionFormat;
    DecimalFormat literPriceFormat;

    private int doublePositiveDrawable;
    private int positiveDrawable;
    private int neutralDrawable;
    private int negativeDrawable;
    private int doubleNegativeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageButton btn_menu_add_new_entry = findViewById(R.id.btn_menu_add_new_entry);
        ImageButton btn_menu_settings = findViewById(R.id.btn_menu_settings);
        TextView tv_menu_statistic_title = findViewById(R.id.tv_menu_statistic_title);
        TextView tv_menu_lastActivity_title = findViewById(R.id.tv_menu_lastActivity_title);

        tv_menu_last_entry_mileage = findViewById(R.id.tv_menu_last_entry_mileage);
        tv_menu_last_entry_date = findViewById(R.id.tv_menu_last_entry_date);

        layout_menu_lastActivity_1 = findViewById(R.id.layout_menu_lastActivity_1);
        layout_menu_lastActivity_2 = findViewById(R.id.layout_menu_lastActivity_2);
        layout_menu_lastActivity_3 = findViewById(R.id.layout_menu_lastActivity_3);

        doublePositiveDrawable = R.drawable.positive_double_cirlce_green;
        positiveDrawable = R.drawable.positive_circle_green;
        neutralDrawable = R.drawable.neutral_line_icon;
        negativeDrawable = R.drawable.negative_circle_red;
        doubleNegativeDrawable = R.drawable.negative_double_circle_red;

        FillMeDataSource dataSource = new FillMeDataSource(this);
        ArrayList<FillEntry> allEntries = dataSource.getAllEntries(true);

        consumptionFormat = new DecimalFormat("0.00");
        literPriceFormat = new DecimalFormat("0.000");

        layoutHelper = new LayoutHelper();

        int allEntriesSize = allEntries.size();

        fillEntriesHelper = new FillEntriesHelper();
        allEntries = fillEntriesHelper.setOptionalFillEntryValues(allEntries);

        updateStatistic(allEntries);
        updateLastEntryData(allEntries, allEntriesSize);
        updateLastActivityList(allEntries, allEntriesSize);

        btn_menu_add_new_entry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newEntryActivity = new Intent(MenuActivity.this, NewEntryActivity.class);
                startActivity(newEntryActivity);
            }
        });

        tv_menu_statistic_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowDiagramActivity = new Intent(MenuActivity.this, ShowDiagramActivity.class);
                startActivity(ShowDiagramActivity);
            }
        });

        tv_menu_lastActivity_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showEntriesActivity = new Intent(MenuActivity.this, ShowEntriesActivity.class);
                startActivity(showEntriesActivity);
            }
        });

        btn_menu_settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsActivity = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(settingsActivity);
            }
        });
    }

    /**
     * Displays the last entries in menu.
     *
     * @param allEntriesList list that contains all fillEntry objects stored in database
     * @param listSize       size of the given arrayList
     */
    private void updateLastEntryData(ArrayList<FillEntry> allEntriesList, int listSize) {
        if (listSize == 0) {
            tv_menu_last_entry_mileage.setText("n/a");
            tv_menu_last_entry_date.setText("n/a");
        } else {
            tv_menu_last_entry_mileage.setText(String.valueOf(allEntriesList.get(0).getMileage()));
            tv_menu_last_entry_date.setText(String.valueOf(allEntriesList.get(0).getStringDate()));
        }
    }

    /**
     * Displays a chart for consumption of the last 12 month (current month not included)
     */
    private void updateStatistic(ArrayList<FillEntry> allEntriesList) {
        Cartesian cartesian = AnyChart.column();
        DateHelper dateHelper = new DateHelper();
        FillEntriesHelper fillEntriesHelper = new FillEntriesHelper();

        AnyChartView anyChartView = findViewById(R.id.chart_menu_statistic);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_menu_statistic));

        Date now = Calendar.getInstance().getTime();
        Calendar calcCalendar = Calendar.getInstance();
        calcCalendar.setTime(now);
        calcCalendar.add(Calendar.MONTH, -12);

        List<DataEntry> chartData = new ArrayList<>();

        String xAxisName;
        double value;

        for (int i = 0; i < 12; i++) {
            xAxisName = dateHelper.getMonthShort(calcCalendar.get(Calendar.MONTH));
            value = fillEntriesHelper.getConsumptionAvgMonth(calcCalendar.get(Calendar.MONTH), calcCalendar.get(Calendar.YEAR), allEntriesList);

            chartData.add(new ValueDataEntry(xAxisName, value));

            calcCalendar.add(Calendar.MONTH, +1);
        }

        Column column = cartesian.column(chartData);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: } l");

        cartesian.animation(true);

        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).title("Liter/100km");
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: } l");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        anyChartView.setChart(cartesian);
    }

    /**
     * Sends the arrayList to the listView to display the last activity in menu activity.
     *
     * @param allEntriesList list that contains all fillEntry objects stored in database
     * @param allEntriesSize size of the given arrayList
     */
    private void updateLastActivityList(ArrayList<FillEntry> allEntriesList, int allEntriesSize) {
        double consumptionAverage = fillEntriesHelper.getConsumptionAvg(allEntriesList);
        double literPriceAverage = fillEntriesHelper.getLiterPriceAvg(allEntriesList);

        if (allEntriesSize >= 3) {
            displayFirstActivity(allEntriesList.get(0), consumptionAverage, literPriceAverage);
            displaySecondActivity(allEntriesList.get(1), consumptionAverage, literPriceAverage);
            displayThirdActivity(allEntriesList.get(2), consumptionAverage, literPriceAverage);
            displayAverage(consumptionAverage, literPriceAverage);
        } else if (allEntriesSize == 2) {
            displayFirstActivity(allEntriesList.get(0), consumptionAverage, literPriceAverage);
            displaySecondActivity(allEntriesList.get(1), consumptionAverage, literPriceAverage);
            displayAverage(consumptionAverage, literPriceAverage);
        } else if (allEntriesSize == 1) {
            displayFirstActivity(allEntriesList.get(0), consumptionAverage, literPriceAverage);
            displayAverage(consumptionAverage, literPriceAverage);
        }
    }

    /**
     * Displays the last entry the user entered
     *
     * @param fillEntryObject fillEntry object
     */
    private void displayFirstActivity(FillEntry fillEntryObject, double avgConsumption, double avgLiterPrice) {
        TextView tv_menu_lastActivity_1_monthShort = findViewById(R.id.tv_menu_lastActivity_1_monthShort);
        TextView tv_menu_lastActivity_1_dayOfMonth = findViewById(R.id.tv_menu_lastActivity_1_dayOfMonth);
        ImageView iv_menu_lastActivity_1_consumptionStatus = findViewById(R.id.iv_menu_lastActivity_1_consumptionStatus);
        TextView tv_menu_lastActivity_1_consumption = findViewById(R.id.tv_menu_lastActivity_1_consumption);
        ImageView iv_menu_lastActivity_1_literPriceStatus = findViewById(R.id.iv_menu_lastActivity_1_literPriceStatus);
        TextView tv_menu_lastActivity_1_literPrice = findViewById(R.id.tv_menu_lastActivity_1_literPrice);

        double literAmount = fillEntryObject.getLiter();
        double drivenMileage = fillEntryObject.getDrivenMileage();
        double consumption = literAmount / (drivenMileage / 100);

        tv_menu_lastActivity_1_monthShort.setText(fillEntryObject.getMonthShort());
        tv_menu_lastActivity_1_dayOfMonth.setText(fillEntryObject.getDayOfMonth());

        tv_menu_lastActivity_1_consumption.setText(consumptionFormat.format(consumption));
        tv_menu_lastActivity_1_literPrice.setText(literPriceFormat.format(fillEntryObject.getLiterPrice()));

        iv_menu_lastActivity_1_consumptionStatus.setBackground(getDrawable(getDrawableIdOfGivenValues(avgConsumption, consumption)));
        iv_menu_lastActivity_1_literPriceStatus.setBackground(getDrawable(getDrawableIdOfGivenValues(avgLiterPrice, fillEntryObject.getLiterPrice())));

        layoutHelper.setHeightOf(layout_menu_lastActivity_1, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Displays the before last entry the user entered
     *
     * @param fillEntryObject fillEntry object
     */
    private void displaySecondActivity(FillEntry fillEntryObject, double avgConsumption, double avgLiterPrice) {
        TextView tv_menu_lastActivity_2_monthShort = findViewById(R.id.tv_menu_lastActivity_2_monthShort);
        TextView tv_menu_lastActivity_2_dayOfMonth = findViewById(R.id.tv_menu_lastActivity_2_dayOfMonth);
        ImageView iv_menu_lastActivity_2_consumptionStatus = findViewById(R.id.iv_menu_lastActivity_2_consumptionStatus);
        TextView tv_menu_lastActivity_2_consumption = findViewById(R.id.tv_menu_lastActivity_2_consumption);
        ImageView iv_menu_lastActivity_2_literPriceStatus = findViewById(R.id.iv_menu_lastActivity_2_literPriceStatus);
        TextView tv_menu_lastActivity_2_literPrice = findViewById(R.id.tv_menu_lastActivity_2_literPrice);

        double literAmount = fillEntryObject.getLiter();
        double drivenMileage = fillEntryObject.getDrivenMileage();
        double consumption = literAmount / (drivenMileage / 100);

        tv_menu_lastActivity_2_monthShort.setText(fillEntryObject.getMonthShort());
        tv_menu_lastActivity_2_dayOfMonth.setText(fillEntryObject.getDayOfMonth());

        tv_menu_lastActivity_2_consumption.setText(consumptionFormat.format(consumption));
        tv_menu_lastActivity_2_literPrice.setText(literPriceFormat.format(fillEntryObject.getLiterPrice()));

        iv_menu_lastActivity_2_consumptionStatus.setBackground(getDrawable(getDrawableIdOfGivenValues(avgConsumption, consumption)));
        iv_menu_lastActivity_2_literPriceStatus.setBackground(getDrawable(getDrawableIdOfGivenValues(avgLiterPrice, fillEntryObject.getLiterPrice())));

        layoutHelper.setHeightOf(layout_menu_lastActivity_2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Displays the 3rd before last entry the user entered
     *
     * @param fillEntryObject fillEntry object
     */
    private void displayThirdActivity(FillEntry fillEntryObject, double avgConsumption, double avgLiterPrice) {
        TextView tv_menu_lastActivity_3_monthShort = findViewById(R.id.tv_menu_lastActivity_3_monthShort);
        TextView tv_menu_lastActivity_3_dayOfMonth = findViewById(R.id.tv_menu_lastActivity_3_dayOfMonth);
        ImageView iv_menu_lastActivity_3_consumptionStatus = findViewById(R.id.iv_menu_lastActivity_3_consumptionStatus);
        TextView tv_menu_lastActivity_3_consumption = findViewById(R.id.tv_menu_lastActivity_3_consumption);
        ImageView iv_menu_lastActivity_3_literPriceStatus = findViewById(R.id.iv_menu_lastActivity_3_literPriceStatus);
        TextView tv_menu_lastActivity_3_literPrice = findViewById(R.id.tv_menu_lastActivity_3_literPrice);

        double literAmount = fillEntryObject.getLiter();
        double drivenMileage = fillEntryObject.getDrivenMileage();
        double consumption = literAmount / (drivenMileage / 100);

        tv_menu_lastActivity_3_monthShort.setText(fillEntryObject.getMonthShort());
        tv_menu_lastActivity_3_dayOfMonth.setText(fillEntryObject.getDayOfMonth());

        tv_menu_lastActivity_3_consumption.setText(consumptionFormat.format(consumption));
        tv_menu_lastActivity_3_literPrice.setText(literPriceFormat.format(fillEntryObject.getLiterPrice()));

        iv_menu_lastActivity_3_consumptionStatus.setBackground(getDrawable(getDrawableIdOfGivenValues(avgConsumption, consumption)));
        iv_menu_lastActivity_3_literPriceStatus.setBackground(getDrawable(getDrawableIdOfGivenValues(avgLiterPrice, fillEntryObject.getLiterPrice())));

        layoutHelper.setHeightOf(layout_menu_lastActivity_3, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void displayAverage(double avgConsumption, double avgLiterPrice) {
        LinearLayout layout_menu_lastActivity_avg = findViewById(R.id.layout_menu_lastActivity_avg);
        TextView tv_menu_lastActivity_avg_consumption = findViewById(R.id.tv_menu_lastActivity_avg_consumption);
        TextView tv_menu_lastActivity_avg_literPrice = findViewById(R.id.tv_menu_lastActivity_avg_literPrice);

        tv_menu_lastActivity_avg_consumption.setText(consumptionFormat.format(avgConsumption));
        tv_menu_lastActivity_avg_literPrice.setText(literPriceFormat.format(avgLiterPrice));

        layoutHelper.setHeightOf(layout_menu_lastActivity_avg, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Returns the drawable based on the given average and entity value
     *
     * @param average Average of the given value
     * @param value   Value of the object
     */
    int getDrawableIdOfGivenValues(double average, double value) {
        double index = value / (average / 100);

        if (index > 120) {
            return doubleNegativeDrawable;
        } else if (index > 105) {
            return negativeDrawable;
        } else if (index > 95) {
            return neutralDrawable;
        } else if (index > 80) {
            return positiveDrawable;
        } else {
            return doublePositiveDrawable;
        }
    }
}
