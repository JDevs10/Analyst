package com.example.fragmenttest.objects;

public class GraphValues {
    private String transaction;
    private double currency;
    private long dateInLong;

    public GraphValues() {
    }

    public GraphValues(double currency, long dateInLong) {
        this.currency = currency;
        this.dateInLong = dateInLong;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public double getCurrency() {
        return currency;
    }

    public void setCurrency(double currency) {
        this.currency = currency;
    }

    public long getDateInLong() {
        return dateInLong;
    }

    public void setDateInLong(long dateInLong) {
        this.dateInLong = dateInLong;
    }
}
