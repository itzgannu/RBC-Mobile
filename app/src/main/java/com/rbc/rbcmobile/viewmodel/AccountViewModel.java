package com.rbc.rbcmobile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.rbc.rbcaccountlibrary.Account;
import com.rbc.rbcaccountlibrary.AccountProvider;
import com.rbc.rbcaccountlibrary.AccountType;
import com.rbc.rbcaccountlibrary.Transaction;
import com.rbc.rbcmobile.model.AccountModel;
import com.rbc.rbcmobile.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AccountViewModel extends AndroidViewModel {
    //backend connection
    private final AccountProvider accountProvider = AccountProvider.INSTANCE;

    //private Instance Variable
    private static AccountViewModel instance;

    public MutableLiveData<List<AccountModel>> mutableLiveDataAccounts = new MutableLiveData<>();
    public MutableLiveData<Throwable> accountErrorMutable = new MutableLiveData<Throwable>();
    Disposable disposable ;

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

    //using rxJava
    public void getAllAccountsList() {
        clearData();
        disposable = Single.create(new SingleOnSubscribe<List<AccountModel>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<AccountModel>> e) throws Exception {
                try{
                    List<Account> accountList = accountProvider.getAccountsList();
                    AccountModel accountModel = new AccountModel();
                    List<AccountModel> newList = accountModel.setAccountModelList(accountList);

                    e.onSuccess(newList);
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AccountModel>>() {
            @Override
            public void accept(@NonNull List<AccountModel> accountModels) throws Exception {
                //mutable live data
                mutableLiveDataAccounts.postValue(accountModels);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                //error mutable live data
                accountErrorMutable.postValue(throwable);
            }
        });
    }

    public void clearData() {
        mutableLiveDataAccounts.postValue(new ArrayList<>());
        accountErrorMutable.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        mutableLiveDataAccounts.postValue(new ArrayList<>());
        accountErrorMutable.setValue(null);
    }

}
