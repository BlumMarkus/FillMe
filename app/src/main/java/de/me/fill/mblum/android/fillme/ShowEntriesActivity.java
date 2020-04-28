package de.me.fill.mblum.android.fillme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowEntriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_entries);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView lv_showEntries_all = findViewById(R.id.lv_showEntries_all);
        ImageButton btn_newEntry_cancel = findViewById(R.id.btn_showEntries_cancel);

        FillMeDataSource fmds = new FillMeDataSource(this);
        ArrayList<FillEntry> list = fmds.getAllEntries();

        ShowEntriesListViewAdapter showEntriesListViewAdapter = new ShowEntriesListViewAdapter(this, list);
        lv_showEntries_all.setAdapter(showEntriesListViewAdapter);

        lv_showEntries_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(ShowEntriesActivity.this, EditEntryActivity.class);
                intent.putExtra("clickedItemID", v.getTag().toString());
                startActivity(intent);
            }
        });

        btn_newEntry_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}