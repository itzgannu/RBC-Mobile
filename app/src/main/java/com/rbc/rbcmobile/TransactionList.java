package com.rbc.rbcmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rbc.rbcaccountlibrary.AccountProvider;
import com.rbc.rbcaccountlibrary.Transaction;
import com.rbc.rbcmobile.adapter.AccountRecyclerAdapter;
import com.rbc.rbcmobile.adapter.TransactionRecyclerAdapter;
import com.rbc.rbcmobile.databinding.ActivityTransactionListBinding;
import com.rbc.rbcmobile.model.AccountModel;
import com.rbc.rbcmobile.model.TransactionModel;
import com.rbc.rbcmobile.viewmodel.AccountViewModel;
import com.rbc.rbcmobile.viewmodel.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionList extends AppCompatActivity implements View.OnClickListener {

    ActivityTransactionListBinding binding;

    AccountModel currentAccount;

    TransactionViewModel transactionViewModel;

    //Recycler View Variables
    RecyclerView recyclerView;
    TransactionRecyclerAdapter transactionRecyclerAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityTransactionListBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        currentAccount = (AccountModel) getIntent().getSerializableExtra("acc_details");

        this.transactionViewModel = TransactionViewModel.getInstance(getApplication());

        setAccValues();

        apiCallCheck();

        this.binding.transactionBack.setOnClickListener(this);
        this.binding.transactionRetryButton.setOnClickListener(this);
    }

    private void setAccValues() {
        this.binding.transactionAccName.setText(currentAccount.getName());
        this.binding.transactionAccNumber.setText(currentAccount.getNumber());
        this.binding.transactionAccBalance.setText(currentAccount.getBalance());
    }

    private void apiCallCheck() {
        int type = currentAccount.getAccountType().ordinal();
        if(type == 1) {
            List<TransactionModel> ccModelList = transactionViewModel.getCcTransactions(currentAccount.getNumber());
            List<TransactionModel> modelList = transactionViewModel.getAllTransactions(currentAccount.getNumber());
            if (modelList == null || ccModelList == null) {
                //display retry button
                this.binding.transactionError.setVisibility(View.VISIBLE);
            } else {
                this.binding.transactionError.setVisibility(View.GONE);
                Log.d("Hero", "cc- "+ccModelList.size());
                Log.d("Hero", "normal- "+modelList.size());
                modelList.addAll(ccModelList);
                startRecyclerView(modelList);
            }
        } else {
            List<TransactionModel> modelList = transactionViewModel.getAllTransactions(currentAccount.getNumber());
            if (modelList == null) {
                //display retry button
                this.binding.transactionError.setVisibility(View.VISIBLE);
            } else {
                this.binding.transactionError.setVisibility(View.GONE);
                startRecyclerView(modelList);
            }
        }


    }

    private void startRecyclerView(List<TransactionModel> modelList) {
        this.recyclerView = this.binding.transactionRecyclerView;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        transactionRecyclerAdapter = new TransactionRecyclerAdapter(this, modelList);
        recyclerView.setAdapter(transactionRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.transaction_retry_button) {
            apiCallCheck();
        } else if (id == R.id.transaction_back) {
            finish();
        }
    }
}