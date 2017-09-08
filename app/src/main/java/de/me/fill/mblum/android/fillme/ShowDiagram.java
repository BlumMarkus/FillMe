package de.me.fill.mblum.android.fillme;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.Chart;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;

public class ShowDiagram extends AppCompatActivity {

    private LineChart mChart;
    private FillMeDataSource fmds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diagram);

        mChart = (LineChart) findViewById(R.id.mChartConsumption);
        //customize line chart
        Description desc = new Description();
        desc.setText("Liter pro 100 Kilometer");
        mChart.setDescription(desc);
        mChart.setNoDataText("Keine Daten vorhanden!");

        //no Data Label
        Paint p = mChart.getPaint(Chart.PAINT_INFO);
        p.setColor(Color.RED);
        p.setTextSize(60);

        //enable value highlighting
        mChart.setHighlightPerDragEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setDrawGridBackground(false);

        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.LTGRAY);

        mChart.animateXY(2000,2000);

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
        ArrayList<FillEntry> list = fmds.getAllEntries();
        Collections.reverse(list);


        //X Wert ist das Datum von jedem Eintrag
        for (FillEntry entry: list) {
            if (entry != list.get(0)) {
                xDates.add(entry.getDate());
            }
        }

        //Y-Wert ist Liter pro 100 km
        for (int i = 0; i < list.size(); i++) {
            if (i != 0){
                yLiterPerKilometer.add(new Entry(i-1, (float)(list.get(i).getLiter()*100)/(list.get(i).getMileage()-list.get(i-1).getMileage())));
            }
        }
        if (yLiterPerKilometer.size() != 0) {
            LineDataSet dataSet = createSet(yLiterPerKilometer);
            LineData data = new LineData(dataSet);

            mChart.setData(data);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            String[] xValues = xDates.toArray(new String[xDates.size()]);
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
            xAxis.setGranularity(1f);
        }
        else
            mChart.clear();
    }


    private LineDataSet createSet(ArrayList yData) {
        LineDataSet set = new LineDataSet(yData, "");
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

    private class MyXAxisValueFormatter implements IAxisValueFormatter{
        private  String[] mValues;
        private MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int intValue = (int) value;

            if (mValues.length > intValue && intValue >= 0) return mValues[intValue];

            return "";
        }
    }
}

