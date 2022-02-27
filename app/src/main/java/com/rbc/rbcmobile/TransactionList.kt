package com.rbc.rbcmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.rbc.rbcmobile.adapter.TransactionRecyclerAdapter;
import com.rbc.rbcmobile.databinding.ActivityTransactionListBinding;
import com.rbc.rbcmobile.model.AccountModel;
import com.rbc.rbcmobile.model.TransactionModel;
import com.rbc.rbcmobile.viewmodel.TransactionViewModel;

import java.util.List;

public class TransactionList extends AppCompatActivity implements View.OnClickListener {

    ActivityTransactionListBinding binding;

    AccountModel currentAccount;

    TransactionViewModel transactionViewModel;

    //Recycler View Variables
    RecyclerView recyclerView;
    TransactionRecyclerAdapter transactionRecyclerAdapter;
    GridLayoutManager gridLayoutManager;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityTransactionListBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        currentAccount = (AccountModel) getIntent().getSerializableExtra("acc_details");

        this.transactionViewModel = TransactionViewModel.getInstance(getApplication());

        setAccValues();
        count = 0;

        //apiCallCheck();
        observeLiveDataFromVM();
        newTransactions();

        this.binding.transactionBack.setOnClickListener(this);
        this.binding.transactionRetryButton.setOnClickListener(this);
    }

    private void newTransactions() {
        //visible loader
        this.binding.transactionLoader.setVisibility(View.VISIBLE);
        transactionViewModel.getTransactionList(currentAccount.getNumber(), currentAccount.getAccountType());
    }

    private void observeLiveDataFromVM() {
        transactionViewModel.mutableLiveDataTransactions.observe(this, new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(List<TransactionModel> transactionModels) {
                //hide loader
                count = count+1;
                binding.transactionError.setVisibility(View.GONE);
                if(transactionModels.size()==0 && count == 1) {
                    binding.transactionLoader.setVisibility(View.VISIBLE);
                } else {
                    binding.transactionLoader.setVisibility(View.GONE);
                }
                if(transactionModels.size()==1 && transactionModels.get(0).getDescription().equalsIgnoreCase("Empty Transaction")){
                    binding.transactionNoTraToDisplay.setVisibility(View.VISIBLE);
                } else{
                    startRecyclerView(transactionModels);
                    binding.transactionNoTraToDisplay.setVisibility(View.GONE);
                }
            }
        });

        transactionViewModel.errorMutable.observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                if(throwable != null) {
                    //hide loader
                    binding.transactionLoader.setVisibility(View.GONE);
                    binding.transactionError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setAccValues() {
        this.binding.transactionAccName.setText(currentAccount.getName());
        this.binding.transactionAccNumber.setText(currentAccount.getNumber());
        this.binding.transactionAccBalance.setText(currentAccount.getBalance());
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
            newTransactions();
        } else if (id == R.id.transaction_back) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transactionViewModel.clearData();
    }
}