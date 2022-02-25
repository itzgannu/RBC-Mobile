package com.rbc.rbcmobile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.rbc.rbcaccountlibrary.Account;
import com.rbc.rbcaccountlibrary.AccountProvider;
import com.rbc.rbcaccountlibrary.Transaction;
import com.rbc.rbcmobile.model.AccountModel;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    //backend connection
    private final AccountProvider accountProvider = AccountProvider.INSTANCE;

    //private Instance Variable
    private static AccountViewModel instance;

    //public Instance method
    public static AccountViewModel getInstance(Application application) {
        if(instance == null) {
            instance = new AccountViewModel(application);
        }
        return instance;
    }

    //Constructor which is created automatically because of extending AndroidViewModel to the current class
    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    //methods
    public List<AccountModel> getAccountsList() {
        List<Account> accountList = accountProvider.getAccountsList();
        AccountModel accountModel = new AccountModel();
        return  accountModel.setAccountModelList(accountList);
    }
}
