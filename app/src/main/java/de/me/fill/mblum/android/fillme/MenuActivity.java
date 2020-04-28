package de.me.fill.mblum.android.fillme;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MenuActivity extends AppCompatActivity {

    FillMeDataSource fmds;
    DecimalFormat f = new DecimalFormat("#0.00");

    private Calendar calendar;
    private int actualCalendarDay;
    private int actualCalendarMonth;
    private int lastCalendarMonth;
    private int actualCalendarYear;

    private String LOGTAG = "MenuActivity";

    private ArrayList<FillEntry> fullDataList;
    private ArrayList<FillEntry> lastMonthList;
    private ArrayList<FillEntry> lastYearList;

    private TextView tv_show_last_mileage;
    private TextView tv_show_last_date;

    private TextView tv_show_overview_mileage;
    private TextView tv_show_overview_time;
    private TextView tv_show_overview_cost;
    private TextView tv_show_overview_liter;

    private ImageButton btn_add_new_entry;
    private ImageButton btn_show_statistic_list;
    private ImageButton btn_show_statistic_diagram;
    private ImageButton btn_show_settings;
    private ImageButton btn_menu_exit_app;

    private ImageButton btn_show_overview_last_fill;
    private ImageButton btn_show_overview_last_month;
    private ImageButton btn_show_overview_last_year;
    private ImageButton btn_show_overview_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d(LOGTAG,"onCreate wurde erfolgreich aufgerufen.");

        btn_add_new_entry = (ImageButton) findViewById(R.id.btn_menu_add_new_entry);
        btn_show_statistic_list = (ImageButton) findViewById(R.id.btn_menu_show_statistic_list);
        btn_show_statistic_diagram = (ImageButton) findViewById(R.id.btn_menu_show_statistic_diagram);
        btn_show_settings = (ImageButton) findViewById(R.id.btn_menu_show_settings);
        btn_menu_exit_app = (ImageButton) findViewById(R.id.btn_menu_exit_app);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOGTAG,"onResume wurde erfolgreich aufgerufen.");

        calendar = Calendar.getInstance();
        actualCalendarDay = calendar.get(Calendar.DAY_OF_MONTH);
        actualCalendarMonth = calendar.get(Calendar.MONTH) + 1;
        actualCalendarYear = calendar.get(Calendar.YEAR);
        if(actualCalendarMonth != 1)
        {
            lastCalendarMonth = actualCalendarMonth-1;
        }
        else
        {
            lastCalendarMonth = 12;
        }

        fmds = new FillMeDataSource(this);
        fullDataList = fmds.getAllEntries();
        lastMonthList = fmds.getlastMonth(lastCalendarMonth, actualCalendarYear);
        lastYearList = fmds.getlastYear(actualCalendarYear);

        updateDisplayedData(fullDataList);

        Log.d(LOGTAG, "Listen wurden geladen:");
        Log.d(LOGTAG, "fullDataList(" + fullDataList.size() + "):" + fullDataList);
        Log.d(LOGTAG, "lastMonthList(" + lastMonthList.size() + "):" + lastMonthList);
        Log.d(LOGTAG, "lastYearList(" + lastYearList.size() + "):" + lastYearList);

        Log.d(LOGTAG,"onResume ist hier zuende.");

        btn_add_new_entry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, NewEntryActivity.class);
                startActivity(intent);
            }
        });

        btn_show_statistic_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ShowEntriesActivity.class);
                startActivity(intent);
            }
        });

        btn_show_statistic_diagram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ShowDiagramActivity.class);
                startActivity(intent);
            }
        });

        btn_show_settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btn_menu_exit_app.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });


        btn_show_overview_last_fill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show_overview_last_fill.setBackgroundResource(R.color.activeButton);
                btn_show_overview_last_month.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_year.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_all.setBackgroundResource(R.color.passiveButton);

                updateOverviewLastFill(fullDataList);
            }
        });

        btn_show_overview_last_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show_overview_last_fill.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_month.setBackgroundResource(R.color.activeButton);
                btn_show_overview_last_year.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_all.setBackgroundResource(R.color.passiveButton);

                updateOverviewLastMonth(lastMonthList);
            }
        });

        btn_show_overview_last_year.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show_overview_last_fill.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_month.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_year.setBackgroundResource(R.color.activeButton);
                btn_show_overview_all.setBackgroundResource(R.color.passiveButton);

                updateOverviewLastYear(lastYearList);
            }
        });

        btn_show_overview_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show_overview_last_fill.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_month.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_last_year.setBackgroundResource(R.color.passiveButton);
                btn_show_overview_all.setBackgroundResource(R.color.activeButton);

                updateOverviewAll(fullDataList);
            }
        });
}

    private void updateDisplayedData(ArrayList<FillEntry> sortedList) {
        int listSize = sortedList.size();

        btn_show_overview_last_fill.setEnabled(false);
        btn_show_overview_last_month.setEnabled(false);
        btn_show_overview_last_year.setEnabled(false);
        btn_show_overview_all.setEnabled(false);

        btn_show_overview_last_fill.setBackgroundResource(R.color.passiveButton);
        btn_show_overview_last_month.setBackgroundResource(R.color.passiveButton);
        btn_show_overview_last_year.setBackgroundResource(R.color.passiveButton);
        btn_show_overview_all.setBackgroundResource(R.color.passiveButton);

        if (listSize == 0) {
            tv_show_last_mileage.setText("n/a");
            tv_show_last_date.setText("n/a");

            tv_show_overview_mileage.setText("n/a");
            tv_show_overview_time.setText("n/a");
            tv_show_overview_cost.setText("n/a");
            tv_show_overview_liter.setText("n/a");
        } else if (listSize == 1) {
            btn_show_overview_all.setBackgroundResource(R.color.activeButton);

            tv_show_last_mileage.setText(String.valueOf(sortedList.get(0).getMileage() + " km"));
            tv_show_last_date.setText(String.valueOf(sortedList.get(0).getDate()));

            tv_show_overview_mileage.setText(String.valueOf(sortedList.get(0).getMileage() + " km"));
            tv_show_overview_cost.setText(String.valueOf(sortedList.get(0).getPrice()));
            tv_show_overview_liter.setText(String.valueOf(sortedList.get(0).getLiter()));

            String newestDate = String.valueOf(sortedList.get(0).getDate());
            String[] splittedNewestDate = newestDate.split("\\.");

            Log.d(LOGTAG, "Splitten war erfolgreich! " + newestDate + "(" + splittedNewestDate.length + ") = " + splittedNewestDate[0] + "," + splittedNewestDate[1] + "," + splittedNewestDate[2]);

            String displayTime = getDateDifference( splittedNewestDate );

            tv_show_overview_time.setText(String.valueOf(displayTime));
        } else {
            btn_show_overview_last_fill.setBackgroundResource(R.color.activeButton);

            btn_show_overview_last_fill.setEnabled(true);
            btn_show_overview_all.setEnabled(true);

            if (lastMonthList.size() >= 1) {
                btn_show_overview_last_month.setEnabled(true);
            }

            if (lastYearList.size() >= 1) {
                btn_show_overview_last_year.setEnabled(true);
            }

            tv_show_last_mileage.setText(String.valueOf(sortedList.get(0).getMileage() + " km"));
            tv_show_last_date.setText(String.valueOf(sortedList.get(0).getDate()));

            updateOverviewLastFill(fullDataList);
        }
    }

    private void updateOverviewAll(ArrayList<FillEntry> sortedList) {
        int listSize = sortedList.size()-1;

        int totalDistance = sortedList.get(0).getMileage();

        double sumCost = 0;
        double sumLiter = 0;

        for (FillEntry entry : sortedList) {
            sumCost += entry.getPrice();
            sumLiter += entry.getLiter();
        }

        tv_show_overview_mileage.setText(String.valueOf(totalDistance + " km"));
        tv_show_overview_liter.setText(String.valueOf(f.format((sumLiter / totalDistance) * 100))+ " / 100 km");
        tv_show_overview_cost.setText(String.valueOf(f.format(sumCost)));

        String latestDate = String.valueOf(sortedList.get(listSize).getDate());
        String[] splittedLatestDate = latestDate.split("\\.");

        Log.d(LOGTAG, "Splitten war erfolgreich! " + latestDate + "(" + splittedLatestDate.length + ") = " + splittedLatestDate[0] + "," + splittedLatestDate[1] + "," + splittedLatestDate[2]);

        String displayTime = getDateDifference( splittedLatestDate );

        tv_show_overview_time.setText(String.valueOf(displayTime));
    }

    private void updateOverviewLastFill(ArrayList<FillEntry> sortedList) {
        int listSize = sortedList.size()-1;
        int actualDistance = sortedList.get(0).getMileage() - sortedList.get(1).getMileage();

        tv_show_overview_mileage.setText(String.valueOf(actualDistance + " km"));
        tv_show_overview_cost.setText(String.valueOf(f.format(sortedList.get(0).getPrice())));
        tv_show_overview_liter.setText(String.valueOf(f.format((sortedList.get(0).getLiter() / actualDistance) *100)) + " / 100 km");

        String newestDate = String.valueOf(sortedList.get(0).getDate());
        String[] splittedNewestDate = newestDate.split("\\.");

        Log.d(LOGTAG, "Splitten war erfolgreich! " + newestDate + "(" + splittedNewestDate.length + ") = " + splittedNewestDate[0] + "," + splittedNewestDate[1] + "," + splittedNewestDate[2]);

        String displayTime = getDateDifference( splittedNewestDate );

        tv_show_overview_time.setText(String.valueOf(displayTime));
    }

    private void updateOverviewLastMonth(ArrayList<FillEntry> sortedList)
    {
        int listSize = sortedList.size()-1;
        int monthBeforeLastMonth = 0;
        if(lastCalendarMonth != 1)
        {
            monthBeforeLastMonth = lastCalendarMonth-1;
        }
        else
        {
            monthBeforeLastMonth = 12;
        }
        ArrayList<FillEntry> monthBeforeLastMonthSortedList = fmds.getlastMonth(monthBeforeLastMonth, actualCalendarYear);
        double sumCost = 0;
        double sumLiter = 0;
        int totalDistance = 0;

        if(listSize+1 == 1)
        {
            totalDistance = sortedList.get(0).getMileage() - monthBeforeLastMonthSortedList.get(0).getMileage();
        }
        else
        {
            totalDistance = sortedList.get(0).getMileage() - sortedList.get(listSize).getMileage();
        }

        for (FillEntry entry : sortedList) {
            sumCost += entry.getPrice();
            sumLiter += entry.getLiter();
        }

        tv_show_overview_mileage.setText(String.valueOf(totalDistance + " km"));
        tv_show_overview_cost.setText(String.valueOf(f.format(sumCost )));
        tv_show_overview_liter.setText(String.valueOf(f.format((sumLiter / totalDistance) *100)) + " / 100 km");
        tv_show_overview_time.setText(GetLastMonthName());
    }

    private void updateOverviewLastYear(ArrayList<FillEntry> sortedList) {
        int listSize = sortedList.size()-1;

        double sumCost = 0;
        double sumLiter = 0;
        int totalDistance = sortedList.get(0).getMileage() - sortedList.get(listSize).getMileage();

        for (FillEntry entry : sortedList) {
            sumCost += entry.getPrice();
            sumLiter += entry.getLiter();
        }

        tv_show_overview_mileage.setText(String.valueOf(totalDistance + " km"));
        tv_show_overview_cost.setText(String.valueOf(f.format(sumCost)));
        tv_show_overview_liter.setText(String.valueOf(f.format((sumLiter / totalDistance) * 100)) + " / 100 km");

        tv_show_overview_time.setText(String.valueOf(actualCalendarYear));
    }

    private String getDateDifference ( String[] splittedDifferenceDate ) {
        Calendar dateForDifferentiation = Calendar.getInstance();
        dateForDifferentiation.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDifferenceDate[0]));
        if ( splittedDifferenceDate[1].equals("0") ) {
            splittedDifferenceDate[1] = "11";
        } else {
            splittedDifferenceDate[1] = String.valueOf(Integer.parseInt(splittedDifferenceDate[1]) - 1);
        }
        dateForDifferentiation.set(Calendar.MONTH, Integer.parseInt(splittedDifferenceDate[1]));
        dateForDifferentiation.set(Calendar.YEAR, Integer.parseInt(splittedDifferenceDate[2]));

        int differentiationDay = dateForDifferentiation.get(Calendar.DAY_OF_MONTH);
        int differentiationMonth = dateForDifferentiation.get(Calendar.MONTH);
        int differentiationYear = dateForDifferentiation.get(Calendar.YEAR);

        Log.d(LOGTAG, "Letzter Eintrag: " + differentiationDay + "," + differentiationMonth + "," + differentiationYear);
        Log.d(LOGTAG, "Datum heute" + actualCalendarDay + "," + actualCalendarMonth + "," + actualCalendarYear);

        long dateDifference = calendar.getTimeInMillis() - dateForDifferentiation.getTimeInMillis();
        Log.d(LOGTAG, "Millisekunden dazwischen: " + dateDifference);
        int daysBetween = (int) (dateDifference / ( 24 * 60 * 60 * 1000));

        Log.d(LOGTAG, "daysBetween: " + daysBetween);

        String displayComment = "";



        if ( daysBetween == 0 ) {
            if ( actualCalendarDay == differentiationDay ) {
                displayComment = "heute";
            } else {
                displayComment = "gestern";
            }
        } else if ( daysBetween == 1 ) {
            displayComment = daysBetween + " Tag";
        }  else {
            displayComment = "Vor " + daysBetween + " Tagen";
        }

        return displayComment;
    }

    private String GetLastMonthName()
    {
        switch (lastCalendarMonth) {
            case 1:
                return "Januar";
            case 2:
                return "Februar";
            case 3:
                return "März";
            case 4:
                return "April";
            case 5:
                return "Mai";
            case 6:
                return "Juni";
            case 7:
                return "Juli";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "Oktober";
            case 11:
                return "November";
            case 12:
                return "Dezember";
        }
        return "";
    }
}
