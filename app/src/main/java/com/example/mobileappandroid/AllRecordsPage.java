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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AllRecordsPage extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Record> records;
    private RecyclerView rv_record;
    private RecyclerView.Adapter rv_adapter;
    private RecyclerView.LayoutManager rv_layoutManager;

    private boolean ViewingByCategory = false;
    private boolean ViewingByAll = true;
    private boolean ViewingByYear = false;
    private boolean ViewingByMonth = false;

    private Button ViewByCategory, ViewYear, ViewMonth, home;
    private Spinner spinner_category, spinner_year, spinner_month;

    private List<Integer> availableYear_distinct;

    private Resources res;

    private int selected_category, selected_year, selected_month;

Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_records_page);

        String lang = SharedPreferenceHelper.getLanguage(this);
        context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        res = resources;
        reloadLang(this);
        home = findViewById(R.id.currency_page_go_home);
        rv_record = findViewById(R.id.rv_record_allrecordspage);
        ViewByCategory = findViewById(R.id.AllRecordsPage_ViewByCategory);
        spinner_category = findViewById(R.id.AllRecordsPage_SpinnerCategory);
        spinner_year = findViewById(R.id.AllRecordsPage_SpinnerYear);
        ViewYear = findViewById(R.id.AllRecordsPage_ViewYear);
        ViewMonth = findViewById(R.id.AllRecordsPage_ViewMonth);
        spinner_month = findViewById(R.id.AllRecordsPage_SpinnerMonth);

        loadData();
        findAvailableYear();
        SpinnerSetup();
        home.setOnClickListener(this);
        ViewByCategory.setOnClickListener(this);
        ViewYear.setOnClickListener(this);
        ViewMonth.setOnClickListener(this);
    }


    private void SpinnerSetup() {
        String[] categories = res.getStringArray(R.array.financeNegCat);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.currency_converter_spinner, categories);
        adapter.setDropDownViewResource(R.layout.currency_converter_dropdown);
        spinner_category.setAdapter(adapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                ((TextView) adapterView.getChildAt(0)).setGravity(Gravity.CENTER);
                selected_category = position;
                searchRecord_distributor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this, R.layout.currency_converter_spinner, availableYear_distinct);
        adapter2.setDropDownViewResource(R.layout.currency_converter_dropdown);
        spinner_year.setAdapter(adapter2);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                ((TextView) adapterView.getChildAt(0)).setGravity(Gravity.CENTER);
                selected_year = availableYear_distinct.get(position);
                searchRecord_distributor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        String[] months = res.getStringArray(R.array.months);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.currency_converter_spinner, months);
        adapter3.setDropDownViewResource(R.layout.currency_converter_dropdown);
        spinner_month.setAdapter(adapter3);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                ((TextView) adapterView.getChildAt(0)).setGravity(Gravity.CENTER);
                selected_month = position;
                searchRecord_distributor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void findAvailableYear() {
        ArrayList<Integer> availableYear = new ArrayList<>();
        for (int i = 0; i <= records.size() - 1; i++) {
            availableYear.add(records.get(i).getYear_int());
        }

        availableYear_distinct = availableYear.stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("record_data", null);
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        if (json == null) {
            records = new ArrayList<>();
            SharedPreferences.Editor editor = sp.edit();
            editor = sp.edit();
            json = gson.toJson(records);
            editor.putString("record_data", json);
            editor.apply();
        }
        records = gson.fromJson(json, type);
        display_rv(records);
    }

    private void searchRecord_distributor() {

        if (ViewingByCategory) {
            if (ViewingByYear&&!ViewingByMonth)
                SearchRecordYear_Category();
            else if(ViewingByMonth)
                SearchRecordMonth_Category();
            else
                SearchRecordAll_Category();
        } else {
            if (ViewingByYear&&!ViewingByMonth)
                SearchRecordYear();
            else if(ViewingByMonth)
                SearchRecordMonth();
            else
                SearchRecordAll();
        }

    }

    private void display_rv(ArrayList viewing_list) {
        rv_record.setHasFixedSize(true);
        rv_layoutManager = new LinearLayoutManager(this);
        rv_adapter = new RecordAdapter(viewing_list);
        rv_record.setLayoutManager(rv_layoutManager);
        rv_record.setAdapter(rv_adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AllRecordsPage_ViewByCategory:
                ViewingByCategory=!ViewingByCategory;

                searchRecord_distributor();
                resetAlpha();
                break;
            case R.id.AllRecordsPage_ViewYear:

                ViewingByYear = !ViewingByYear;
                ViewingByMonth = false;
                ViewingByAll = false;
                searchRecord_distributor();
                resetAlpha();
                break;
            case R.id.AllRecordsPage_ViewMonth:

                ViewingByYear = true;
                ViewingByMonth = !ViewingByMonth;
                ViewingByAll = false;
                searchRecord_distributor();
                resetAlpha();
                break;
            case R.id.currency_page_go_home:
                Intent intent = new Intent(this, FinanceMainPage.class);
                intent.putExtra("is_saved", false);
                startActivity(intent);
                break;
        }
    }

    private void SearchRecordAll() {
        loadData();
        display_rv(records);
    }

    private void SearchRecordYear() {
        loadData();
        ArrayList<Record> records_temp = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getYear_int() == selected_year)
                records_temp.add(records.get(i));
        }
        display_rv(records_temp);
    }

    private void SearchRecordMonth() {
        loadData();
        ArrayList<Record> records_temp = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getYear_int() == selected_year && records.get(i).getMonth_int() == selected_month)
                records_temp.add(records.get(i));
        }
        display_rv(records_temp);
    }

    private void SearchRecordAll_Category() {
        loadData();
        ArrayList<Record> records_temp = new ArrayList<>();
        records_temp.clear();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getCategory_type() == selected_category)
                records_temp.add(records.get(i));
        }
        display_rv(records_temp);
        if (records_temp.size() == 0) {
            String[] categories = res.getStringArray(R.array.financeNegCat);
            Toast.makeText(this, "Record with " + categories[selected_category] + " is not found", Toast.LENGTH_SHORT).show();
        }
        display_rv(records_temp);
    }

    private void SearchRecordYear_Category() {
        loadData();
        ArrayList<Record> records_temp = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getYear_int() == selected_year && records.get(i).getCategory_type() == selected_category)
                records_temp.add(records.get(i));
        }
        display_rv(records_temp);
    }

    private void SearchRecordMonth_Category() {
        loadData();
        ArrayList<Record> records_temp = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getYear_int() == selected_year && records.get(i).getMonth_int() == selected_month && records.get(i).getCategory_type() == selected_category)
                records_temp.add(records.get(i));
        }
        display_rv(records_temp);
    }

    public void resetAlpha(){
        if(ViewByCategory.getAlpha()==0.3f&&ViewingByCategory) {
            ViewByCategory.setAlpha(0.3f);
            ViewByCategory.animate().alpha(0.9f).setDuration(300);
            ViewByCategory.setAlpha(0.9f);
        }
        else if(ViewByCategory.getAlpha()==0.9f&&!ViewingByCategory) {
            ViewByCategory.setAlpha(1.0f);
            ViewByCategory.animate().alpha(0.3f).setDuration(300);
            ViewByCategory.setAlpha(0.3f);
        }

        if(ViewMonth.getAlpha()==0.3f&&ViewingByMonth) {
            ViewMonth.setAlpha(0.3f);
            ViewMonth.animate().alpha(0.9f).setDuration(300);
            ViewMonth.setAlpha(0.9f);
        }
        else if(ViewMonth.getAlpha()==0.9f&&!ViewingByMonth) {
            ViewMonth.setAlpha(1.0f);
            ViewMonth.animate().alpha(0.3f).setDuration(300);
            ViewMonth.setAlpha(0.3f);
        }

        if(ViewYear.getAlpha()==0.3f&&ViewingByYear) {
            ViewYear.setAlpha(0.3f);
            ViewYear.animate().alpha(0.9f).setDuration(300);
            ViewYear.setAlpha(0.9f);
        }
        else if(ViewYear.getAlpha()==0.9f&&!ViewingByYear) {
            ViewYear.setAlpha(1.0f);
            ViewYear.animate().alpha(0.3f).setDuration(300);
            ViewYear.setAlpha(0.3f);
        }
    }

    private void reloadLang(Context context) {
        //Lang change
        String lang = SharedPreferenceHelper.getLanguage(context);
        context = LocaleHelper.setLocale(context, lang);
        Resources resources = context.getResources();

        //Edit below
        //get element id
        TextView a = findViewById(R.id.setting_screen_titile);
        TextView b = findViewById(R.id.tv_tracking_title1);
        Button c = findViewById(R.id.AllRecordsPage_ViewYear);
        Button d = findViewById(R.id.AllRecordsPage_ViewMonth);
        Button e = findViewById(R.id.AllRecordsPage_ViewByCategory);

        //get string
        a.setText(resources.getString(R.string.allRecord));
        b.setText(resources.getString(R.string.Filter));
        c.setText(resources.getString(R.string.byYear));
        d.setText(resources.getString(R.string.byMonth));
        e.setText(resources.getString(R.string.Category));
        return;
    }

}