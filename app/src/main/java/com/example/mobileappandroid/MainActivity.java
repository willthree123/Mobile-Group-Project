package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout go_to_currency_cal;
    private Button to_setting,button_add_record;
    private LinearLayout to_finance, to_interest;
    private ArrayList<Record> records;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        to_finance = findViewById(R.id.to_finance);
        to_finance.setOnClickListener(this);

        go_to_currency_cal = findViewById(R.id.to_finance_checker_add_records);
        go_to_currency_cal.setOnClickListener(this);

        to_setting=findViewById(R.id.btn_setting);
        to_setting.setOnClickListener(this);
//        SharedPreferences sharedPreferences = getSharedPreferences("KEY",MODE_PRIVATE);
//        SharedPreferences.Editor editor= sharedPreferences.edit();
//        editor.putString();
//        SharedPreferences sp=getSharedPreferences("Records",MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.clear();
//        editor.commit();
        reloadLang(MainActivity.this);
        to_interest = findViewById(R.id.to_interest);
        to_interest.setOnClickListener(this);
        button_add_record = findViewById(R.id.add_record);
        button_add_record.setOnClickListener(this);
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
                intent = new Intent(this, InterestCalc.class);
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

        //get string
        tv_tool_title.setText(resources.getString(R.string.main_tools));
        return;
    }
}