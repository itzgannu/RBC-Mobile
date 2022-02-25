package com.rbc.rbcmobile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.rbc.rbcaccountlibrary.Account;
import com.rbc.rbcaccountlibrary.AccountProvider;
import com.rbc.rbcaccountlibrary.Transaction;
import com.rbc.rbcmobile.model.AccountModel;
import com.rbc.rbcmobile.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    //backend connection
    private final AccountProvider accountProvider = AccountProvider.INSTANCE;

    private static TransactionViewModel instance;

    public static TransactionViewModel getInstance(Application application) {
        if(instance == null) {
            instance = new TransactionViewModel(application);
        }
        return instance;
    }

    public TransactionViewModel(@NonNull Application application) {
        super(application);
    }

    public List<TransactionModel> getAllTransactions(String accountNumber) {
        try {
            List<Transaction> transactions = accountProvider.getTransactions(accountNumber);
            TransactionModel transactionModel = new TransactionModel();
            return transactionModel.setTransactionModelList(transactions);
        } catch (Exception e) {
            return null;
        }
    }

    public List<TransactionModel> getCcTransactions(String accountNumber) {
        try {
            List<Transaction> transactions = accountProvider.getAdditionalCreditCardTransactions(accountNumber);
            TransactionModel transactionModel = new TransactionModel();
            return transactionModel.setTransactionModelList(transactions);
        } catch (Exception e) {
            return null;
        }
    }
}
