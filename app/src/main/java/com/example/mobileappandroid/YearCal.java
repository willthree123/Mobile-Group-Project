package com.example.mobileappandroid;

public class YearCal {
    private int year;
    private double amount;

    public YearCal(int year) {
        this.year = year;
        this.amount=0;
    }

    public String getYear() {
        return (Integer.toString(year));
    }

    public int getYear_int() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getAmount() {
        return amount;
    }

    public void addAmount(double amount) {
        this.amount = this.amount + amount;
    }
}
