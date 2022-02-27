package com.rbc.rbcmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.rbc.rbcmobile.adapter.AccountRecyclerAdapter;
import com.rbc.rbcmobile.databinding.ActivityHomeBinding;
import com.rbc.rbcmobile.model.AccountModel;
import com.rbc.rbcmobile.viewmodel.AccountViewModel;

import java.util.Calendar;
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

        setWelcomeMessage();

        this.accountViewModel = AccountViewModel.getInstance(getApplication());

        observeLiveDataFromVM();
        getNewList();
    }

    private void getNewList() {
        this.accountViewModel.getAllAccountsList();
    }

    private void observeLiveDataFromVM() {
        this.accountViewModel.mutableLiveDataAccounts.observe(this, new Observer<List<AccountModel>>() {
            @Override
            public void onChanged(List<AccountModel> accountModels) {
                startRecyclerView(accountModels);
            }
        });
    }

    private void startRecyclerView(List<AccountModel> accountModels) {
        this.recyclerView = this.binding.homeRecyclerView;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        accountRecyclerAdapter = new AccountRecyclerAdapter(this, accountModels);
        recyclerView.setAdapter(accountRecyclerAdapter);
    }

    private void setWelcomeMessage() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String title = "Welcome";

        if(timeOfDay > 0 && timeOfDay < 4){
            title = "Hello, Night Owl";
            this.binding.homeWelcomeMessage.setText(title);
        }if(timeOfDay >= 4 && timeOfDay < 12){
            title = "Good Morning";
            this.binding.homeWelcomeMessage.setText(title);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            title = "Good Afternoon";
            this.binding.homeWelcomeMessage.setText(title);
        }else if(timeOfDay >= 16 && timeOfDay < 24){
            title = "Good Evening";
            this.binding.homeWelcomeMessage.setText(title);
        }else{
            this.binding.homeWelcomeMessage.setText(title);
        }
    }

}