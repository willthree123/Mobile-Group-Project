package com.example.mobileappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class currency_converter extends AppCompatActivity {

    Spinner sp1,sp2;
    EditText ed1,ed2;
    Button convert;
    Button home;

    Double amount;
    Double selected_currency_top_values_double;
    Double selected_currency_bottom_values_double;
    Double the_required_amount;

    String url;
    String selected_currency_top_values_string;
    String selected_currency_bottom_values_string;

    JSONObject currency;

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
        home = findViewById(R.id.currency_page_go_home);

        //disable ed2 edit
        ed2.setFocusable(false);
        ed2.setEnabled(false);
        ed2.setCursorVisible(false);
        ed2.setKeyListener(null);
        ed2.setBackgroundColor(Color.TRANSPARENT);

        //set 2 spinners item
        ArrayAdapter ad = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,currency_choose);
        sp1.setAdapter(ad);
        sp1.setSelection(148); // set default USD

        ArrayAdapter ad2 = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,currency_choose);
        sp2.setAdapter(ad2);
        sp2.setSelection(57); //set default HKD

        //set API link
        RequestQueue queue = Volley.newRequestQueue(currency_converter.this);
        url = "http://api.exchangeratesapi.io/v1/latest?access_key=7ee74c9f604e100b9f5d1f66edcbd52b&format=1";

        //button listener
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Double tot;
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

                                ed2.setText(String.format("%.2f",the_required_amount));
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
        });

        //home button listener
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currency_converter.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}