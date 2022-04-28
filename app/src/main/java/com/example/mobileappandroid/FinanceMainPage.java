package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;

public class FinanceMainPage extends AppCompatActivity implements View.OnClickListener {


    Button bt_add_record, home;
    private LinearLayout bt_to_graph;
    public boolean is_saved;
    private ArrayList<Record> records;
    private Record record;

//    private TextView test;
//    private String amount[];

    private RecyclerView rv_record;
    private RecyclerView.Adapter rv_adapter;
    private RecyclerView.LayoutManager rv_layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_main_page);
        reloadLang(this);
        loadData();
        is_saved = false;
        is_saved = getIntent().getExtras().getBoolean("is_saved");
        if (is_saved) {
            saveData();
            loadData();
        }
        home = findViewById(R.id.currency_page_go_home);
        bt_add_record = findViewById(R.id.add_record);
        bt_to_graph = findViewById(R.id.to_graph);
        home.setOnClickListener(this);
        bt_add_record.setOnClickListener(this);
        bt_to_graph.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveData() {
        record = (Record) getIntent().getSerializableExtra("record_info");
        records.add(record);
        SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(records);
        editor.putString("record_data", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("record_data", null);
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();

        if (json == null) {
            records = new ArrayList<>();
            // JustForTest
//            for(int i=2010;i<=2022;i++) {
//                Calendar cal = Calendar.getInstance();
//                Calendar cal2 = Calendar.getInstance();
//                cal2.set(i,1,1);
//                cal.set(i,0,1);
//                records.add(new Record((i-2000)*10, cal, R.drawable.ic_launcher_foreground, "No", true,0));
//                records.add(new Record((i-2000)*20, cal, R.drawable.ic_launcher_foreground, "No", true,1));
//                records.add(new Record((i-2000)*10, cal, R.drawable.ic_launcher_foreground, "No", false,2));
//                records.add(new Record((i-2000)*40, cal, R.drawable.ic_launcher_foreground, "No", false,3));
//            }
            // EndJustForTest
//            Calendar cal = Calendar.getInstance();
//            cal.set(2022,0,1);
//            for (int i=0;i<=13;i++){
//                records.add(new Record(100, cal, R.drawable.ic_launcher_foreground, "No", false,i));
//            }

            SharedPreferences.Editor editor = sp.edit();
            editor = sp.edit();
            json = gson.toJson(records);
            editor.putString("record_data", json);
            editor.apply();
        }
        records = gson.fromJson(json, type);
        rv_record = findViewById(R.id.rv_record);
        rv_record.setHasFixedSize(true);
        rv_layoutManager = new LinearLayoutManager(this);
        rv_adapter = new RecordAdapter(records);
        rv_record.setLayoutManager(rv_layoutManager);
        rv_record.setAdapter(rv_adapter);

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add_record:
                intent = new Intent(this, FinanceTracker.class);
                startActivity(intent);
                break;
            case R.id.to_graph:
                intent = new Intent(this, FinanceAmountPieChart.class);
                startActivity(intent);
                break;
            case R.id.currency_page_go_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void reloadLang(Context context) {
        //Lang change
        String lang = SharedPreferenceHelper.getLanguage(context);
        context = LocaleHelper.setLocale(context, lang);
        Resources resources = context.getResources();

        //Edit below
        //get element id
        TextView setting_screen_titile = findViewById(R.id.setting_screen_titile);
        TextView tv_tracking_title1 = findViewById(R.id.tv_tracking_title1);
        Button btn = findViewById(R.id.add_record);
        TextView textView10 = findViewById(R.id.textView10);
        TextView textView8 = findViewById(R.id.textView8);
        TextView tv_tracking_title = findViewById(R.id.tv_tracking_title);


        //get string
        setting_screen_titile.setText(resources.getString(R.string.Financial_Tracking));
        tv_tracking_title1.setText(resources.getString(R.string.recentRecords));
        btn.setText(resources.getString(R.string.plusadd_button));
        textView10.setText(resources.getString(R.string.viewDetail));
        textView8.setText(resources.getString(R.string.viewDetail));

        tv_tracking_title.setText(resources.getString(R.string.Charts));


        return;
    }
}