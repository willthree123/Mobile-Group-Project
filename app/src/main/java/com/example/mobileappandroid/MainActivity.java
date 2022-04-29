package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout go_to_currency_cal;
    private Button to_setting,button_add_record;
    private LinearLayout to_finance, to_interest;
    private ArrayList<Record> records;
    private PieChart pieChart;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reloadLang(MainActivity.this);

        to_finance = findViewById(R.id.to_finance);
        to_finance.setOnClickListener(this);

        go_to_currency_cal = findViewById(R.id.to_finance_checker_add_records);
        go_to_currency_cal.setOnClickListener(this);

        to_setting=findViewById(R.id.btn_setting);
        to_setting.setOnClickListener(this);

        pieChart=findViewById(R.id.PieChart_main);
        loadData();
        loadChart();


//        SharedPreferences sp=getSharedPreferences("Records",MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.clear();
//        editor.commit();

        reloadLang(MainActivity.this);


//         Intent intent = new Intent(this, AllRecordsPage.class);
//         startActivity(intent);

        to_interest = findViewById(R.id.to_interest);
        to_interest.setOnClickListener(this);
        button_add_record = findViewById(R.id.add_record);
        button_add_record.setOnClickListener(this);

    }

    private void loadData(){
        SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("record_data", null);
        Type type = new TypeToken<ArrayList<Record>>() {}.getType();
        if (json == null) {
            records = new ArrayList<>();
            SharedPreferences.Editor editor = sp.edit();
            editor = sp.edit();
            json = gson.toJson(records);
            editor.putString("record_data", json);
            editor.apply();
        }
        records = gson.fromJson(json, type);
    }

    private void loadChart(){
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        };
        Resources resources = getResources();
        String[] categories_name = resources.getStringArray(R.array.financeNegCat);
        ArrayList<CatCal> categories = new ArrayList<>();
        for (int i = 0; i < categories_name.length; i++) {
            categories.add(new CatCal(i));
        }

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        Log.d("Size",Integer.toString(records.size()));
        for (int i=0;i<records.size();i++)
        {
            for(int m=0;m<categories.size();m++)
            {
                Log.d("i",Integer.toString(i));
                if(records.get(i).getYear_int()==year&&records.get(i).getMonth_int()==month&&records.get(i).getCategory_type()==categories.get(m).getCategory()&&records.get(i).isConsume()==true)
                {
                    categories.get(m).addAmount(records.get(i).getAmount());
                    break;
                }
            }
        }

        ArrayList<PieEntry> amounts = new ArrayList<>();
        for (int i=0;i<categories.size();i++)
        {
            if(categories.get(i).getAmount()>0)
                amounts.add(new PieEntry((float) categories.get(i).getAmount(), categories_name[i]));
        }

        if (amounts.size() == 0) {
            pieChart.setVisibility(View.INVISIBLE);
        } else {
            pieChart.setVisibility(View.VISIBLE);
        }

        PieDataSet pieDataSet = new PieDataSet(amounts, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setValueFormatter(formatter);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(resources.getString(R.string.is_consume));
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

    @Override
    public void onBackPressed() {
//        reloadLang(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadLang(MainActivity.this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.to_finance:
                intent = new Intent(this, FinanceMainPage.class);
                intent.putExtra("is_saved", false);
                startActivity(intent);
                break;
            case R.id.to_finance_checker_add_records:
                intent = new Intent(this, currency_converter.class);
                startActivity(intent);
                break;
            case R.id.btn_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.add_record:
                intent = new Intent(this, FinanceTracker.class);
                startActivity(intent);
                break;
            case R.id.to_interest:
                intent = new Intent(this, Interest_calc.class);
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
        TextView tv_tool_title = findViewById(R.id.tv_tool_title);
        TextView add_interest_to_record = findViewById(R.id.add_interest_to_record);
        TextView add_interest_to_record2 = findViewById(R.id.add_interest_to_record2);
        TextView tv_tracking_title= findViewById(R.id.tv_tracking_title);
        TextView tv_tracking_title2 = findViewById(R.id.tv_tracking_title2);
        TextView textView8 = findViewById(R.id.textView8);
//        TextView  = findViewById(R.id.);
        Button add_record = findViewById(R.id.add_record);

        //get string
        tv_tool_title.setText(resources.getString(R.string.main_tools));
        add_interest_to_record.setText(resources.getString(R.string.main_currency_btn));
        add_interest_to_record2.setText(resources.getString(R.string.interestCalc));
        tv_tracking_title.setText(resources.getString(R.string.Financial_Tracking));
        tv_tracking_title2.setText(resources.getString(R.string.thisMonth));
        textView8.setText(resources.getString(R.string.viewDetail));
        add_record.setText(resources.getString(R.string.plusadd_button));

        return;
    }
}