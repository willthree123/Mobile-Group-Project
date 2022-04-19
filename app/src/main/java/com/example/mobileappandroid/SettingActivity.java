package com.example.mobileappandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    TextView displayText;
    Switch langSwitch;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        langSwitch=findViewById(R.id.lang_switch);
        displayText=findViewById(R.id.displayText);

        langSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String lanuage = "en";
                if(isChecked)lanuage="zh";

                context = LocaleHelper.setLocale(SettingActivity.this, lanuage);
                resources = context.getResources();
                displayText.setText(resources.getString(R.string.display_text));

            }
        });
    }

}