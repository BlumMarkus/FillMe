package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;

public class ShowEntriesActivity extends AppCompatActivity {

    private FillMeDataSource fmds;
    private ArrayList<FillEntry> list;

    private ListView lv_showEntries_all;
    private ShowEntriesListViewAdapter showEntriesListViewAdapter;

    private ImageButton btn_showEntries_show_statistic_diagram;
    private ImageButton btn_showEntries_add_new_entry;
    private ImageButton btn_showEntries_show_settings;
    private ImageButton btn_showEntries_exit_app;
    private ImageButton btn_showEntries_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_entries);
    }

    @Override
    protected void onResume() {
        super.onResume();

        lv_showEntries_all = (ListView) findViewById(R.id.lv_showEntries_all);

        btn_showEntries_show_statistic_diagram = (ImageButton) findViewById(R.id.btn_showEntries_show_statistic_diagram);
        btn_showEntries_add_new_entry = (ImageButton) findViewById(R.id.btn_showEntries_add_new_entry);
        btn_showEntries_show_settings = (ImageButton) findViewById(R.id.btn_showEntries_show_settings);
        btn_showEntries_exit_app = (ImageButton) findViewById(R.id.btn_showEntries_exit_app);
        btn_showEntries_back = (ImageButton) findViewById(R.id.btn_showEntries_back);

        fmds = new FillMeDataSource(this);
        list = fmds.getAllEntries();

        showEntriesListViewAdapter = new ShowEntriesListViewAdapter(this, list);
        lv_showEntries_all.setAdapter(showEntriesListViewAdapter);

        lv_showEntries_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(ShowEntriesActivity.this, EditEntryActivity.class);
                intent.putExtra("clickedItemID", v.getTag().toString());
                startActivity(intent);
            }
        });

        btn_showEntries_show_statistic_diagram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowEntriesActivity.this, ShowDiagramActivity.class);
                startActivity(intent);
            }
        });

        btn_showEntries_add_new_entry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_showEntries_show_settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_showEntries_exit_app.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_showEntries_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}