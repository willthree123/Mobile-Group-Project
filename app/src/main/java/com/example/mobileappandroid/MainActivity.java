package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button go_to_currency_cal;
    private Button to_finance;
    private ArrayList<Record> records;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      

        to_finance = findViewById(R.id.to_finance);
        to_finance.setOnClickListener(this);
        SharedPreferences sp=getSharedPreferences("Records",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();

        go_to_currency_cal = findViewById(R.id.to_currency_converter);
        go_to_currency_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, currency_converter.class);
                startActivity(intent);
            }
        });

        Button btnSetting = findViewById(R.id.button2);
        btnSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(view.getContext(), SettingActivity.class);
                startActivity(switchActivityIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.to_finance:
                Intent intent = new Intent(this, FinanceMainPage.class);
                intent.putExtra("is_saved", false);
                startActivity(intent);
                break;
        }

    }

}