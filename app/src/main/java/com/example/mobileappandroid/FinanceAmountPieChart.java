package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FinanceAmountPieChart extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Record> records;
    private List<Integer> availableYear_distinct;
    private PieChart pieChart;

    private boolean Category_mode = false;
    private boolean Consume_mode = false;
    private boolean Viewing_Year = true;

    private Spinner spinner;
    private int selected_year;

    private Date currentTime;
    private Calendar calendar;
    private Resources res;

    private Button bt_view_all;
    private Button bt_view_year;
    private Button bt_change_view_mode;

    private ValueFormatter formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_pie_chart);

        res = getResources();

        bt_view_all=findViewById(R.id.piechart_view_all);
        bt_view_year=findViewById(R.id.piechart_view_year);
        bt_change_view_mode=findViewById(R.id.piechart_change_view_mode);

        spinner=findViewById(R.id.spinner_year);

        if(findAvailableYear()){
            ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,availableYear_distinct);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    selected_year=availableYear_distinct.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            bt_view_all.setOnClickListener(this);
            bt_view_year.setOnClickListener(this);
            bt_change_view_mode.setOnClickListener(this);

            formatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.format("%.2f", value);
                }
            };

            pieChart = findViewById(R.id.PieChart);
            loadChartYear(Category_mode);
        }
        else{
            Toast.makeText(this, "No record is found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.piechart_view_all:
                loadChartYear(Category_mode);
                break;
            case R.id.piechart_view_year:
                loadChartMonth(selected_year,Category_mode);
                break;
            case R.id.piechart_change_view_mode:
                if(Consume_mode)
                    Consume_mode=false;
                else
                    Consume_mode=true;
                if(Viewing_Year)
                    loadChartYear(Category_mode);
                else
                    loadChartMonth(selected_year,Category_mode);
                break;
        }
    }

    private boolean findAvailableYear(){
        SharedPreferences sp=getSharedPreferences("Records",MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sp.getString("record_data",null);
        Type type=new TypeToken<ArrayList<Record>>(){}.getType();
        records=gson.fromJson(json,type);

        ArrayList<Integer> availableYear = new ArrayList<>();
        for (int i=0;i<=records.size()-1;i++) {
            availableYear.add(records.get(i).getYear_int());
        }

        availableYear_distinct = availableYear.stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());
        if(availableYear_distinct.size()>0){
            return true;
        }
        else{
            return false;
        }
    }

    private void loadChartYear(boolean Category_mode) {
        Viewing_Year = true;
        ArrayList<YearCal> years;
        years = new ArrayList<>();


        for(int i=0;i<=availableYear_distinct.size()-1;i++){
            years.add(new YearCal(availableYear_distinct.get(i)));
        }

        for (int i = 0; i <= records.size()-1; i++) {
            for(int m=0;m <= years.size()-1;m++){
                if(records.get(i).getYear_int()==years.get(m).getYear_int()&&records.get(i).isConsume()==Consume_mode){
                    years.get(m).addAmount(records.get(i).getAmount());
                }
            }
        }

        ArrayList<PieEntry> amounts = new ArrayList<>();
        for(int i=0;i <= years.size()-1;i++){
            amounts.add(new PieEntry((float)years.get(i).getAmount(),years.get(i).getYear()));
        }
        PieDataSet pieDataSet = new PieDataSet(amounts,"");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setValueFormatter(formatter);


        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        if(Consume_mode)
            pieChart.setCenterText("Consume");
        else
            pieChart.setCenterText("Revenue");
        pieChart.invalidate();

    }

    private void loadChartMonth(int selected_year,boolean Category_mode) {
        Viewing_Year = false;
        ArrayList<MonthCal> months;
        months = new ArrayList<>();

        for (int i=0;i<12;i++){
            months.add(new MonthCal(i));
        }

        for (int i = 0; i <= records.size()-1; i++) {
            for(int m=0;m <= months.size()-1;m++){
                if(records.get(i).getMonth_int()==months.get(m).getMonth_int()&&records.get(i).isConsume()==Consume_mode&&records.get(i).getYear_int()==selected_year){
                    months.get(m).addAmount(records.get(i).getAmount());
                    break;
                }
            }
        }

        ArrayList<PieEntry> amounts = new ArrayList<>();
        for(int i=0;i <= months.size()-1;i++){
            String[] month = res.getStringArray(R.array.months);
            if(months.get(i).getAmount()!=0){
                amounts.add(new PieEntry((float)months.get(i).getAmount(),month[months.get(i).getMonth_int()]));
            }
        }
        PieDataSet pieDataSet = new PieDataSet(amounts,"");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setValueFormatter(formatter);


        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        if(Consume_mode)
            pieChart.setCenterText("Consume");
        else
            pieChart.setCenterText("Revenue");
        pieChart.animate();
        pieChart.invalidate();

    }

    @Override
    public void onBackPressed() {
    }

}