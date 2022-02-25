package com.rbc.rbcmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rbc.rbcmobile.databinding.ActivityTransactionListBinding;

public class TransactionList extends AppCompatActivity {

    ActivityTransactionListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityTransactionListBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
    }
}