package com.rbc.rbcmobile.model;

import java.io.Serializable;
import java.util.Calendar;

public class TransactionModel implements Serializable {
    String amount;
    Calendar date;
    String description;

    public TransactionModel() {
    }

    public TransactionModel(String amount, Calendar date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Calendar getDate() {
        return date;
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
}
