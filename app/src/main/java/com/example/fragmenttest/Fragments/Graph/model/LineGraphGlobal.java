package com.example.fragmenttest.Fragments.Graph.model;

public class LineGraphGlobal {

    private long dateTime;
    private String currency;
    private String ticketType;

    public LineGraphGlobal() {
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
}
