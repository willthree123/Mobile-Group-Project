package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FinanceTracker extends AppCompatActivity implements View.OnClickListener {


    private EditText amount;
    private EditText description;
    private Button bt_consume;
    private Button revenue;
    private Button save;
    private Button bt_today;
    private Button bt_yesterday;
    private Button bt_dby;
    private Button bt_custom_date;

    private Date currentTime;;
    private Calendar calendar_temp_picker = Calendar.getInstance();

    private Calendar calendar;

    private DatePickerDialog.OnDateSetListener listener;

    private SimpleDateFormat dateFormat;

    private double money;
    private String money_str;
    private String description_str;
    private boolean consume = true;

    private Record record;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_tracker);
        save = findViewById(R.id.add_record_button);
        amount = findViewById(R.id.add_record_amount);
        description = findViewById(R.id.add_record_description);
        bt_consume =findViewById(R.id.consume_button);
        revenue=findViewById(R.id.revenue_button);
        bt_today=findViewById(R.id.add_record_date_today);
        bt_yesterday=findViewById(R.id.add_record_date_yesterday);
        bt_dby=findViewById(R.id.add_record_date_dby);
        bt_custom_date=findViewById(R.id.add_record_date_custom);

        Calendar calendar_temp;

        currentTime = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("dd/MM\nYYYY", Locale.getDefault());
        calendar_temp = Calendar.getInstance();
        calendar_temp.setTime(currentTime);
        calendar = Calendar.getInstance();
        calendar.setTime(currentTime);

        bt_today.setText(dateFormat.format(calendar_temp.getTime()));
        calendar_temp.add(Calendar.DATE, -1);
        bt_yesterday.setText(dateFormat.format(calendar_temp.getTime()));
        calendar_temp.add(Calendar.DATE, -1);
        bt_dby.setText(dateFormat.format(calendar_temp.getTime()));

        bt_today.setOnClickListener(this);
        bt_yesterday.setOnClickListener(this);
        bt_dby.setOnClickListener(this);
        bt_custom_date.setOnClickListener(this);
        bt_consume.setOnClickListener(this);
        revenue.setOnClickListener(this);
        save.setOnClickListener(this);

        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year,month,day);
                calendar_temp_picker.set(year,month,day);
                Calendar temp_cal = Calendar.getInstance();
                temp_cal.set(year,month,day);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM\nyyyy");
                String selected_date = format.format(temp_cal.getTime());
                bt_custom_date.setText(selected_date);
            }
        };

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_record_button:
                if (validate_record()) {
                    Intent intent = new Intent(this, FinanceMainPage.class);
                    intent.putExtra("is_saved", true);
                    record = new Record(money, calendar ,R.drawable.ic_launcher_foreground,description_str,consume,0);
                    intent.putExtra("record_info", record);
                    startActivity(intent);
                }
                break;
            case R.id.consume_button:
                consume = true;
                break;
            case R.id.revenue_button:
                consume = false;
                break;
            case R.id.add_record_date_today:
                calendar.setTime(currentTime);
                break;
            case R.id.add_record_date_yesterday:
                calendar.setTime(currentTime);
                calendar.add(Calendar.DATE, -1);
                break;
            case R.id.add_record_date_dby:
                calendar.setTime(currentTime);
                calendar.add(Calendar.DATE, -2);
                break;
            case R.id.add_record_date_custom:
                int year = calendar_temp_picker.get(Calendar.YEAR);
                int month = calendar_temp_picker.get(Calendar.MONTH);
                int day = calendar_temp_picker.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,listener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;
        }
    }


    public boolean validate_record(){
        try{
            money = Double.parseDouble(amount.getText().toString());
            money_str = amount.getText().toString();
        }
        catch(Exception exception){
            Toast.makeText(this, "The amount should be numerical", Toast.LENGTH_LONG).show();
            return (false);
        }
        try{
            description_str = description.getText().toString();
        }
        catch(Exception exception){
            return (false);
        }
        return (true);
    }

}