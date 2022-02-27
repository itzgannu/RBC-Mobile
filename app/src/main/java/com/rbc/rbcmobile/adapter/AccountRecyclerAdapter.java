package com.rbc.rbcmobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rbc.rbcmobile.R;
import com.rbc.rbcmobile.TransactionList;
import com.rbc.rbcmobile.model.AccountModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountRecyclerAdapter extends RecyclerView.Adapter<AccountRecyclerAdapter.MyViewHolder> {
    Context context;
    List<AccountModel> accountModelList;
    boolean newOrdinal = true;
    int previousOrdinal = 0;

    public AccountRecyclerAdapter(Context context, List<AccountModel> accountModelList) {
        this.context = context;
        this.accountModelList = accountModelList;
        groupAccounts();
    }

    private void groupAccounts() {
        List<AccountModel> groupedList = new ArrayList<>();
        int seq = 0;
        for(int i = 0 ; i<accountModelList.size(); i++) {
            if(seq<4){
                for(int j=0; j<accountModelList.size(); j++) {
                    int typeNum = Objects.requireNonNull(accountModelList.get(j).getAccountType()).ordinal();
                    if(typeNum == seq ) {
                        groupedList.add(accountModelList.get(j));
                    }
                }
                seq = seq+1;
            }
        }
        this.accountModelList = groupedList;
    }

    @NonNull
    @Override
    public AccountRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_accounts, parent, false);
        return new AccountRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountRecyclerAdapter.MyViewHolder holder, int position) {
        String acc_name = accountModelList.get(position).getName();
        String acc_number = accountModelList.get(position).getNumber();
        String acc_balance = accountModelList.get(position).getBalance();
        int ordinal = Objects.requireNonNull(accountModelList.get(position).getAccountType()).ordinal();
        
        holder.assignValues(acc_name, acc_number, acc_balance, ordinal);

        holder.tapLayout.setOnClickListener(v -> {
            Intent goToTransactionScreen = new Intent(context, TransactionList.class).addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            goToTransactionScreen.putExtra("acc_details", accountModelList.get(holder.getAdapterPosition()));
            context.startActivity(goToTransactionScreen);
        });
    }

    private String getTitle(int ordinal) {
        if(ordinal == 0 ) {
            return  "CHEQUING";
        } else if(ordinal == 1) {
            return "CREDIT CARD";
        } else if(ordinal == 2) {
            return "LOAN";
        } else if (ordinal == 3) {
            return "MORTGAGE";
        } else {
            return "NA";
        }
    }

    @Override
    public int getItemCount() {
        return accountModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView account_name_tv, account_number_tv, account_balance_tv, account_header;
        MaterialCardView accountRecyclerLayout;
        LinearLayout tapLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            account_name_tv = itemView.findViewById(R.id.account_recycler_acc_name);
            account_number_tv = itemView.findViewById(R.id.account_recycler_acc_number);
            account_balance_tv = itemView.findViewById(R.id.account_recycler_acc_balance);
            account_header = itemView.findViewById(R.id.account_recycler_header);
            accountRecyclerLayout = itemView.findViewById(R.id.recycler_accounts);
            tapLayout = itemView.findViewById(R.id.account_recycler_tapping_layout);
        }

        public void assignValues(String acc_name, String acc_number, String acc_balance, int ordinal) {
            if(newOrdinal) {
                previousOrdinal = ordinal;
                String title = getTitle(ordinal);
                account_header.setText(title); newOrdinal = false;
            }
            else if(ordinal != previousOrdinal) {
                previousOrdinal = ordinal;
                String title = getTitle(ordinal);
                account_header.setText(title);
            }
            else {
                account_header.setVisibility(View.GONE);
            }
            account_name_tv.setText(acc_name);
            String newNumber = "("+acc_number+")" ;
            account_number_tv.setText(newNumber);
            account_balance_tv.setText(acc_balance);
        }
    }
}
