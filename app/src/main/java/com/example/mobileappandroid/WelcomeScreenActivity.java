package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class WelcomeScreenActivity extends AppCompatActivity {
    String welcome_sta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        //delay in ms
        int DELAY = 2300;

        TextView a =findViewById(R.id.textView5);
        a.setText(SharedPreferenceHelper.getLangResources(this).getString(R.string.welcome_slo));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, DELAY);
    }
}