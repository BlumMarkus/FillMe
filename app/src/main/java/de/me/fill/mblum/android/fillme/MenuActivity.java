package de.me.fill.mblum.android.fillme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MenuActivity extends AppCompatActivity {

    FillMeDataSource fmds;
    DecimalFormat f = new DecimalFormat("#0.00");
    private Calendar cal;
    private int yearCal;
    private int monthCal;
    private String logTag = "MenuActivity";

    private ArrayList<FillEntry> list;
    private ArrayList<FillEntry> monthList;
    private ArrayList<FillEntry> yearList;

    private TextView tv_actualMileage;
    private TextView tv_lastEntry;
    private TextView tv_actualConsumption;
    private TextView tv_actualConsumptionCost;
    private TextView tv_totalConsumption;
    private TextView tv_totalConsumptionCost;

    private Button btn_newEntry;
    private Button btn_showEntryList;
    private Button btn_statistic_lastFill;
    private Button btn_statistic_lastMonth;
    private Button btn_statistic_lastYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d(logTag,"onCreate wurde erfolgreich aufgerufen.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(logTag,"onResume wurde erfolgreich aufgerufen.");

        btn_newEntry = (Button) findViewById(R.id.btn_newEntry);
        btn_showEntryList = (Button) findViewById(R.id.btn_showEntry);

        btn_statistic_lastFill = (Button) findViewById(R.id.btn_DataLastFill);
        btn_statistic_lastMonth = (Button) findViewById(R.id.btn_DataLastMonth);
        btn_statistic_lastYear = (Button) findViewById(R.id.btn_DataLastYear);

        tv_actualMileage = (TextView) findViewById(R.id.txt_ActualMileage);
        tv_lastEntry = (TextView) findViewById(R.id.txt_LastEntry);
        tv_actualConsumption = (TextView) findViewById(R.id.txt_ActualConsumption);
        tv_actualConsumptionCost = (TextView) findViewById(R.id.txt_ActualConsumptionCost);
        tv_totalConsumption = (TextView) findViewById(R.id.txt_TotalConsumption);
        tv_totalConsumptionCost = (TextView) findViewById(R.id.txt_TotalConsumptionCost);

        btn_newEntry.setBackgroundResource(R.color.passiveButton);
        btn_showEntryList.setBackgroundResource(R.color.passiveButton);
        btn_statistic_lastFill.setBackgroundResource(R.color.passiveButton);
        btn_statistic_lastMonth.setBackgroundResource(R.color.passiveButton);
        btn_statistic_lastYear.setBackgroundResource(R.color.passiveButton);

        cal = Calendar.getInstance();
        yearCal = cal.get(Calendar.YEAR);
        monthCal = cal.get(Calendar.MONTH);

        fmds = new FillMeDataSource(this);
        list = fmds.getAllEntries();
        monthList = fmds.getlastMonth(monthCal + 1, yearCal);
        yearList = fmds.getlastYear(yearCal);
        updateDisplayedData(list);

        Log.d(logTag,"onResume ist hier zuende.");

        btn_newEntry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, newEntryActivity.class);
                startActivity(intent);
            }
        });

        btn_showEntryList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, showEntriesActivity.class);
                startActivity(intent);
            }
        });

        btn_statistic_lastFill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_statistic_lastFill.setBackgroundResource(R.color.activeButton);
                btn_statistic_lastMonth.setBackgroundResource(R.color.passiveButton);
                btn_statistic_lastYear.setBackgroundResource(R.color.passiveButton);

                updateStatisticLastFill(list);
            }
        });

        btn_statistic_lastMonth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_statistic_lastFill.setBackgroundResource(R.color.passiveButton);
                btn_statistic_lastMonth.setBackgroundResource(R.color.activeButton);
                btn_statistic_lastYear.setBackgroundResource(R.color.passiveButton);

                updateStatistic(monthList);
            }
        });

        btn_statistic_lastYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_statistic_lastFill.setBackgroundResource(R.color.passiveButton);
                btn_statistic_lastMonth.setBackgroundResource(R.color.passiveButton);
                btn_statistic_lastYear.setBackgroundResource(R.color.activeButton);

                updateStatistic(yearList);
            }
        });
    }

    private void updateDisplayedData(ArrayList<FillEntry> sortedList) {
        int listSize = sortedList.size();

        btn_statistic_lastFill.setEnabled(false);
        btn_statistic_lastMonth.setEnabled(false);
        btn_statistic_lastYear.setEnabled(false);

        if (listSize == 0) {
            tv_actualMileage.setText("n/a");
            tv_lastEntry.setText("n/a");
            tv_actualConsumption.setText("n/a");
            tv_actualConsumptionCost.setText("n/a");
            tv_totalConsumption.setText("n/a");
            tv_totalConsumptionCost.setText("n/a");
        } else if (listSize == 1) {
            tv_actualMileage.setText((String.valueOf(sortedList.get(0).getMileage())));
            tv_lastEntry.setText(String.valueOf(sortedList.get(0).getDate()));

            tv_actualConsumption.setText("n/a");
            tv_actualConsumptionCost.setText("n/a");
            tv_totalConsumption.setText("n/a");
            tv_totalConsumptionCost.setText("n/a");
        } else {
            btn_statistic_lastFill.setEnabled(true);

            if (monthList.size() > 1) {
                btn_statistic_lastMonth.setEnabled(true);
            }

            if (yearList.size() > 1) {
                btn_statistic_lastYear.setEnabled(true);
            }

            tv_actualMileage.setText((String.valueOf(sortedList.get(0).getMileage())));
            tv_lastEntry.setText(String.valueOf(sortedList.get(0).getDate()));

            int actualDistance = sortedList.get(0).getMileage() - sortedList.get(1).getMileage();

            tv_actualConsumption.setText(String.valueOf(f.format((sortedList.get(0).getLiter() / actualDistance) * 100)));
            tv_actualConsumptionCost.setText(String.valueOf(f.format((sortedList.get(0).getPrice() / actualDistance) * 100)));

            btn_statistic_lastFill.setBackgroundResource(R.color.activeButton);
            
            int totalDistance = sortedList.get(0).getMileage() - sortedList.get(listSize - 1).getMileage();

            double sumCost = 0;
            double sumLiter = 0;

            for (FillEntry entry : sortedList) {
                sumCost += entry.getPrice();
                sumLiter += entry.getLiter();
            }

            sumCost -= sortedList.get(listSize - 1).getPrice();
            sumLiter -=  sortedList.get(listSize - 1).getLiter();

            tv_totalConsumption.setText(String.valueOf(f.format((sumLiter / totalDistance) * 100)));
            tv_totalConsumptionCost.setText(String.valueOf(f.format((sumCost / totalDistance) * 100)));
        }
    }

    private void updateStatisticLastFill(ArrayList<FillEntry> sortedList) {
        tv_actualMileage.setText((String.valueOf(sortedList.get(0).getMileage())));
        tv_lastEntry.setText(String.valueOf(sortedList.get(0).getDate()));

        int actualDistance = sortedList.get(0).getMileage() - sortedList.get(1).getMileage();

        tv_actualConsumption.setText(String.valueOf(f.format((sortedList.get(0).getLiter() / actualDistance) * 100)));
        tv_actualConsumptionCost.setText(String.valueOf(f.format((sortedList.get(0).getPrice() / actualDistance) * 100)));
    }

    private void updateStatistic(ArrayList<FillEntry> sortedList) {
        int listSize = sortedList.size();

        int totalDistance = sortedList.get(0).getMileage() - sortedList.get(listSize - 1).getMileage();

        double sumCost = 0;
        double sumLiter = 0;

        for (FillEntry entry : sortedList) {
            sumCost += entry.getPrice();
            sumLiter += entry.getLiter();
        }

        sumCost -= sortedList.get(listSize - 1).getPrice();
        sumLiter -=  sortedList.get(listSize - 1).getLiter();

        tv_actualConsumption.setText(String.valueOf(f.format((sumLiter / totalDistance) * 100)));
        tv_actualConsumptionCost.setText(String.valueOf(f.format((sumCost / totalDistance) * 100)));
    }
}
