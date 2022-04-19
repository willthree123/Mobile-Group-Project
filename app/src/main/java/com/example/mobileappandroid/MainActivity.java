package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button go_to_currency_cal;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}