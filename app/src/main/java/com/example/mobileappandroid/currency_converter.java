package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

public class currency_converter extends AppCompatActivity implements View.OnClickListener{

    Spinner sp1,sp2;
    EditText ed1,ed2;
    Button convert;
    Button swap;
    ImageButton home;
    Button virtual_keyboard[];

    Double amount;
    Double selected_currency_top_values_double;
    Double selected_currency_bottom_values_double;
    Double the_required_amount;

    String url;
    String selected_currency_top_values_string;
    String selected_currency_bottom_values_string;

    JSONObject currency;
    RequestQueue queue;

    String[] currency_choose ={
            "AED","AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG","AZN","BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL",
            "BSD","BTC","BTN","BWP","BYN","BYR","CAD","CDF","CHF","CLF","CLP","CNY","COP","CRC","CUC","CUP","CVE","CZK","DJF","DKK",
            "DOP","DZD","EGP","ERN","ETB","EUR","FJD","FKP","GBP","GEL","GGP","GHS","GIP","GMD","GNF","GTQ","GYD","HKD","HNL","HRK",
            "HTG","HUF","IDR","ILS","IMP","INR","IQD","IRR","ISK","JEP","JMD","JOD","JPY","KES","KGS","KHR","KMF","KPW","KRW","KWD",
            "KYD","KZT","LAK","LBP","LKR","LRD","LSL","LTL","LVL","LYD","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRO","MUR","MVR",
            "MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON",
            "RSD","RUB","RWF","SAR","SBD","SCR","SDG","SEK","SGD","SHP","SLL","SOS","SRD","STD","SVC","SYP","SZL","THB","TJS","TMT",
            "TND","TOP","TRY","TTD","TWD","TZS","UAH","UGX","USD","UYU","UZS","VEF","VND","VUV","WST","XAF","XAG","XAU","XCD","XDR",
            "XOF","XPF","YER","ZAR","ZMK","ZMW","ZWL"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //find view by id
        setContentView(R.layout.activity_currency_converter);
        ed1 = findViewById(R.id.enter_amount_top);
        ed2 = findViewById(R.id.show_amount);
        sp1 = findViewById(R.id.choose_currency_top);
        sp2 = findViewById(R.id.choose_currency_bottom);
        convert = findViewById(R.id.convert);
        swap = findViewById(R.id.swap);
        home = findViewById(R.id.currency_page_go_home);

        //disable ed2 edit
        ed2.setFocusable(false);
        ed2.setEnabled(false);
        ed2.setCursorVisible(false);
        ed2.setKeyListener(null);
        ed2.setBackgroundColor(Color.TRANSPARENT);

        //disable ed1 edit
        ed1.setFocusable(false);
        ed1.setEnabled(false);
        ed1.setCursorVisible(false);
        ed1.setKeyListener(null);
        ed1.setBackgroundColor(Color.TRANSPARENT);

        setup_virtual_keyboard();


        //set 2 spinners item
        ArrayAdapter ad = new ArrayAdapter<String>(this, R.layout.currency_converter_spinner,currency_choose);
//        com.google.android.material.R.layout.support_simple_spinner_dropdown_item
        ad.setDropDownViewResource(R.layout.currency_converter_dropdown);
        sp1.setAdapter(ad);
        sp1.setSelection(148); // set default USD
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String amount_string_form = ed1.getText().toString();
                if(amount_string_form.matches("")){
                }else {
                    change_currency();
                }
            }

            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        ArrayAdapter ad2 = new ArrayAdapter<String>(this, R.layout.currency_converter_spinner,currency_choose);
        ad2.setDropDownViewResource(R.layout.currency_converter_dropdown);
        sp2.setAdapter(ad2);
        sp2.setSelection(57); //set default HKD
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String amount_string_form = ed1.getText().toString();
                if(amount_string_form.matches("")){
                }else {
                    change_currency();
                }
            }

                public void onNothingSelected(AdapterView<?> parent){
                }
            });


        //set API link
        queue = Volley.newRequestQueue(currency_converter.this);
        url = "http://api.exchangeratesapi.io/v1/latest?access_key=7ee74c9f604e100b9f5d1f66edcbd52b&format=1";

        //button listener
        convert.setOnClickListener(this);
        swap.setOnClickListener(this);

        //home button listener
        home.setOnClickListener(this);


    }

    public void setup_virtual_keyboard(){
        Button virtual_keyboard[] = new Button[13];
        virtual_keyboard[0] = findViewById(R.id.button_0);
        virtual_keyboard[1] = findViewById(R.id.button_1);
        virtual_keyboard[2] = findViewById(R.id.button_2);
        virtual_keyboard[3] = findViewById(R.id.button_3);
        virtual_keyboard[4] = findViewById(R.id.button_4);
        virtual_keyboard[5] = findViewById(R.id.button_5);
        virtual_keyboard[6] = findViewById(R.id.button_6);
        virtual_keyboard[7] = findViewById(R.id.button_7);
        virtual_keyboard[8] = findViewById(R.id.button_8);
        virtual_keyboard[9] = findViewById(R.id.button_9);
        virtual_keyboard[10] = findViewById(R.id.button_dot);
        virtual_keyboard[11] = findViewById(R.id.button_ac);
        virtual_keyboard[12] = findViewById(R.id.button_back);
        for(int i=0; i<13;i++){
            virtual_keyboard[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_0:
                Log.d("pressed", "onClick: 0");
                add_the_pressed_button_and_update("0");
                break;
            case R.id.button_1:
                add_the_pressed_button_and_update("1");
                break;
            case R.id.button_2:
                add_the_pressed_button_and_update("2");
                break;
            case R.id.button_3:
                add_the_pressed_button_and_update("3");
                break;
            case R.id.button_4:
                add_the_pressed_button_and_update("4");
                break;
            case R.id.button_5:
                add_the_pressed_button_and_update("5");
                break;
            case R.id.button_6:
                add_the_pressed_button_and_update("6");
                break;
            case R.id.button_7:
                add_the_pressed_button_and_update("7");
                break;
            case R.id.button_8:
                add_the_pressed_button_and_update("8");
                break;
            case R.id.button_9:
                add_the_pressed_button_and_update("9");
                break;
            case R.id.button_dot:
                add_the_pressed_button_and_update(".");
                break;
            case R.id.button_ac:
                ed1.setText("");
                ed2.setText("");
                break;
            case R.id.button_back:
                int a = ed1.getText().toString().length();
                Log.d("bitch",Integer.toString(a) );
                if((ed1.getText().toString().length()!=1) && (ed1.getText().toString().length()!=0)){
                delete_last_character();
                }
                else{
                    ed1.setText("");
                    ed2.setText("");
                }
                break;
            case R.id.currency_page_go_home:
                Intent intent = new Intent(currency_converter.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.convert:
                sp1.setSelection(148);
                sp2.setSelection(57);
                change_currency();
                break;
            case R.id.swap:
                int temp1 = sp1.getSelectedItemPosition();
                int temp2 = sp2.getSelectedItemPosition();
                sp1.setSelection(temp2);
                sp2.setSelection(temp1);

                String strTemp2 = ed2.getText().toString();
                ed1.setText(strTemp2);

                change_currency();
                break;


        }
    }

    public void add_the_pressed_button_and_update(String pressed_value){
        String currency_top;
        String currency_bottom;

        currency_top = sp1.getSelectedItem().toString();
        currency_bottom = sp2.getSelectedItem().toString();

        //get json from API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url ,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    currency = response.getJSONObject("rates");

                    selected_currency_top_values_string =currency.getString(currency_top);
                    selected_currency_bottom_values_string = currency.getString(currency_bottom);

                    selected_currency_top_values_double = Double.parseDouble(selected_currency_top_values_string);
                    selected_currency_bottom_values_double = Double.parseDouble(selected_currency_bottom_values_string);

                    //check user input is null or not
                    String amount_string_form = ed1.getText().toString();
                    amount_string_form += pressed_value;
                    if (amount_string_form!=null){
                        amount = Double.parseDouble(amount_string_form);

                        the_required_amount = (amount/selected_currency_top_values_double)*selected_currency_bottom_values_double;
                        ed1.setText(amount_string_form);
                        ed2.setText(String.format("%.4f",the_required_amount));
                    }else{
                        Toast.makeText(currency_converter.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(currency_converter.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(request);
    }

    public void delete_last_character(){
        String currency_top;
        String currency_bottom;

        currency_top = sp1.getSelectedItem().toString();
        currency_bottom = sp2.getSelectedItem().toString();

        //get json from API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url ,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    currency = response.getJSONObject("rates");

                    selected_currency_top_values_string =currency.getString(currency_top);
                    selected_currency_bottom_values_string = currency.getString(currency_bottom);

                    selected_currency_top_values_double = Double.parseDouble(selected_currency_top_values_string);
                    selected_currency_bottom_values_double = Double.parseDouble(selected_currency_bottom_values_string);

                    //check user input is null or not
                    String amount_string_form = ed1.getText().toString();
                    amount_string_form = removeLastChar(amount_string_form);
                    if (!((amount_string_form==null) && (amount_string_form.matches("")))){
                        amount = Double.parseDouble(amount_string_form);

                        the_required_amount = (amount/selected_currency_top_values_double)*selected_currency_bottom_values_double;

                        ed1.setText(amount_string_form);
                        ed2.setText(String.format("%.4f",the_required_amount));
                    }else{
                        Toast.makeText(currency_converter.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(currency_converter.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(request);
    }

    public void change_currency(){
        String currency_top;
        String currency_bottom;

        currency_top = sp1.getSelectedItem().toString();
        currency_bottom = sp2.getSelectedItem().toString();

        //get json from API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url ,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    currency = response.getJSONObject("rates");

                    selected_currency_top_values_string =currency.getString(currency_top);
                    selected_currency_bottom_values_string = currency.getString(currency_bottom);

                    selected_currency_top_values_double = Double.parseDouble(selected_currency_top_values_string);
                    selected_currency_bottom_values_double = Double.parseDouble(selected_currency_bottom_values_string);

                    //check user input is null or not
                    String amount_string_form = ed1.getText().toString();
                    if (amount_string_form!=null){
                        amount = Double.parseDouble(amount_string_form);

                        the_required_amount = (amount/selected_currency_top_values_double)*selected_currency_bottom_values_double;

                        ed2.setText(String.format("%.4f",the_required_amount));
                    }else{
                        Toast.makeText(currency_converter.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(currency_converter.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(request);
    }

    public String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

}
