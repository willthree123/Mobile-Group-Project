package com.example.mobileappandroid;

public class MonthCal {
    private int month;
    private double amount;

    public MonthCal(int month) {
        this.month = month;
        this.amount=0;
    }

    public int getMonth_int() {
        return month;
    }
    public String getMonth() {
        return (Integer.toString(month));
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void addAmount(double amount){
        this.amount=this.amount+amount;
    }
}
