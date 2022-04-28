package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Interest_calc extends AppCompatActivity {
    EditText interest_rate;
    EditText start_amount;
    EditText year;
    EditText result_interest;
    EditText result_interest_with_starting;

    Button clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_calc);


    }
}