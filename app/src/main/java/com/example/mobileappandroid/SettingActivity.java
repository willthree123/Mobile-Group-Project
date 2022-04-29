package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Resources resources;
    RadioButton radioButton, radiobtn_eng, radiobtn_chin, radiobtn_jp;
    RadioGroup radioGroup;
    Button home, clear, btn_load, btn_export;
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
        btn_load = findViewById(R.id.btn_load);
        btn_export = findViewById(R.id.btn_export);
        btn_load.setOnClickListener(SettingActivity.this);
        btn_export.setOnClickListener(SettingActivity.this);

        radiobtn_eng = findViewById(R.id.radio_eng);
        radiobtn_chin = findViewById(R.id.radio_chin);
        radiobtn_jp = findViewById(R.id.radio_jp);

//        Log.d("cry", SharedPreferenceHelper.getLanguage(SettingActivity.this));
        String a = SharedPreferenceHelper.getLanguage(SettingActivity.this).toString();

        if (a.matches("en")) {
            radiobtn_eng.setChecked(true);
        } else if (a.matches("zh")) {
            radiobtn_chin.setChecked(true);
        } else if (a.matches("ja")) {
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
                        SharedPreferenceHelper.setLanguage(SettingActivity.this, "ja");
                        break;
                }


                reloadLang(SettingActivity.this);

            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        LinearLayout linearLayout;
        ArrayList<Record> records;
        String json, lang;
        ClipboardManager clipboard;
        ClipData clip;
        EditText et;
        switch (view.getId()) {

            case R.id.currency_page_go_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_clear_all_records:
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                alert.setTitle(resources.getString(R.string.clearAll));
                alert.setMessage(resources.getString(R.string.clearConfirm));
                alert.setPositiveButton(resources.getString(R.string.clear), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String lang = SharedPreferenceHelper.getLanguage(SettingActivity.this);
                        context = LocaleHelper.setLocale(SettingActivity.this, lang);
                        resources = context.getResources();
                        Toast.makeText(context, resources.getString(R.string.cleared), Toast.LENGTH_SHORT).show();
                        SharedPreferenceHelper.clearRecords(SettingActivity.this);
                    }
                });
                alert.setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.create().show();
                break;
            case R.id.btn_load:
                lang = SharedPreferenceHelper.getLanguage(SettingActivity.this);
                context = LocaleHelper.setLocale(SettingActivity.this, lang);
                resources = context.getResources();
                SharedPreferenceHelper.setString(SettingActivity.this, "LoadConfirm", "key", "f");
                AlertDialog.Builder alert1 = new AlertDialog.Builder(SettingActivity.this);
                alert1.setTitle(resources.getString(R.string.loadTitle));
                alert1.setMessage(resources.getString(R.string.loadConfirm));
                alert1.setPositiveButton(resources.getString(R.string.loadNOverwrite), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferenceHelper.setString(SettingActivity.this, "LoadConfirm", "key", "t");
                        loadJson();
                    }
                });
                alert1.setNegativeButton((resources.getString(R.string.no)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert1.create().show();

                break;


            case R.id.btn_export:
                linearLayout = findViewById(R.id.export_layout);
                et = findViewById(R.id.exportjson_editText);

                //get lang
                lang = SharedPreferenceHelper.getLanguage(SettingActivity.this);
                context = LocaleHelper.setLocale(SettingActivity.this, lang);
                resources = context.getResources();

                //get json string
                SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
                Gson gson = new Gson();
                json = sp.getString("record_data", null);

                clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (json != null && json.length() > 5) {
                    et.setText(json);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout = findViewById(R.id.load_layout);
                    linearLayout.setVisibility(View.GONE);
                    clip = ClipData.newPlainText("Records json", json);
                    Toast.makeText(context, resources.getString(R.string.recordsCopied), Toast.LENGTH_SHORT).show();
                } else {
                    clip = ClipData.newPlainText("Record json", "");
                    Toast.makeText(context, resources.getString(R.string.recordsNull), Toast.LENGTH_SHORT).show();
                }
                clipboard.setPrimaryClip(clip);
                break;
        }
    }

    public void loadJson() {
        LinearLayout linearLayout;
        EditText et;
        ClipboardManager clipboard;
        ClipData clip;
        if (SharedPreferenceHelper.getString(this, "LoadConfirm", "key") == "t") {

//                btn_load.setVisibility(View.GONE);

            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String pasteData = "";

            // If it does contain data, decide if you can handle the data.
            if (!(clipboard.hasPrimaryClip())) {
                Toast.makeText(context, "ERROR: Clipboard is null", Toast.LENGTH_SHORT).show();
                clip = ClipData.newPlainText("Record json", "");
                clipboard.setPrimaryClip(clip);
                SharedPreferenceHelper.clearRecords(this);
                return;
            } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))) {

                // since the clipboard has data but it is not plain text
                Toast.makeText(context, "ERROR: Clipboard Not Plain Text", Toast.LENGTH_SHORT).show();
                clip = ClipData.newPlainText("Record json", "");
                clipboard.setPrimaryClip(clip);
                SharedPreferenceHelper.clearRecords(this);
                return;

            } else {

                //since the clipboard contains plain text.
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                // Gets the clipboard as text.
                pasteData = item.getText().toString();
//                Log.d("cry",pasteData.substring(3, 15));
                if (pasteData.length() > 20 && pasteData.substring(3, 16).equals("Category_type")) {
                    linearLayout = findViewById(R.id.export_layout);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout = findViewById(R.id.load_layout);
                    linearLayout.setVisibility(View.VISIBLE);
                    et = findViewById(R.id.loadjson_editText);
                    et.setText(pasteData);

                    SharedPreferenceHelper.clearRecords(this);

                    SharedPreferences sp_load = getSharedPreferences("Records", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp_load.edit();
                    editor.putString("record_data", pasteData);
                    editor.apply();
                    try {
                        loadData();
                    } catch (Exception e) {
                        Toast.makeText(context, "ERROR: NOT MATCHING JSON", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(context, resources.getString(R.string.loadedRecord), Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferenceHelper.clearRecords(this);
                    Toast.makeText(context, "ERROR: NOT MATCHING JSON", Toast.LENGTH_SHORT).show();
                    clip = ClipData.newPlainText("Record json", "");
                    clipboard.setPrimaryClip(clip);
                    return;
                }
            }
        }
    }

    private ArrayList<Record> records;

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("record_data", null);
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        if (json == null) {
            records = new ArrayList<>();
            SharedPreferences.Editor editor = sp.edit();
            editor = sp.edit();
            json = gson.toJson(records);
            editor.putString("record_data", json);
            editor.apply();
        }
        records = gson.fromJson(json, type);
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
        TextView tv_tracking_title = findViewById(R.id.tv_tracking_title);
        TextView tv_export_title = findViewById(R.id.tv_export_title);

        TextView textView6 = findViewById(R.id.textView6);
        TextView textView4 = findViewById(R.id.textView4);
        TextView textView7 = findViewById(R.id.textView7);


        Button btn_clear_all_records = findViewById(R.id.btn_clear_all_records);
        Button btn_export = findViewById(R.id.btn_export);
        Button btn_load = findViewById(R.id.btn_load);

        //get string
        tv_lang_title.setText(resources.getString(R.string.Language));
        setting_screen_titile.setText(resources.getString(R.string.setting_titile));
        tv_tracking_title.setText(resources.getString(R.string.Tracking));
        btn_clear_all_records.setText(resources.getString(R.string.btn_clear_tracking));
        tv_export_title.setText(resources.getString(R.string.export));
        textView6.setText(resources.getString(R.string.loadDes));
        textView4.setText(resources.getString(R.string.backupDes));
        textView7.setText(resources.getString(R.string.warning));
        btn_export.setText(resources.getString(R.string.btn_export));
        btn_load.setText(resources.getString(R.string.btn_load));

        if (!isOnCreate) {
            Toast.makeText(context, resources.getString(R.string.display_text), Toast.LENGTH_SHORT).show();
        }
        return;

    }
}
