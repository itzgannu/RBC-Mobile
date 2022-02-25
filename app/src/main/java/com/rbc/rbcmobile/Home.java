package com.rbc.rbcmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.rbc.rbcaccountlibrary.AccountProvider;
import com.rbc.rbcmobile.adapter.AccountRecyclerAdapter;
import com.rbc.rbcmobile.databinding.ActivityHomeBinding;
import com.rbc.rbcmobile.model.AccountModel;
import com.rbc.rbcmobile.viewmodel.AccountViewModel;

import java.util.List;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;

    AccountViewModel accountViewModel;

    //Recycler View Variables
    RecyclerView recyclerView;
    AccountRecyclerAdapter accountRecyclerAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.accountViewModel = AccountViewModel.getInstance(getApplication());
        List<AccountModel> accountModelList = this.accountViewModel.getAccountsList();

        //send to recycler view
        startRecyclerView(accountModelList);
    }

    private void startRecyclerView(List<AccountModel> accountModels) {
        this.recyclerView = this.binding.homeRecyclerView;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        accountRecyclerAdapter = new AccountRecyclerAdapter(this, accountModels);
        recyclerView.setAdapter(accountRecyclerAdapter);
    }
}