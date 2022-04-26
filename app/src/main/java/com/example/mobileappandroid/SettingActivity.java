package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Resources resources;
    RadioButton radioButton, radiobtn_eng, radiobtn_chin,radiobtn_jp;
    RadioGroup radioGroup;
    Button home, clear;
    private boolean isOnCreate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        reloadLang(SettingActivity.this);
        isOnCreate = false;

        home = findViewById(R.id.currency_page_go_home);
        home.setOnClickListener(SettingActivity.this);
        clear = findViewById(R.id.btn_clear_all_records);
        clear.setOnClickListener(SettingActivity.this);

        radiobtn_eng = findViewById(R.id.radio_eng);
        radiobtn_chin = findViewById(R.id.radio_chin);
        radiobtn_jp = findViewById(R.id.radio_jp);

//        Log.d("cry", SharedPreferenceHelper.getLanguage(SettingActivity.this));
        String a = SharedPreferenceHelper.getLanguage(SettingActivity.this).toString();

        if (a.matches("en")) {
            radiobtn_eng.setChecked(true);
        } else if (a.matches("zh")) {
            radiobtn_chin.setChecked(true);
        }
        else if (a.matches("jp")) {
            radiobtn_jp.setChecked(true);
        }

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
                    case R.id.radio_jp:
                        SharedPreferenceHelper.setLanguage(SettingActivity.this, "jp");
                        break;
                }


                reloadLang(SettingActivity.this);

            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.currency_page_go_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_clear_all_records:
                Log.d("cry", "Pressed");
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                alert.setTitle("CLEAR ALL RECORDS");
                alert.setMessage("Are you sure you want to clear all records?");
                alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String lang = SharedPreferenceHelper.getLanguage(SettingActivity.this);
                        context = LocaleHelper.setLocale(SettingActivity.this, lang);
                        resources = context.getResources();
                        Toast.makeText(context, resources.getString(R.string.cleared), Toast.LENGTH_SHORT).show();
                        SharedPreferenceHelper.clearRecords(SettingActivity.this);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.create().show();
                break;
        }
    }

    private void reloadLang(Context context) {
        //Lang Change
        String lang = SharedPreferenceHelper.getLanguage(context);
        context = LocaleHelper.setLocale(context, lang);
        resources = context.getResources();

        //Edit below
        //get element id
        TextView tv_lang_title = findViewById(R.id.tv_lang_title);
        TextView setting_screen_titile = findViewById(R.id.setting_screen_titile);
        TextView tv_tracking_title=findViewById(R.id.tv_tracking_title);
        Button btn_clear_all_records = findViewById(R.id.btn_clear_all_records);

        //get string
        tv_lang_title.setText(resources.getString(R.string.Language));
        setting_screen_titile.setText(resources.getString(R.string.setting_titile));
        tv_tracking_title.setText(resources.getString(R.string.Tracking));
        btn_clear_all_records.setText(resources.getString(R.string.btn_clear_tracking));

        if (!isOnCreate) {
            Toast.makeText(context, resources.getString(R.string.display_text), Toast.LENGTH_SHORT).show();
        }
        return;

    }

}