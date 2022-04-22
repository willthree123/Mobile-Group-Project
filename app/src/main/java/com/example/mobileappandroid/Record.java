package com.example.mobileappandroid;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Record implements Serializable {
    private double amount;
    private Calendar date;
    private String description;
    private int category;
    private boolean consume;
    private int Category_type;

    public Record(double amount, Calendar date, int category, String description, boolean consume, int Category_type) {
        this.amount = amount;
        this.date = date;
        this.category=category;
        this.description=description;
        this.consume=consume;
        this.Category_type=Category_type;
    }


    public double getAmount() {
        return amount;
    }

    public String getDate() {
            return (Integer.toString(date.get(Calendar.DATE))+"/"+Integer.toString(date.get(Calendar.MONTH)+1)+"/"+Integer.toString(date.get(Calendar.YEAR)));
    }

    public String getYear() {
        return (Integer.toString(date.get(Calendar.YEAR)));
    }

    public int getYear_int() {
        return (date.get(Calendar.YEAR));
    }

    public int getMonth_int() {
        return (date.get(Calendar.MONTH));
    }

    public Calendar getDate_calendar() {
        return date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isConsume() {
        return consume;
    }

    public void setConsume(boolean consume) {
        this.consume = consume;
    }


}
