package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

public class showEntriesActivity extends AppCompatActivity {

    private FillMeDataSource fmds;
    private ArrayList<FillEntry> list;
    private ListView lv_showAllEntries;
    private ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_entries);
    }

    @Override
    protected void onResume() {
        super.onResume();

        lv_showAllEntries = (ListView) findViewById(R.id.lv_showAllEntries);

        fmds = new FillMeDataSource(this);
        list = fmds.getAllEntries();

        listViewAdapter = new ListViewAdapter(this, list);
        lv_showAllEntries.setAdapter(listViewAdapter);

        lv_showAllEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(showEntriesActivity.this, EditEntryActivity.class);
                intent.putExtra("clickedItemID", v.getTag().toString());
                startActivity(intent);
            }
        });
    }
}