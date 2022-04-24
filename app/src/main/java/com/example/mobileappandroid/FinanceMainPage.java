package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Calendar;

public class FinanceMainPage extends AppCompatActivity implements View.OnClickListener {


    private Button button_add_record;
    private Button bt_to_graph;
    public boolean is_saved;
    private ArrayList<Record> records;
    private Record record;
    private boolean RecordisEmpty=true;

//    private TextView test;
//    private String amount[];

    private RecyclerView rv_record;
    private RecyclerView.Adapter rv_adapter;
    private RecyclerView.LayoutManager rv_layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_main_page);
        loadData();
        is_saved = false;
        is_saved = getIntent().getExtras().getBoolean("is_saved");
        if(is_saved){
            saveData();
            loadData();
        }

        button_add_record = findViewById(R.id.add_record);
        bt_to_graph=findViewById(R.id.to_graph);
        button_add_record.setOnClickListener(this);
        bt_to_graph.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
    }

    private void saveData() {
        record = (Record) getIntent().getSerializableExtra("record_info");
        records.add(record);
        SharedPreferences sp=getSharedPreferences("Records",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Gson gson = new Gson();
        String json=gson.toJson(records);
        editor.putString("record_data",json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sp=getSharedPreferences("Records",MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sp.getString("record_data",null);
        Type type=new TypeToken<ArrayList<Record>>(){}.getType();

        if (json==null){
            RecordisEmpty=true;
            records= new ArrayList<>();
            // JustForTest
//            for(int i=2010;i<=2022;i++) {
//                Calendar cal = Calendar.getInstance();
//                Calendar cal2 = Calendar.getInstance();
//                cal2.set(i,1,1);
//                cal.set(i,0,1);
//                records.add(new Record((i-2000)*10, cal, R.drawable.ic_launcher_foreground, "No", true,0));
//                records.add(new Record((i-2000)*20, cal2, R.drawable.ic_launcher_foreground, "No", true,0));
//                records.add(new Record((i-2000)*10, cal, R.drawable.ic_launcher_foreground, "No", false,0));
//                records.add(new Record((i-2000)*40, cal2, R.drawable.ic_launcher_foreground, "No", false,0));
//            }
            // EndJustForTest

            SharedPreferences.Editor editor=sp.edit();
            editor=sp.edit();
            json=gson.toJson(records);
            editor.putString("record_data",json);
            editor.apply();
        }
        RecordisEmpty = false;
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
        switch (view.getId()){
            case R.id.add_record:
                Intent intent = new Intent(this, FinanceTracker.class);
                startActivity(intent);
                break;
            case R.id.to_graph:
                if(RecordisEmpty==false) {
                    Intent intent2 = new Intent(this, FinanceGraph.class);
                    startActivity(intent2);
                }
                else{
                    Toast.makeText(this, "No record to generate graph", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}