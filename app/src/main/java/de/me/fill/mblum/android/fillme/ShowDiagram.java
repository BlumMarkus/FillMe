package de.me.fill.mblum.android.fillme;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;

public class ShowDiagram extends AppCompatActivity {

    private RelativeLayout diagramLayout;
    private LineChart mChart;
    private Description desc;
    private FillMeDataSource fmds;
    private ArrayList<FillEntry> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diagram);

        diagramLayout = (RelativeLayout) findViewById(R.id.diagramLayout);

        mChart = (LineChart) findViewById(R.id.mChart);
        //customize line chart
        desc = new Description();
        desc.setText("Liter pro 100 Kilometer");
        mChart.setDescription(desc);
        mChart.setNoDataText("Keine Daten vorhanden!");

        //enable value highlighting
        mChart.setHighlightPerTapEnabled(true);
        mChart.setHighlightPerDragEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.LTGRAY);

        //datas
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setDrawGridLines(false);
        y1.setDrawGridLines(true);
        y1.setAxisMinimum(0);

        YAxis y12 = mChart.getAxisRight();
        y12.setEnabled(false);

        fmds = new FillMeDataSource(this);
        fillDiagram();
    }

    private void fillDiagram() {
        ArrayList<String> xDates = new ArrayList<>();
        ArrayList<Entry> yLiterPerKilometer = new ArrayList<>();
        list = fmds.getAllEntries();
        Collections.reverse(list);


        //X Wert ist das Datum von jedem Eintrag
        for (FillEntry entry: list) {
            if (entry != list.get(0)) {
                xDates.add(entry.getDate());
            }
        }

        //Y-Wert ist Liter pro 100 km
        //i = -1, erster Entry 0 an der 1. Stelle haben muss
        for (int i = (-1); i < list.size()-1; i++) {
            if (i != (-1)){
                yLiterPerKilometer.add(new Entry(i, (float)(list.get(i).getLiter()*100)/(list.get(i+1).getMileage()-list.get(i).getMileage())));
            }
        }

        LineDataSet dataSet = createSet(yLiterPerKilometer);
        LineData data = new LineData(dataSet);

        mChart.setData(data);
        XAxis xAxis = mChart.getXAxis();
        String[] xValues = xDates.toArray(new String[xDates.size()]);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
        xAxis.setGranularity(1f);

    }


    private LineDataSet createSet(ArrayList yData) {
        LineDataSet set = new LineDataSet(yData, "DataSet 1");
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.getFillAlpha();
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 177));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);

        return set;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter{
        private  String[] mValues;
        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
        }
    }
}

