package com.rbc.rbcmobile.model;

import com.rbc.rbcaccountlibrary.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionModel implements Serializable, Comparable<TransactionModel> {
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

    public TransactionModel(Transaction transaction) {
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
    }

    public List<TransactionModel> setTransactionModelList (List<Transaction> transactionList) {
        List<TransactionModel> transactionModels = new ArrayList<>();
        for(Transaction transaction : transactionList) {
            TransactionModel model = new TransactionModel(transaction);
            transactionModels.add(model);
        }
        return transactionModels;
    }

    public String getAmount() {
        return amount;
    }

    public Calendar getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(TransactionModel o) {
        return getDate().getTime().compareTo(o.getDate().getTime());
    }
}
