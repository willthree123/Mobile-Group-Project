package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FinanceAmountPieChart extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Record> records;
    private List<Integer> availableYear_distinct;
    private ArrayList<String> availableMonth_distinct_name;

    private List<Integer> availableCategory_distinct;
    private PieChart pieChart;

    private String[] month;
    private ArrayList<String> months;

    private boolean Category_mode = false;
    private boolean Consume_mode = false;
    private boolean ViewingAll = true;
    private boolean ViewingYear = true;
    private boolean ViewingMonth = false;

    private Spinner spinner_year, spinner_month;
    private int selected_year, selected_month;

    private Date currentTime;
    private Calendar calendar;
    private Resources resources;

    private Button bt_view_all;
    private Button bt_view_year;
    private Button bt_change_view_mode;
    private Button bt_change_view_category;
    private Button bt_view_month, home;

    private ValueFormatter formatter;

    private TextView TextView_show_displaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_pie_chart);
        reloadLang(this);
        availableMonth_distinct_name = new ArrayList<>();
        months = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("record_data", null);
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        records = gson.fromJson(json, type);

//        resources = getResources();
        month = resources.getStringArray(R.array.months);
        for (int i = 0; i < month.length; i++) {
            months.add(month[i]);
        }


        bt_view_all = findViewById(R.id.piechart_view_all);
        bt_view_year = findViewById(R.id.piechart_view_year);
        bt_view_month = findViewById(R.id.piechart_view_month);
        bt_change_view_mode = findViewById(R.id.piechart_change_view_mode);
        bt_change_view_category = findViewById(R.id.piechart_change_view_category);
        TextView_show_displaying = findViewById(R.id.TextView_show_displaying);
        home = findViewById(R.id.currency_page_go_home);

        spinner_year = findViewById(R.id.spinner_year);
        spinner_month = findViewById(R.id.spinner_month);

        findAvailableCategory();
        findAvailableYear();

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.currency_converter_spinner, availableYear_distinct);
        adapter.setDropDownViewResource(R.layout.currency_converter_dropdown);
        spinner_year.setAdapter(adapter);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                ((TextView) adapterView.getChildAt(0)).setGravity(Gravity.CENTER);
                selected_year = availableYear_distinct.get(position);
                findAvailableMonth();
                if (ViewingYear || ViewingMonth)
                    display_mode_filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                ((TextView) adapterView.getChildAt(0)).setGravity(Gravity.CENTER);
                selected_month = months.indexOf(availableMonth_distinct_name.get(position));
                if (ViewingMonth)
                    display_mode_filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        bt_view_all.setOnClickListener(this);
        bt_view_year.setOnClickListener(this);
        bt_view_month.setOnClickListener(this);
        bt_change_view_mode.setOnClickListener(this);
        bt_change_view_category.setOnClickListener(this);
        home.setOnClickListener(this);


        formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        };

        pieChart = findViewById(R.id.PieChart);
        loadChartAll_Amount();
    }

    private void findAvailableMonth() {
        List<Integer> availableMonth = new ArrayList<>();
        availableMonth_distinct_name.clear();
        for (int i = 0; i <= records.size() - 1; i++) {
            if (records.get(i).getYear_int() == selected_year) {
                availableMonth.add(records.get(i).getMonth_int());
            }
        }
        List<Integer> availableMonth_distinct = availableMonth.stream().distinct().sorted().collect(Collectors.toList());
        for (int i = 0; i < availableMonth_distinct.size(); i++) {
            availableMonth_distinct_name.add(month[availableMonth_distinct.get(i)]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.currency_converter_spinner, availableMonth_distinct_name);
        adapter.setDropDownViewResource(R.layout.currency_converter_dropdown);
        spinner_month.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.piechart_view_all:
                ViewingAll = true;
                ViewingYear = false;
                ViewingMonth = false;
                resetAlpha();
                bt_view_all.animate().alpha(0.9f).setDuration(300);
                break;
            case R.id.piechart_view_year:
                ViewingAll = false;
                ViewingYear = true;
                ViewingMonth = false;
                resetAlpha();
                bt_view_year.animate().alpha(0.9f).setDuration(300);
                break;
            case R.id.piechart_view_month:
                ViewingAll = false;
                ViewingYear = false;
                ViewingMonth = true;
                resetAlpha();
                bt_view_month.animate().alpha(0.9f).setDuration(300);
                break;

            case R.id.piechart_change_view_mode: // CONSUME/REVENUE
                Consume_mode = !Consume_mode;
                if (Consume_mode) {
                    bt_change_view_mode.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_record_consume, null));
                } else {
                    bt_change_view_mode.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_record_rev, null));
                }
                break;
            case R.id.piechart_change_view_category:
                Category_mode = !Category_mode;
                if (!Category_mode) {
                    bt_change_view_category.setAlpha(1.0f);
                } else {

                    bt_change_view_category.animate().alpha(0.7f).setDuration(300);
                }
                break;
            case R.id.currency_page_go_home:
                Intent intent = new Intent(this, FinanceMainPage.class);
                intent.putExtra("is_saved", false);
                startActivity(intent);
                break;
        }
        display_mode_filter();
    }

    public void resetAlpha() {
        bt_view_all.setAlpha(0.3f);
        bt_view_month.setAlpha(0.3f);
        bt_view_year.setAlpha(0.3f);

        if (bt_view_all.getAlpha() != 0.3f) {
            bt_view_all.animate().alpha(0.3f).setDuration(300);
        }
        if (bt_view_month.getAlpha() != 0.3f) {
            bt_view_month.animate().alpha(0.3f).setDuration(300);
        }
        if (bt_view_year.getAlpha() != 0.3f) {
            bt_view_year.animate().alpha(0.3f).setDuration(300);
        }
    }

    private void display_mode_filter() {
        if (!Category_mode) {
            if (ViewingAll)
                loadChartAll_Amount();
            if (ViewingYear)
                loadChartYear_Amount();
            if (ViewingMonth)
                loadChartMonth_Amount();
        } else {
            if (ViewingAll)
                loadChartAll_Category();
            if (ViewingYear)
                loadChartYear_Category();
            if (ViewingMonth)
                loadChartMonth_Category();
        }
    }

    private void findAvailableYear() {

        ArrayList<Integer> availableYear = new ArrayList<>();
        for (int i = 0; i <= records.size() - 1; i++) {
            availableYear.add(records.get(i).getYear_int());
        }

        availableYear_distinct = availableYear.stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());
    }

    private void findAvailableCategory() {

        SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("record_data", null);
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        records = gson.fromJson(json, type);

        ArrayList<Integer> availableCategory = new ArrayList<>();
        for (int i = 0; i <= records.size() - 1; i++) {
            availableCategory.add(records.get(i).getCategory_type());
        }

        availableCategory_distinct = availableCategory.stream().distinct().collect(Collectors.toList());
    }

    private void loadChartAll_Amount() {
        ArrayList<YearCal> years;
        years = new ArrayList<>();
        TextView_show_displaying.setText(resources.getString(R.string.ViewAll));

        for (int i = 0; i <= availableYear_distinct.size() - 1; i++) {
            years.add(new YearCal(availableYear_distinct.get(i)));
        }

        for (int i = 0; i <= records.size() - 1; i++) {
            for (int m = 0; m <= years.size() - 1; m++) {
                if (records.get(i).getYear_int() == years.get(m).getYear_int() && records.get(i).isConsume() == Consume_mode) {
                    years.get(m).addAmount(records.get(i).getAmount());
                    break;
                }
            }
        }
        ArrayList<PieEntry> amounts = new ArrayList<>();
        for (int i = 0; i <= years.size() - 1; i++) {
            if (years.get(i).getAmount() != 0)
                amounts.add(new PieEntry((float) years.get(i).getAmount(), years.get(i).getYear()));
        }

        if (amounts.size() == 0) {
            TextView_show_displaying.setText(resources.getString(R.string.NoData));
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
        if (Consume_mode)
            pieChart.setCenterText(resources.getString(R.string.is_consume));
        else
            pieChart.setCenterText(resources.getString(R.string.is_revenue));
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

    private void loadChartYear_Amount() {
        ArrayList<MonthCal> months;
        months = new ArrayList<>();
        TextView_show_displaying.setText(resources.getString(R.string.ViewingMode) + Integer.toString(selected_year));

        for (int i = 0; i < 12; i++) {
            months.add(new MonthCal(i));
        }

        for (int i = 0; i <= records.size() - 1; i++) {
            for (int m = 0; m <= months.size() - 1; m++) {
                if (records.get(i).getMonth_int() == months.get(m).getMonth_int() && records.get(i).isConsume() == Consume_mode && records.get(i).getYear_int() == selected_year) {
                    months.get(m).addAmount(records.get(i).getAmount());
                    break;
                }
            }
        }

        ArrayList<PieEntry> amounts = new ArrayList<>();
        for (int i = 0; i <= months.size() - 1; i++) {
            if (months.get(i).getAmount() != 0) {
                amounts.add(new PieEntry((float) months.get(i).getAmount(), month[months.get(i).getMonth_int()]));
            }
        }

        if (amounts.size() == 0) {
            TextView_show_displaying.setText(resources.getString(R.string.NoData));
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
        if (Consume_mode)
            pieChart.setCenterText(resources.getString(R.string.is_consume));
        else
            pieChart.setCenterText(resources.getString(R.string.is_revenue));
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

    private void loadChartMonth_Amount() {
        ArrayList<MonthCal> months;
        months = new ArrayList<>();
        TextView_show_displaying.setText(resources.getString(R.string.ViewingMode)+ Integer.toString(selected_year) + " " + month[selected_month]);

        months.add(new MonthCal(selected_month));

        for (int i = 0; i <= records.size() - 1; i++) {
            if (records.get(i).getMonth_int() == months.get(0).getMonth_int() && records.get(i).isConsume() == Consume_mode && records.get(i).getYear_int() == selected_year) {
                months.get(0).addAmount(records.get(i).getAmount());
                break;
            }
        }

        ArrayList<PieEntry> amounts = new ArrayList<>();
        for (int i = 0; i <= months.size() - 1; i++) {
            if (months.get(i).getAmount() != 0) {
                amounts.add(new PieEntry((float) months.get(i).getAmount(), month[months.get(i).getMonth_int()]));
            }
        }

        if (amounts.size() == 0) {
            TextView_show_displaying.setText(resources.getString(R.string.NoData));
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
        if (Consume_mode)
            pieChart.setCenterText(resources.getString(R.string.is_consume));
        else
            pieChart.setCenterText(resources.getString(R.string.is_revenue));
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();

    }

    private void loadChartAll_Category() {
        ArrayList<CatCal> categories;
        categories = new ArrayList<>();
        TextView_show_displaying.setText(resources.getString(R.string.ViewingAll));

        for (int i = 0; i <= availableCategory_distinct.size() - 1; i++) {
            categories.add(new CatCal(availableCategory_distinct.get(i)));
        }
        for (int i = 0; i <= records.size() - 1; i++) {
            for (int m = 0; m <= categories.size() - 1; m++) {
                if (records.get(i).getCategory_type() == categories.get(m).getCategory() && records.get(i).isConsume() == Consume_mode) {
                    categories.get(m).addAmount(records.get(i).getAmount());
                    break;
                }
            }
        }

        ArrayList<PieEntry> amounts = new ArrayList<>();
        for (int i = 0; i <= categories.size() - 1; i++) {
            if (categories.get(i).getAmount() != 0)
                amounts.add(new PieEntry((float) categories.get(i).getAmount(), categories.get(i).getCategory_name(this)));
        }

        if (amounts.size() == 0) {
            TextView_show_displaying.setText(resources.getString(R.string.NoData));
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
        if (Consume_mode)
            pieChart.setCenterText(resources.getString(R.string.is_consume));
        else
            pieChart.setCenterText(resources.getString(R.string.is_revenue));
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

    private void loadChartYear_Category() {
        ArrayList<CatCal> categories;
        categories = new ArrayList<>();
        TextView_show_displaying.setText(resources.getString(R.string.ViewingMode) + Integer.toString(selected_year) + " ("+resources.getString(R.string.Category)+")");

        for (int i = 0; i <= availableCategory_distinct.size() - 1; i++) {
            categories.add(new CatCal(availableCategory_distinct.get(i)));
        }

        for (int i = 0; i <= records.size() - 1; i++) {
            for (int m = 0; m <= categories.size() - 1; m++) {
                if (records.get(i).getCategory_type() == categories.get(m).getCategory() && records.get(i).isConsume() == Consume_mode && records.get(i).getYear_int() == selected_year) {
                    categories.get(m).addAmount(records.get(i).getAmount());
                    break;
                }
            }
        }

        ArrayList<PieEntry> amounts = new ArrayList<>();
        for (int i = 0; i <= categories.size() - 1; i++) {
            if (categories.get(i).getAmount() != 0)
                amounts.add(new PieEntry((float) categories.get(i).getAmount(), categories.get(i).getCategory_name(this)));
        }

        if (amounts.size() == 0) {
            TextView_show_displaying.setText(resources.getString(R.string.NoData));
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
        if (Consume_mode)
            pieChart.setCenterText(resources.getString(R.string.is_consume));
        else
            pieChart.setCenterText(resources.getString(R.string.is_revenue));
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

    private void loadChartMonth_Category() {
        ArrayList<CatCal> categories;
        categories = new ArrayList<>();
        TextView_show_displaying.setText(resources.getString(R.string.ViewingMode) + Integer.toString(selected_year) + " " + month[selected_month] + " ("+resources.getString(R.string.Category)+")");

        for (int i = 0; i <= availableCategory_distinct.size() - 1; i++) {
            categories.add(new CatCal(availableCategory_distinct.get(i)));
        }

        for (int i = 0; i <= records.size() - 1; i++) {
            for (int m = 0; m <= categories.size() - 1; m++) {
                if (records.get(i).getCategory_type() == categories.get(m).getCategory() && records.get(i).isConsume() == Consume_mode && records.get(i).getYear_int() == selected_year && records.get(i).getMonth_int() == selected_month) {
                    categories.get(m).addAmount(records.get(i).getAmount());
                    break;
                }
            }
        }

        ArrayList<PieEntry> amounts = new ArrayList<>();
        for (int i = 0; i <= categories.size() - 1; i++) {
            if (categories.get(i).getAmount() != 0)
                amounts.add(new PieEntry((float) categories.get(i).getAmount(), categories.get(i).getCategory_name(this)));
        }

        if (amounts.size() == 0) {
            TextView_show_displaying.setText(resources.getString(R.string.NoData));
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
        if (Consume_mode)
            pieChart.setCenterText(resources.getString(R.string.is_consume));
        else
            pieChart.setCenterText(resources.getString(R.string.is_revenue));
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, FinanceMainPage.class);
        intent.putExtra("is_saved", false);
        startActivity(intent);
    }

    private void reloadLang(Context context) {
        //Lang Change
        String lang = SharedPreferenceHelper.getLanguage(context);
        context = LocaleHelper.setLocale(context, lang);
        resources = context.getResources();

        //Edit below
        //get element id
        Button a = findViewById(R.id.piechart_change_view_mode);
        a.setText(resources.getString(R.string.ConsumeNRev));

        a = findViewById(R.id.piechart_change_view_category);
        a.setText(resources.getString(R.string.CatAll));

        a = findViewById(R.id.piechart_view_all);
        a.setText(resources.getString(R.string.ViewAll));
        a = findViewById(R.id.piechart_view_year);
        a.setText(resources.getString(R.string.byYear));
        a = findViewById(R.id.piechart_view_month);
        a.setText(resources.getString(R.string.byMonth));

        return;

    }

}