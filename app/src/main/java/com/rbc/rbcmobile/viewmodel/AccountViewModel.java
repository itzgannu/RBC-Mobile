package com.rbc.rbcmobile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.rbc.rbcaccountlibrary.Account;
import com.rbc.rbcaccountlibrary.AccountProvider;
import com.rbc.rbcmobile.model.AccountModel;

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
    private final AccountProvider accountProvider = AccountProvider.INSTANCE;

    private static AccountViewModel instance;

    public MutableLiveData<List<AccountModel>> mutableLiveDataAccounts = new MutableLiveData<>();
    public MutableLiveData<Throwable> accountErrorMutable = new MutableLiveData<Throwable>();
    Disposable disposable ;

    public static AccountViewModel getInstance(Application application) {
        if(instance == null) {
            instance = new AccountViewModel(application);
        }
        return instance;
    }

    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

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
                mutableLiveDataAccounts.postValue(accountModels);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) {
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
