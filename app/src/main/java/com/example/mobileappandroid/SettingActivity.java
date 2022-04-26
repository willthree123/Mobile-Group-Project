package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class SettingActivity extends AppCompatActivity {
    Context context;
    Resources resources;
    RadioButton radioButton;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);


        reloadLang(SettingActivity.this);

        radioGroup = findViewById(R.id.radio_group_lan);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int btn_id = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(btn_id);

                switch (btn_id) {
                    case R.id.radio_eng:
                        SharedPreferenceHelper.setLanguage(SettingActivity.this, "en");


                        break;
                    case R.id.radio_chin:
                        SharedPreferenceHelper.setLanguage(SettingActivity.this, "zh");
                        break;
                }


                reloadLang(SettingActivity.this);


            }
        });

    }

    private void reloadLang(Context context) {
        //Lang Change
        String lang = SharedPreferenceHelper.getLanguage(context);
        context = LocaleHelper.setLocale(context, lang);
        Resources resources = context.getResources();

        //Edit below
        //get element id
        TextView tv_lang_title = findViewById(R.id.tv_lang_title);
        TextView setting_screen_titile = findViewById(R.id.setting_screen_titile);

        //get string
        tv_lang_title.setText(resources.getString(R.string.Language));
        setting_screen_titile.setText(resources.getString(R.string.setting_titile));
        Toast.makeText(context, resources.getString(R.string.display_text), Toast.LENGTH_SHORT).show();
        return;
    }

}