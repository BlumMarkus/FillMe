package de.me.fill.mblum.android.fillme;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    SimpleDateFormat dateFormat;
    ArrayList<FillEntry> list;
    Context context;

    private String LOG_TAG = "ListViewAdapter";

    private TextView tv_date;
    private TextView tv_mileage;
    private TextView tv_status;
    private TextView tv_price;
    private TextView tv_liter;

    private String status;

    public ListViewAdapter(Context context, ArrayList<FillEntry> list) {
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
        View customView = View.inflate(context, R.layout.custom_list, null);

        tv_date = (TextView) customView.findViewById(R.id.tv_date);
        tv_mileage = (TextView) customView.findViewById(R.id.tv_mileage);
        tv_status = (TextView) customView.findViewById(R.id.tv_status);
        tv_price = (TextView) customView.findViewById(R.id.tv_price);
        tv_liter = (TextView) customView.findViewById(R.id.tv_liter);

        if (list.get(position).getStatus() == 1) {
            status = "Usereingabe";
        } else {
            status = "Generiert";
        }

        tv_date.setText(String.valueOf(list.get(position).getDate()));
        tv_mileage.setText(String.valueOf(list.get(position).getMileage() + " km"));
        tv_status.setText(status);
        tv_price.setText(String.valueOf(list.get(position).getPrice() + " €"));
        tv_liter.setText(String.valueOf(list.get(position).getLiter() + " l"));

        customView.setTag(list.get(position).getID());

        return customView;
    }
}
