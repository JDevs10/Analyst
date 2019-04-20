package com.example.fragmenttest.objects;

import android.graphics.Color;

public class Ticket {
    private int id;
    private String name;
    private String category;
    private String ticketType;
    private double currency;
    private int currencyColor;
    private String currencyType;
    private String date;
    private long dateInLong;

    public Ticket(int id, String name, String category, String ticketType, double currency, int currencyColor, String currencyType, String date, long dateInLong) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.ticketType = ticketType;
        this.currency = currency;
        this.currencyColor = currencyColor;
        this.currencyType = currencyType;
        this.date = date;
        this.dateInLong = dateInLong;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getCurrency() {
        return currency;
    }

    public void setCurrency(double currency) {
        this.currency = currency;
    }

    public int getCurrencyColor(){ return currencyColor;}

    public void setCurrencyColor(int currencyColor){ this.currencyColor = currencyColor; }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDateInLong() {
        return dateInLong;
    }

    public void setDateInLong(long dateInLong) {
        this.dateInLong = dateInLong;
    }
}
