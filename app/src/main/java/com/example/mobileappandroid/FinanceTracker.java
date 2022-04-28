package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
    private Button btn_fNeg1, btn_fNeg2, btn_fNeg3, btn_fNeg4, btn_fNeg5, btn_fNeg6, btn_fNeg7, btn_fNeg8, btn_fNeg9, btn_fNeg10, btn_fNeg11, btn_fNeg12, btn_fNeg13, btn_fNeg14, btn_fAdd1, btn_fAdd2, btn_fAdd3, btn_fAdd4;
    private Integer[] btn_id_array = {
            R.id.btn_fNeg1
            , R.id.btn_fNeg2
            , R.id.btn_fNeg3
            , R.id.btn_fNeg4
            , R.id.btn_fNeg5
            , R.id.btn_fNeg6
            , R.id.btn_fNeg7
            , R.id.btn_fNeg8
            , R.id.btn_fNeg9
            , R.id.btn_fNeg10
            , R.id.btn_fNeg11
            , R.id.btn_fNeg12
            , R.id.btn_fNeg13
            , R.id.btn_fNeg14
            , R.id.btn_fAdd1
            , R.id.btn_fAdd2
            , R.id.btn_fAdd3
            , R.id.btn_fAdd4
    };
    private int[] category_images = {
            R.drawable.ficon_01, R.drawable.ficon_02,
            R.drawable.ficon_03, R.drawable.ficon_04,
            R.drawable.ficon_05, R.drawable.ficon_06,
            R.drawable.ficon_07, R.drawable.ficon_08,
            R.drawable.ficon_09, R.drawable.ficon_10,
            R.drawable.ficon_11, R.drawable.ficon_12,
            R.drawable.ficon_13, R.drawable.ficon_14,
    };
    private int categoryPicked;

    private Date currentTime;
    ;
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
        bt_consume = findViewById(R.id.consume_button);
        revenue = findViewById(R.id.revenue_button);
        bt_today = findViewById(R.id.add_record_date_today);
        bt_yesterday = findViewById(R.id.add_record_date_yesterday);
        bt_dby = findViewById(R.id.add_record_date_dby);
        bt_custom_date = findViewById(R.id.add_record_date_custom);
        btn_fNeg1 = findViewById(R.id.btn_fNeg1);
        btn_fNeg2 = findViewById(R.id.btn_fNeg2);
        btn_fNeg3 = findViewById(R.id.btn_fNeg3);
        btn_fNeg4 = findViewById(R.id.btn_fNeg4);
        btn_fNeg5 = findViewById(R.id.btn_fNeg5);
        btn_fNeg6 = findViewById(R.id.btn_fNeg6);
        btn_fNeg7 = findViewById(R.id.btn_fNeg7);
        btn_fNeg8 = findViewById(R.id.btn_fNeg8);
        btn_fNeg9 = findViewById(R.id.btn_fNeg9);
        btn_fNeg10 = findViewById(R.id.btn_fNeg10);
        btn_fNeg11 = findViewById(R.id.btn_fNeg11);
        btn_fNeg12 = findViewById(R.id.btn_fNeg12);
        btn_fNeg13 = findViewById(R.id.btn_fNeg13);
        btn_fNeg14 = findViewById(R.id.btn_fNeg14);
        btn_fAdd1 = findViewById(R.id.btn_fAdd1);
        btn_fAdd2 = findViewById(R.id.btn_fAdd2);
        btn_fAdd3 = findViewById(R.id.btn_fAdd3);
        btn_fAdd4 = findViewById(R.id.btn_fAdd4);
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
        btn_fAdd1.setOnClickListener(this);
        btn_fAdd2.setOnClickListener(this);
        btn_fAdd3.setOnClickListener(this);
        btn_fAdd4.setOnClickListener(this);

        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                calendar_temp_picker.set(year, month, day);
                Calendar temp_cal = Calendar.getInstance();
                temp_cal.set(year, month, day);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM\nyyyy");
                String selected_date = format.format(temp_cal.getTime());
                bt_custom_date.setText(selected_date);
            }
        };
        resetAllAlpha();
    }

    @Override
    public void onClick(View view) {
        Button btn;
        switch (view.getId()) {
            case R.id.add_record_button:
                if (validate_record()) {
                    Intent intent = new Intent(this, FinanceMainPage.class);
                    intent.putExtra("is_saved", true);
                    record = new Record(money, calendar, category_images[categoryPicked], description_str, consume, categoryPicked);
                    intent.putExtra("record_info", record);
                    startActivity(intent);
                }
                break;
            case R.id.consume_button:
                consume = true;
                showCat(consume);
                break;
            case R.id.revenue_button:
                consume = false;
                showCat(consume);
                break;
            case R.id.add_record_date_today:
                calendar.setTime(currentTime);
                dateRestAlpha();
                btn = findViewById(view.getId());
                btn.setAlpha(0.4f);
                btn.animate().alpha(1.0f).setDuration(300);
                btn.setAlpha(1.0f);
                break;
            case R.id.add_record_date_yesterday:
                calendar.setTime(currentTime);
                calendar.add(Calendar.DATE, -1);
                dateRestAlpha();
                btn = findViewById(view.getId());
                btn.setAlpha(0.4f);
                btn.animate().alpha(1.0f).setDuration(300);
                btn.setAlpha(1.0f);
                break;
            case R.id.add_record_date_dby:
                calendar.setTime(currentTime);
                calendar.add(Calendar.DATE, -2);
                dateRestAlpha();
                btn = findViewById(view.getId());
                btn.setAlpha(0.4f);
                btn.animate().alpha(1.0f).setDuration(300);
                btn.setAlpha(1.0f);
                break;
            case R.id.add_record_date_custom:
                dateRestAlpha();
                int year = calendar_temp_picker.get(Calendar.YEAR);
                int month = calendar_temp_picker.get(Calendar.MONTH);
                int day = calendar_temp_picker.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, listener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

//                bt_dby.setText(dateFormat.format(calendar_temp.getTime()));
                break;
            case R.id.btn_fNeg1:

            case R.id.btn_fNeg2:

            case R.id.btn_fNeg3:

            case R.id.btn_fNeg4:

            case R.id.btn_fNeg5:

            case R.id.btn_fNeg6:

            case R.id.btn_fNeg7:

            case R.id.btn_fNeg8:

            case R.id.btn_fNeg9:

            case R.id.btn_fNeg10:

            case R.id.btn_fNeg11:

            case R.id.btn_fNeg12:

            case R.id.btn_fNeg13:

            case R.id.btn_fNeg14:

            case R.id.btn_fAdd1:

            case R.id.btn_fAdd2:

            case R.id.btn_fAdd3:

            case R.id.btn_fAdd4:


                categoryPicked = Arrays.asList(btn_id_array).indexOf(view.getId());
                resetAllAlpha();

                btn = findViewById(view.getId());
//
                Animation animation = new AlphaAnimation(0.3f, 1.0f);
                animation.setDuration(300);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                btn.startAnimation(animation);
                btn.setAlpha(1);
                break;
        }
    }

    public void showCat(boolean sta) {
        Button btn1, btn2;
        androidx.gridlayout.widget.GridLayout gridLayout1, gridLayout2;
        gridLayout1 = findViewById(R.id.gridLayout1);
        gridLayout2 = findViewById(R.id.gridLayout2);
        btn1 = findViewById(R.id.consume_button);
        btn2 = findViewById(R.id.revenue_button);

        if (sta) {
            gridLayout1.setVisibility(View.GONE);
            gridLayout2.setVisibility(View.VISIBLE);

            btn2.setAlpha(0.9f);
            btn2.animate().alpha(0.4f).setDuration(300);
            btn2.setAlpha(0.4f);

            btn1.setAlpha(0.4f);
            btn1.animate().alpha(0.9f).setDuration(300);
            btn1.setAlpha(0.9f);

        } else {
            gridLayout1.setVisibility(View.VISIBLE);
            gridLayout2.setVisibility(View.GONE);

            btn1.setAlpha(0.9f);
            btn1.animate().alpha(0.4f).setDuration(300);
            btn1.setAlpha(0.4f);

            btn2.setAlpha(0.4f);
            btn2.animate().alpha(0.9f).setDuration(300);
            btn2.setAlpha(0.9f);
        }
    }

    public void resetAllAlpha() {
        Button btn;
        for (int i = 0; i < btn_id_array.length; i++) {
            btn = findViewById(btn_id_array[i]);
            if (btn.getAlpha() == 1.0f) {
                btn.setAlpha(1.0f);
                btn.animate().alpha(0.3f).setDuration(300);
                btn.setAlpha(0.3f);
            }
        }
    }

    public void dateRestAlpha() {
        Button btn;
        btn = findViewById(R.id.add_record_date_today);
        if (btn.getAlpha() == 1.0f) {
            btn.setAlpha(1.0f);
            btn.animate().alpha(0.4f).setDuration(300);
            btn.setAlpha(0.4f);
        }

        btn = findViewById(R.id.add_record_date_yesterday);
        if (btn.getAlpha() == 1.0f) {
            btn.setAlpha(1.0f);
            btn.animate().alpha(0.4f).setDuration(300);
            btn.setAlpha(0.4f);
        }
        btn = findViewById(R.id.add_record_date_dby);
        if (btn.getAlpha() == 1.0f) {
            btn.setAlpha(1.0f);
            btn.animate().alpha(0.4f).setDuration(300);
            btn.setAlpha(0.4f);
        }
    }

    public boolean validate_record() {
        try {
            money = Double.parseDouble(amount.getText().toString());
            money_str = amount.getText().toString();
        } catch (Exception exception) {
            Toast.makeText(this, "The amount should be numerical", Toast.LENGTH_LONG).show();
            return (false);
        }
        try {
            description_str = description.getText().toString();
        } catch (Exception exception) {
            return (false);
        }
        return (true);
    }

}