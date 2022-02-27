package com.rbc.rbcmobile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.rbc.rbcaccountlibrary.AccountProvider;
import com.rbc.rbcaccountlibrary.AccountType;
import com.rbc.rbcaccountlibrary.Transaction;
import com.rbc.rbcmobile.model.TransactionModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TransactionViewModel extends AndroidViewModel {
    private final AccountProvider accountProvider = AccountProvider.INSTANCE;

    public MutableLiveData<List<TransactionModel>> mutableLiveDataTransactions = new MutableLiveData<>();
    public MutableLiveData<Throwable> errorMutable = new MutableLiveData<Throwable>();
    Disposable disposable ;

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

    public List<TransactionModel> getCcTransactions(String accountNumber) {
        try {
            List<Transaction> transactions = accountProvider.getAdditionalCreditCardTransactions(accountNumber);
            TransactionModel transactionModel = new TransactionModel();
            return transactionModel.setTransactionModelList(transactions);
        } catch (Exception e) {
            return null;
        }
    }

    public void getTransactionList(String accountNumber, AccountType accountType) {
        clearData();
        disposable = Single.create(new SingleOnSubscribe<List<TransactionModel>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<TransactionModel>> e) throws Exception {
                try{
                    List<Transaction> transactions = accountProvider.getTransactions(accountNumber);
                    TransactionModel transactionModel = new TransactionModel();
                    List<TransactionModel> transactionModelList = transactionModel.setTransactionModelList(transactions);

                    if(accountType == AccountType.CREDIT_CARD) {
                        List<Transaction> ccTransactions = accountProvider.getAdditionalCreditCardTransactions(accountNumber);
                        TransactionModel ccTransactionModel = new TransactionModel();
                        List<TransactionModel> ccTransactionModelList = ccTransactionModel.setTransactionModelList(ccTransactions);
                        transactionModelList.addAll(ccTransactionModelList);
                    }

                    e.onSuccess(transactionModelList);
                } catch (Exception ex){
                    e.onError(ex);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<TransactionModel>>() {
            @Override
            public void accept(@NonNull List<TransactionModel> transactionModels) throws Exception {
                if(transactionModels.size()==0){
                    List<TransactionModel> emptyModel = new ArrayList<>();
                    Calendar calendar = Calendar.getInstance();
                    TransactionModel a = new TransactionModel("Empty",calendar, "Empty Transaction");
                    emptyModel.add(a);
                    mutableLiveDataTransactions.postValue(emptyModel);
                }else{
                    mutableLiveDataTransactions.postValue(transactionModels);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) {
                errorMutable.setValue(throwable);
            }
        });
    }

    public void clearData() {
        mutableLiveDataTransactions.postValue(new ArrayList<>());
        errorMutable.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        mutableLiveDataTransactions.postValue(null);
        errorMutable.setValue(null);
    }
}
