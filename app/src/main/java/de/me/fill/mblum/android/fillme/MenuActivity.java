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
import android.widget.ImageButton;
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
    private String LOGTAG = "MenuActivity";

    private ArrayList<FillEntry> list;
    private ArrayList<FillEntry> monthList;
    private ArrayList<FillEntry> yearList;

    private TextView tv_show_last_mileage;
    private TextView tv_show_last_date;

    private TextView tv_show_overview_mileage;
    private TextView tv_show_overview_time;
    private TextView tv_show_overview_cost;
    private TextView tv_show_overview_liter;

    private ImageButton btn_add_new_entry;
    private ImageButton btn_show_statistic_list;
    private ImageButton btn_show_statistic_diagram;

    private ImageButton btn_show_overview_last_fill;
    private ImageButton btn_show_overview_last_month;
    private ImageButton btn_show_overview_last_year;
    private ImageButton btn_show_overview_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d(LOGTAG,"onCreate wurde erfolgreich aufgerufen.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOGTAG,"onResume wurde erfolgreich aufgerufen.");

        btn_add_new_entry = (ImageButton) findViewById(R.id.btn_menu_add_new_entry);
        btn_show_statistic_list = (ImageButton) findViewById(R.id.btn_menu_show_statistic_list);
        btn_show_statistic_diagram = (ImageButton) findViewById(R.id.btn_menu_show_statistic_diagram);

        btn_show_overview_last_fill = (ImageButton) findViewById(R.id.btn_menu_show_overview_last_fill);
        btn_show_overview_last_month = (ImageButton) findViewById(R.id.btn_menu_show_overview_last_month);
        btn_show_overview_last_year = (ImageButton) findViewById(R.id.btn_menu_show_overview_last_year);
        btn_show_overview_all = (ImageButton) findViewById(R.id.btn_menu_show_overview_all);

        tv_show_last_mileage = (TextView) findViewById(R.id.tv_menu_show_last_mileage);
        tv_show_last_date = (TextView) findViewById(R.id.tv_menu_show_last_date);

        tv_show_overview_mileage = (TextView) findViewById(R.id.tv_menu_show_overview_mileage);
        tv_show_overview_time = (TextView) findViewById(R.id.tv_menu_show_overview_time);
        tv_show_overview_cost = (TextView) findViewById(R.id.tv_menu_show_overview_cost);
        tv_show_overview_liter = (TextView) findViewById(R.id.tv_menu_show_overview_liter);

        cal = Calendar.getInstance();
        yearCal = cal.get(Calendar.YEAR);
        monthCal = cal.get(Calendar.MONTH);

        fmds = new FillMeDataSource(this);
        list = fmds.getAllEntries();
        monthList = fmds.getlastMonth(monthCal + 1, yearCal);
        yearList = fmds.getlastYear(yearCal);
        updateDisplayedData(list);

        Log.d(LOGTAG,"onResume ist hier zuende.");

        btn_add_new_entry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, newEntryActivity.class);
                startActivity(intent);
            }
        });

        btn_show_statistic_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, showEntriesActivity.class);
                startActivity(intent);
            }
        });

        btn_show_overview_last_fill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show_overview_last_fill.setBackgroundResource(R.color.activeButton);
                btn_show_overview_last_month.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_year.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_all.setBackgroundResource(R.color.passiveButton);

                //updateStatisticLastFill(list);
            }
        });

        btn_show_overview_last_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show_overview_last_fill.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_month.setBackgroundResource(R.color.activeButton);
                btn_show_overview_last_year.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_all.setBackgroundResource(R.color.passiveButton);

                //updateStatistic(monthList);
            }
        });

        btn_show_overview_last_year.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show_overview_last_fill.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_month.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_year.setBackgroundResource(R.color.activeButton);
                btn_show_overview_all.setBackgroundResource(R.color.passiveButton);

                //updateStatistic(yearList);
            }
        });

        btn_show_overview_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show_overview_last_fill.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_month.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_year.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_all.setBackgroundResource(R.color.activeButton);

                updateOverviewAll(list);
            }
        });
}

    private void updateDisplayedData(ArrayList<FillEntry> sortedList) {
        int listSize = sortedList.size();

        btn_show_overview_last_fill.setEnabled(false);
        btn_show_overview_last_month.setEnabled(false);
        btn_show_overview_last_year.setEnabled(false);
        btn_show_overview_all.setEnabled(false);

        if (listSize == 0) {
            tv_show_last_mileage.setText("n/a");
            tv_show_last_date.setText("n/a");

            tv_show_overview_mileage.setText("n/a");
            tv_show_overview_time.setText("n/a");
            tv_show_overview_cost.setText("n/a");
            tv_show_overview_liter.setText("n/a");
        } else if (listSize == 1) {
            btn_show_overview_all.setBackgroundResource(R.color.activeButton);

            tv_show_last_mileage.setText(String.valueOf(sortedList.get(0).getMileage()));
            tv_show_last_date.setText(String.valueOf(sortedList.get(0).getDate()));

            tv_show_overview_mileage.setText(String.valueOf(sortedList.get(0).getMileage()));
            tv_show_overview_time.setText("last fill"); //Müssen hier noch ausrechnen, wie viel tage das schon her ist
            tv_show_overview_cost.setText(String.valueOf(sortedList.get(0).getPrice()));
            tv_show_overview_liter.setText(String.valueOf(sortedList.get(0).getLiter()));
        } else {
            btn_show_overview_last_fill.setEnabled(true);
            btn_show_overview_all.setEnabled(true);

            if (monthList.size() > 1) {
                btn_show_overview_last_month.setEnabled(true);
            }

            if (yearList.size() > 1) {
                btn_show_overview_last_year.setEnabled(true);
            }

            tv_show_last_mileage.setText((String.valueOf(sortedList.get(0).getMileage())));
            tv_show_last_date.setText(String.valueOf(sortedList.get(0).getDate()));

            int actualDistance = sortedList.get(0).getMileage() - sortedList.get(1).getMileage();

            tv_show_overview_mileage.setText(String.valueOf(actualDistance));
            tv_show_overview_time.setText(""); //Müssen hier noch ausrechnen, wie viel tage das schon her ist
            tv_show_overview_cost.setText(String.valueOf(f.format((sortedList.get(0).getPrice() / actualDistance) * 100)));
            tv_show_overview_liter.setText(String.valueOf(f.format((sortedList.get(0).getLiter() / actualDistance) * 100)));

            btn_show_overview_last_fill.setBackgroundResource(R.color.activeButton);
        }
    }

    private void updateOverviewAll(ArrayList<FillEntry> sortedList) {
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

        tv_show_overview_mileage.setText(String.valueOf(totalDistance + " km"));

        tv_show_overview_liter.setText(String.valueOf(f.format((sumLiter / totalDistance) * 100)));
        tv_show_overview_cost.setText(String.valueOf(f.format((sumCost / totalDistance) * 100)));
    }

    /*
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
    */
}
