package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecordDetailPage extends AppCompatActivity implements View.OnClickListener {

    private EditText et_amount;
    private TextView tv_date;
    private EditText et_description;

    private Record record;

    private Button enable_edit;
    private Button bt_save;

    private ArrayList<Record> records;
    Resources resources;
    private int record_position;
    private boolean editable;
    private double new_amount;
    private String new_description;

    private Calendar calendar_picker = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail_page);
        reloadLang();
        et_amount = findViewById(R.id.record_detail_amount);
        tv_date = findViewById(R.id.record_detail_date);
        et_description = findViewById(R.id.record_detail_description);
        bt_save = findViewById(R.id.record_edit_save);
        enable_edit = findViewById(R.id.record_enable_edit);
        Button home =findViewById(R.id.currency_page_go_home);

        record = (Record) getIntent().getSerializableExtra("record_info");
        record_position = getIntent().getIntExtra("record_position", record_position);

        et_amount.setText(String.format("%.2f", record.getAmount()));
        tv_date.setText(record.getDate());
        et_description.setText(record.getDescription());

        et_amount.setEnabled(false);
        tv_date.setEnabled(false);
        et_description.setEnabled(false);

        tv_date.setOnClickListener(this);
        enable_edit.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        home.setOnClickListener(this);
        reloadLang(this);


        calendar_picker = record.getDate_calendar();
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar_picker.set(year, month, day);
                Calendar temp_cal = Calendar.getInstance();
                temp_cal.set(year, month, day);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String selected_date = format.format(temp_cal.getTime());
                tv_date.setText(selected_date);
            }
        };

    }

    @Override
    public void onClick(View view) {
        LinearLayout l5 = findViewById(R.id.l5);
        switch (view.getId()) {
            case R.id.record_enable_edit:
                if (editable) {
                    editable = false;
                    et_amount.setEnabled(false);
                    tv_date.setEnabled(false);
                    et_description.setEnabled(false);
                    l5.setAlpha(0.5f);
                } else {
                    editable = true;
                    et_amount.setEnabled(true);
                    tv_date.setEnabled(true);
                    et_description.setEnabled(true);
                    l5.setAlpha(1.0f);
                }
                break;
            case R.id.record_detail_date:
                int year = calendar_picker.get(Calendar.YEAR);
                int month = calendar_picker.get(Calendar.MONTH);
                int day = calendar_picker.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, listener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;
            case R.id.record_edit_save:
                if (InputValidator()) {
                    SharedPreferences sp = getSharedPreferences("Records", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sp.getString("record_data", null);
                    Type type = new TypeToken<ArrayList<Record>>() {
                    }.getType();
                    records = gson.fromJson(json, type);

                    new_description = et_description.getText().toString();

                    records.get(record_position).setAmount(new_amount);
                    records.get(record_position).setDescription(new_description);
                    records.get(record_position).setDate(calendar_picker);

                    SharedPreferences.Editor editor = sp.edit();
                    editor = sp.edit();
                    json = gson.toJson(records);
                    editor.putString("record_data", json);
                    editor.apply();
                    Intent intent = new Intent(this, FinanceMainPage.class);
                    intent.putExtra("is_saved", false);
                    startActivity(intent);
                }
                break;
            case R.id.currency_page_go_home:
                Intent intent = new Intent(this, FinanceMainPage.class);
                intent.putExtra("is_saved", false);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, FinanceMainPage.class);
        intent.putExtra("is_saved", false);
        startActivity(intent);
    }

    public void reloadLang() {
        resources = SharedPreferenceHelper.getLangResources(this);

        TextView a = findViewById(R.id.setting_screen_titile);
        a.setText(resources.getString(R.string.Details));
        a = findViewById(R.id.tv_lang_title1);
        a.setText(resources.getString(R.string.Amount));
        a = findViewById(R.id.tv_lang_title2);
        a.setText(resources.getString(R.string.Description));
        a = findViewById(R.id.tv_lang_title);
        a.setText(resources.getString(R.string.Date));
        Button b = findViewById(R.id.record_edit_save);
        b.setText(resources.getString(R.string.Save));
        b = findViewById(R.id.record_enable_edit);
        b.setText(resources.getString(R.string.Edit));
    }

    private boolean InputValidator()
    {
        try{
            new_amount = Double.parseDouble(et_amount.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "The amount should be numerical", Toast.LENGTH_LONG).show();
            return (false);
        }
        return true;
    }

    private void reloadLang(Context context) {
        //Lang change
        String lang = SharedPreferenceHelper.getLanguage(context);
        context = LocaleHelper.setLocale(context, lang);
        Resources resources = context.getResources();

        //Edit below
        //get element id
        TextView title_text_view = findViewById(R.id.setting_screen_titile);
        TextView amount_text_view = findViewById(R.id.tv_lang_title1);
        TextView description_text_view = findViewById(R.id.tv_lang_title2);
        TextView date_text_view = findViewById(R.id.tv_lang_title);
        Button save_button_text = findViewById(R.id.record_edit_save);
        Button edit_button_text = findViewById(R.id.record_enable_edit);

        //get string
        title_text_view.setText(resources.getString(R.string.record_detail_title));
        amount_text_view.setText((resources.getString(R.string.record_detail_amount)));
        description_text_view.setText(resources.getString(R.string.record_detail_description));
        date_text_view.setText(resources.getString(R.string.record_detail_date));
        save_button_text.setText(resources.getString(R.string.record_detail_save));
        edit_button_text.setText(resources.getString(R.string.record_detail_edit));


        return;
    }

}