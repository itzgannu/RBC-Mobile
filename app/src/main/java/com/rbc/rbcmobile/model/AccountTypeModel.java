package com.rbc.rbcmobile.model;

import com.rbc.rbcaccountlibrary.AccountType;

import java.io.Serializable;

public class AccountTypeModel implements Serializable {
    String name;
    int ordinal;

    public AccountTypeModel() {
    }

    public AccountTypeModel(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public AccountTypeModel(AccountType accountType) {
        this.name = accountType.name();
        this.ordinal = accountType.ordinal();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}
