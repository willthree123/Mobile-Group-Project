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
    private Button btn_fNeg1, btn_fNeg2, btn_fNeg3, btn_fNeg4, btn_fNeg5, btn_fNeg6, btn_fNeg7, btn_fNeg8, btn_fNeg9, btn_fNeg10, btn_fNeg11, btn_fNeg12, btn_fNeg13, btn_fNeg14;

    private int[] category_images = {
            R.drawable.ficon_01,R.drawable.ficon_02,
            R.drawable.ficon_03,R.drawable.ficon_04,
            R.drawable.ficon_05,R.drawable.ficon_06,
            R.drawable.ficon_07,R.drawable.ficon_08,
            R.drawable.ficon_09,R.drawable.ficon_10,
            R.drawable.ficon_11,R.drawable.ficon_12,
            R.drawable.ficon_13,R.drawable.ficon_14,
    };
    private int categoryPicked;

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
        Intent intent = new Intent(this, FinanceMainPage.class);
        intent.putExtra("is_saved", false);
        startActivity(intent);
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
        btn_fNeg1=findViewById(R.id.btn_fNeg1);
        btn_fNeg2=findViewById(R.id.btn_fNeg2);
        btn_fNeg3=findViewById(R.id.btn_fNeg3);
        btn_fNeg4=findViewById(R.id.btn_fNeg4);
        btn_fNeg5=findViewById(R.id.btn_fNeg5);
        btn_fNeg6=findViewById(R.id.btn_fNeg6);
        btn_fNeg7=findViewById(R.id.btn_fNeg7);
        btn_fNeg8=findViewById(R.id.btn_fNeg8);
        btn_fNeg9=findViewById(R.id.btn_fNeg9);
        btn_fNeg10=findViewById(R.id.btn_fNeg10);
        btn_fNeg11=findViewById(R.id.btn_fNeg11);
        btn_fNeg12=findViewById(R.id.btn_fNeg12);
        btn_fNeg13=findViewById(R.id.btn_fNeg13);
        btn_fNeg14=findViewById(R.id.btn_fNeg14);
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
        btn_fNeg1.setOnClickListener(this);
        btn_fNeg2.setOnClickListener(this);
        btn_fNeg3.setOnClickListener(this);
        btn_fNeg4.setOnClickListener(this);
        btn_fNeg5.setOnClickListener(this);
        btn_fNeg6.setOnClickListener(this);
        btn_fNeg7.setOnClickListener(this);
        btn_fNeg8.setOnClickListener(this);
        btn_fNeg9.setOnClickListener(this);
        btn_fNeg10.setOnClickListener(this);
        btn_fNeg11.setOnClickListener(this);
        btn_fNeg12.setOnClickListener(this);
        btn_fNeg13.setOnClickListener(this);
        btn_fNeg14.setOnClickListener(this);


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
                    record = new Record(money, calendar ,category_images[categoryPicked],description_str,consume,categoryPicked);
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
            case R.id.btn_fNeg1:
                categoryPicked =0;
                break;
            case R.id.btn_fNeg2:
                categoryPicked =1;
                break;
            case R.id.btn_fNeg3:
                categoryPicked =2;
                break;
            case R.id.btn_fNeg4:
                categoryPicked =3;
                break;
            case R.id.btn_fNeg5:
                categoryPicked =4;
                break;
            case R.id.btn_fNeg6:
                categoryPicked =5;
                break;
            case R.id.btn_fNeg7:
                categoryPicked =6;
                break;
            case R.id.btn_fNeg8:
                categoryPicked =7;
                break;
            case R.id.btn_fNeg9:
                categoryPicked =8;
                break;
            case R.id.btn_fNeg10:
                categoryPicked =9;
                break;
            case R.id.btn_fNeg11:
                categoryPicked =10;
                break;
            case R.id.btn_fNeg12:
                categoryPicked =11;
                break;
            case R.id.btn_fNeg13:
                categoryPicked =12;
                break;
            case R.id.btn_fNeg14:
                categoryPicked =13;
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