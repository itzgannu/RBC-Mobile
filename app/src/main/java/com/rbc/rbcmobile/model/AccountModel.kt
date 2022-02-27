package com.rbc.rbcmobile.model;

import com.rbc.rbcaccountlibrary.Account;
import com.rbc.rbcaccountlibrary.AccountType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountModel implements Serializable {
    String balance;
    String name;
    String number;
    AccountType accountType;

    public AccountModel() {
    }

    public AccountModel(Account account) {
        this.balance = account.getBalance();
        this.name = account.getName();
        this.number = account.getNumber();
        this.accountType = account.getType();
    }

    public String getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public List<AccountModel> setAccountModelList (List<Account> accountList) {
        List<AccountModel> accountModels = new ArrayList<>();
        for(Account account : accountList) {
            AccountModel model = new AccountModel(account);
            accountModels.add(model);
        }
        return accountModels;
    }
}
