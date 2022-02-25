package com.rbc.rbcmobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rbc.rbcmobile.R;
import com.rbc.rbcmobile.model.TransactionModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.MyViewHolder> {
    Context context;
    List<TransactionModel> transactionModelList = new ArrayList<>();

    public TransactionRecyclerAdapter(Context context, List<TransactionModel> transactionModelList) {
        this.context = context;
        this.transactionModelList = transactionModelList;
        sortTransactions();
    }

    private void sortTransactions() {
        Collections.sort(transactionModelList, Collections.reverseOrder());
    }

    @NonNull
    @Override
    public TransactionRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_transactions, parent, false);
        return new TransactionRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionRecyclerAdapter.MyViewHolder holder, int position) {
        Date date = transactionModelList.get(position).getDate().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String stringDate= formatter.format(date);
        String description = transactionModelList.get(position).getDescription();
        String amount = transactionModelList.get(position).getAmount();

        holder.assignValues(stringDate, description, amount);
    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView transaction_date, transaction_description, transaction_amount;
        MaterialCardView transactionRecyclerLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            transaction_date = itemView.findViewById(R.id.transactions_recycler_acc_date);
            transaction_amount = itemView.findViewById(R.id.transactions_recycler_acc_amount);
            transaction_description = itemView.findViewById(R.id.transactions_recycler_acc_description);
            transactionRecyclerLayout = itemView.findViewById(R.id.transactions_recycler_layout);
        }

        public void assignValues(String date, String description, String amount) {
            transaction_date.setText(date);
            transaction_amount.setText(amount);
            transaction_description.setText(description);
        }
    }
}
