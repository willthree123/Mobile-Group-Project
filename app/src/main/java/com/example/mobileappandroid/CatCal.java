package com.example.mobileappandroid;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

public class CatCal {
    int category;
    double amount;

    public CatCal(int category) {
        this.category = category;
        this.amount = 0;
    }

    public int getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public void addAmount(double amount) {
        this.amount = this.amount + amount;
    }

    public String getCategory_name(Context context){
        Resources res = context.getResources();
        String[] cats = res.getStringArray(R.array.financeNegCat);
        return cats[category];
    }
}
