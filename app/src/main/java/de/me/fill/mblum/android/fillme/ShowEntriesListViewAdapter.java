package de.me.fill.mblum.android.fillme;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShowEntriesListViewAdapter extends BaseAdapter {

    private ArrayList<FillEntry> list;
    private Context context;

    private String LOG_TAG = "ListViewAdapter";

    ShowEntriesListViewAdapter(Context context, ArrayList<FillEntry> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = View.inflate(context, R.layout.format_listview_showentries, null);

        TextView tv_dayOfMonth = customView.findViewById(R.id.tv_showEntries_dayOfMonth);
        TextView tv_monthShort = customView.findViewById(R.id.tv_showEntries_monthShort);
        TextView tv_mileage = customView.findViewById(R.id.tv_showEntries_mileage);
        TextView tv_price = customView.findViewById(R.id.tv_showEntries_price);
        TextView tv_liter = customView.findViewById(R.id.tv_showEntries_liter);

        tv_dayOfMonth.setText(String.valueOf(list.get(position).getDayOfMonth()));
        tv_monthShort.setText(String.valueOf(list.get(position).getMonthShort()));
        tv_mileage.setText(String.valueOf(list.get(position).getMileage()));

        DecimalFormat format = new DecimalFormat("0.00");

        tv_price.setText(String.valueOf(format.format(list.get(position).getPrice())));
        tv_liter.setText(String.valueOf(format.format(list.get(position).getLiter())));

        customView.setTag(list.get(position).getID());

        return customView;
    }
}
