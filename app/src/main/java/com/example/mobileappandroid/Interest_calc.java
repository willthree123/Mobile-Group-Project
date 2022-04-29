package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Interest_calc extends AppCompatActivity {
    EditText interest_rate;
    EditText start_amount;
    EditText year;
    EditText result_interest;
    EditText result_interest_with_starting;

    Button clear;
    ImageButton home;

    LinearLayout add_interest_only;
    LinearLayout add_interest_with_starting;

    Spinner interest_spinner;

    String[] spinner_array = {"simple interest","compound interest"};

    String interest_rate_get;
    String starting_get;
    String year_get;
    String interest_spinner_get;

    Double interest_rate_double;
    Double starting_double;
    Double year_double;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_calc);
        set_component();
        interest_spinner_settings();
        set_interest_rate_listener();
        set_start_amount_listener();
        set_year_listener();
        clear_on_click();
        home_on_click();
        add_interest_only_on_click();
        add_interest_with_starting_on_click();
        reloadLang(this);
    }

    private void set_component(){

        interest_rate = findViewById(R.id.interest_rate);
        start_amount = findViewById(R.id.startAmount);
        year = findViewById(R.id.year);

        result_interest = findViewById(R.id.result_interest);
        disable_edit_text_editing(result_interest);

        result_interest_with_starting = findViewById(R.id.result_interestWithStarting);
        disable_edit_text_editing(result_interest_with_starting);

        clear = findViewById(R.id.clear);
        home = findViewById(R.id.currency_page_go_home);

        add_interest_only = findViewById(R.id.add_interest);
        add_interest_with_starting = findViewById(R.id.add_interestWithStarting);

        interest_spinner = findViewById(R.id.selector);
    }

    private void disable_edit_text_editing(EditText et){
        et.setFocusable(false);
        et.setEnabled(false);
        et.setCursorVisible(false);
        et.setKeyListener(null);
        et.setBackgroundColor(Color.TRANSPARENT);
    }

    private void interest_spinner_settings(){
        Resources r =SharedPreferenceHelper.getLangResources(this);
        spinner_array[0]=r.getString(R.string.SimInterest);
        spinner_array[1]=r.getString(R.string.ComInterest);

        ArrayAdapter ad = new ArrayAdapter<String>(Interest_calc.this, R.layout.currency_converter_spinner, spinner_array);
        ad.setDropDownViewResource(R.layout.currency_converter_dropdown);
        interest_spinner.setAdapter(ad);
        interest_spinner.setSelection(0);
        interest_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                whole_calculate_interest_process();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void set_interest_rate_listener(){
        interest_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                whole_calculate_interest_process();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void set_start_amount_listener(){
        start_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                whole_calculate_interest_process();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void set_year_listener(){
        year.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                whole_calculate_interest_process();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void clear_on_click(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interest_spinner.setSelection(0);
                clear_all_text();
            }
        });
    }

    private void home_on_click(){
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Interest_calc.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void add_interest_only_on_click(){
        add_interest_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_string_null()==true){
                    pass_interest_to_record_listener();
                    Intent intent = new Intent(Interest_calc.this, FinanceTracker.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void add_interest_with_starting_on_click(){
        add_interest_with_starting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_string_null()==true){
                    pass_interest_with_starting_to_record_listener();
                    Intent intent = new Intent(Interest_calc.this, FinanceTracker.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void pass_interest_to_record_listener(){
        String[] get_the_string_array = set_up_string_after_calculation();
        if(check_string_null()==true){
            String amount = get_the_string_array[0];
            String description = "This is a interest amount without the starting amount";
            shared_pref_passing(amount,description);
        }
    }

    private void pass_interest_with_starting_to_record_listener(){
        String[] get_the_string_array = set_up_string_after_calculation();
        if(check_string_null()==true){
            String amount = get_the_string_array[1];
            String description = "This is a interest amount with the starting amount";
            shared_pref_passing(amount,description);
        }
    }

    private void clear_all_text(){
        interest_rate.setText("");
        year.setText("");
        start_amount.setText("");
        result_interest.setText("");
        result_interest_with_starting.setText("");
    }


    private void whole_calculate_interest_process(){
        String text_needed_to_be_return[];
        get_all_component_text();
        if(check_string_null()==true) {
            turn_things_to_double();
            if (is_it_simple_interest()) {
                text_needed_to_be_return = calculate_simple_interest();
            } else {
                text_needed_to_be_return = calculate_compound_interest();
            }
            set_result_text(text_needed_to_be_return);
        }

    }



    private void get_all_component_text(){
        interest_rate_get = interest_rate.getText().toString();
        starting_get = start_amount.getText().toString();
        year_get = year.getText().toString();
        interest_spinner_get = interest_spinner.getSelectedItem().toString();
    }

    private Boolean check_string_null(){
        Boolean fill_all = true;
        if (interest_rate_get.matches("")){
//            Toast.makeText(Interest_calc.this, "You need to fill in the interest rate", Toast.LENGTH_SHORT).show();
            fill_all = false;
        }
        else if (starting_get.matches("")){
//            Toast.makeText(Interest_calc.this, "You need to fill in the starting", Toast.LENGTH_SHORT).show();
            fill_all = false;
        }
        else if (year_get.matches("")){

//            Toast.makeText(Interest_calc.this, "You need to fill in the number of year", Toast.LENGTH_SHORT).show();
            fill_all = false;
        }
        return fill_all;
    }

    private void turn_things_to_double(){
        interest_rate_double = Double.parseDouble(interest_rate_get);
        starting_double = Double.parseDouble(starting_get);
        year_double = Double.parseDouble(year_get);
    }

    public Boolean is_it_simple_interest(){

        if(interest_spinner.getSelectedItem().equals(spinner_array[0])){
            return true;
        }
        else{
            return false;
        }
    }

    private String[] calculate_simple_interest(){
        Double interest_result_with_starting = starting_double*(1+(year_double*(interest_rate_double/100)));
        Double interest_result = interest_result_with_starting - starting_double;

        String interest_result_string = String.format("%.2f",interest_result);
        String interest_result_with_starting_string = String.format("%.2f",interest_result_with_starting);
        String arr[] = {interest_result_string,interest_result_with_starting_string};
        return arr;
    }

    private String[] calculate_compound_interest(){
        Double interest_result = 0.0;
        Double interest_result_with_starting = starting_double;

        int year_in_integer = (int) Math.round(year_double);
        for (int i = 0; i < year_in_integer; i++){
            interest_result_with_starting *= (1+(interest_rate_double/100));
        }
        interest_result = interest_result_with_starting - starting_double;
        String interest_result_string = String.format("%.2f",interest_result);
        String interest_result_with_starting_string = String.format("%.2f",interest_result_with_starting);
        String arr[] = {interest_result_string,interest_result_with_starting_string};
        return arr;
    }

    private void set_result_text(String[] str){
        result_interest.setText(str[0]);
        result_interest_with_starting.setText(str[1]);
    }

    private String[] set_up_string_after_calculation(){
        String text_needed_to_be_return[];
        get_all_component_text();
        if(check_string_null()==true) {
            turn_things_to_double();
            if (is_it_simple_interest()) {
                text_needed_to_be_return = calculate_simple_interest();
            } else {
                text_needed_to_be_return = calculate_compound_interest();
            }
            return text_needed_to_be_return;
        }
        else{
            return null;
        }

    }


    private void shared_pref_passing(String amount,String description){
        SharedPreferenceHelper.setString(this,"Interest_calc_to_add_records","Amount",amount);
        SharedPreferenceHelper.setString(this,"Interest_calc_to_add_records","Description",description);
    }

    private void reloadLang(Context context) {
        //Lang change
        String lang = SharedPreferenceHelper.getLanguage(context);
        context = LocaleHelper.setLocale(context, lang);
        Resources resources = context.getResources();

        //Edit below
        //get element id
        TextView title = findViewById(R.id.currency_converter_title);
        TextView interest_text = findViewById(R.id.Interest_Text);
        TextView starting_text = findViewById(R.id.Starting_text);
        TextView year_text = findViewById(R.id.Year_Text);
        TextView display_text = findViewById(R.id.display_text);
        TextView add_interest_to_record = findViewById(R.id.add_interest_to_record);
        TextView displayText1=findViewById(R.id.display_text1);
        TextView displayText3=findViewById(R.id.display_text3);
        //get string
        title.setText(resources.getString(R.string.interest_calculator_title));
        interest_text.setText((resources.getString(R.string.interest_calculator_interest)));
        starting_text.setText(resources.getString(R.string.interest_calculator_starting));
        year_text.setText(resources.getString(R.string.interest_calculator_year));
        display_text.setText(resources.getString(R.string.interest_calculator_add_records_no_starting));
        add_interest_to_record.setText(resources.getString(R.string.interest_calculator_add_records_with_starting));
        interest_rate.setHint(resources.getString(R.string.currency_converter_hint));
        year.setHint(resources.getString(R.string.currency_converter_hint));
        start_amount.setHint(resources.getString(R.string.currency_converter_hint));
        displayText1.setText(resources.getString(R.string.Interest));
        displayText3.setText(resources.getString(R.string.InterestwithStarting));
        return;
    }
}