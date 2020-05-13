package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private String LOGTAG = "MenuActivity";

    private TextView tv_menu_last_entry_mileage;
    private TextView tv_menu_last_entry_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageButton btn_menu_add_new_entry = findViewById(R.id.btn_menu_add_new_entry);
        ImageButton btn_menu_settings = findViewById(R.id.btn_menu_show_settings);
        TextView tv_menu_statistic_title = findViewById(R.id.tv_menu_statistic_title);
        TextView tv_menu_lastActivity_title = findViewById(R.id.tv_menu_lastActivity_title);

        tv_menu_last_entry_mileage = findViewById(R.id.tv_menu_last_entry_mileage);
        tv_menu_last_entry_date = findViewById(R.id.tv_menu_last_entry_date);

        FillMeDataSource dataSource = new FillMeDataSource(this);
        ArrayList<FillEntry> allEntries = dataSource.getAllEntries(true);

        int allEntriesSize = allEntries.size();

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
        FillEntriesHelper fillEntriesHelper = new FillEntriesHelper(allEntriesList);

        AnyChartView anyChartView = findViewById(R.id.chart_menu_statistic);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_menu_statistic));


        Date now = Calendar.getInstance().getTime();
        Calendar calcCalendar = Calendar.getInstance();
        calcCalendar.setTime(now);

        List<DataEntry> chartData = new ArrayList<>();

        String xAxisName;
        double value;

        for (int i = 0; i < 12; i++) {
            calcCalendar.add(Calendar.MONTH, -1);

            xAxisName = dateHelper.getMonthShort(calcCalendar.get(Calendar.MONTH) + 1);
            value = fillEntriesHelper.getConsumptionAvgMonth(calcCalendar.get(Calendar.MONTH) + 1, calcCalendar.get(Calendar.YEAR));

            if (value != 0) {
                chartData.add(new ValueDataEntry(xAxisName, value));
            }
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
        ArrayList<FillEntry> lastActivityEntries = new ArrayList<>();

        if (allEntriesSize > 3) {
            for (int counter = 0; counter < allEntriesSize; counter++) {
                lastActivityEntries.add(counter, allEntriesList.get(counter));
            }
        } else {
            lastActivityEntries = allEntriesList;
        }

        // TextView tv_menu_lastActivity_monthShort = customView.findViewById(R.id.tv_menu_lastActivity_monthShort);
        // TextView tv_menu_lastActivity_dayOfMonth = customView.findViewById(R.id.tv_menu_lastActivity_dayOfMonth);
        // ImageView iv_menu_lastActivity_consumptionStatus = customView.findViewById(R.id.iv_menu_lastActivity_consumptionStatus);
        // TextView tv_menu_lastActivity_consumption = customView.findViewById(R.id.tv_menu_lastActivity_consumption);
        // ImageView iv_menu_lastActivity_literPriceStatus = customView.findViewById(R.id.iv_menu_lastActivity_literPriceStatus);
        // TextView tv_menu_lastActivity_literPrice = customView.findViewById(R.id.tv_menu_lastActivity_literPrice);
//
        // DecimalFormat consumptionFormat = new DecimalFormat("0.0");
        // DecimalFormat literPriceFormat = new DecimalFormat("0.000");
//
        // String consumptionText;
        // int objectListSize = objectList.size() - 1;
//
        // if (position + 1 <= objectListSize) {
        //     int mileageOfEntryBefore = objectList.get(position + 1).getMileage();
        //     double drivenKilometers = mileageOfEntryBefore - objectList.get(position).getMileage();
        //     double consumption = objectList.get(position).getLiter() / (drivenKilometers / 100);
        //     consumptionText = String.valueOf(consumptionFormat.format(consumption));
        // } else {
        //     consumptionText = "n/a";
        // }
//
        // tv_menu_lastActivity_monthShort.setText(String.valueOf(objectList.get(position).getMonthShort()));
        // tv_menu_lastActivity_dayOfMonth.setText(String.valueOf(objectList.get(position).getDayOfMonth()));
        // tv_menu_lastActivity_consumption.setText(consumptionText);
        // tv_menu_lastActivity_literPrice.setText(literPriceFormat.format(objectList.get(position).getLiterPrice()));
//
        // customView.setTag(objectList.get(position).getID());
//
        // return customView;
    }
}
