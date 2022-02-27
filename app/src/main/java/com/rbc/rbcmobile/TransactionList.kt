package com.rbc.rbcmobile

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.rbc.rbcmobile.model.AccountModel
import com.rbc.rbcmobile.viewmodel.TransactionViewModel
import androidx.recyclerview.widget.RecyclerView
import com.rbc.rbcmobile.adapter.TransactionRecyclerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import android.os.Bundle
import com.rbc.rbcmobile.model.TransactionModel
import com.rbc.rbcmobile.databinding.ActivityTransactionListBinding

class TransactionList : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityTransactionListBinding? = null
    private var currentAccount: AccountModel? = null
    private var transactionViewModel: TransactionViewModel? = null

    private var recyclerView: RecyclerView? = null
    private var transactionRecyclerAdapter: TransactionRecyclerAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionListBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        currentAccount = intent.getSerializableExtra("acc_details") as AccountModel?
        transactionViewModel = TransactionViewModel.getInstance(application)
        setAccValues()
        count = 0

        observeLiveDataFromVM()
        newTransactions()
        binding!!.transactionBack.setOnClickListener(this)
        binding!!.transactionRetryButton.setOnClickListener(this)
    }

    private fun newTransactions() {
        binding!!.transactionLoader.visibility = View.VISIBLE
        transactionViewModel!!.getTransactionList(
            currentAccount!!.number,
            currentAccount!!.accountType
        )
    }

    private fun observeLiveDataFromVM() {
        transactionViewModel!!.mutableLiveDataTransactions.observe(
            this,
            { transactionModels -> //hide loader
                count += 1
                binding!!.transactionError.visibility = View.GONE
                if (transactionModels.size == 0 && count == 1) {
                    binding!!.transactionLoader.visibility = View.VISIBLE
                }
                if (transactionModels.size == 1 && transactionModels[0].description.equals(
                        "Empty Transaction",
                        ignoreCase = true
                    )
                ) {
                    binding!!.transactionNoTraToDisplay.visibility = View.VISIBLE
                    binding!!.transactionLoader.visibility = View.GONE
                } else {
                    startRecyclerView(transactionModels)
                    binding!!.transactionNoTraToDisplay.visibility = View.GONE
                }
            })
        transactionViewModel!!.errorMutable.observe(this, { throwable ->
            if (throwable != null) {
                //hide loader
                binding!!.transactionLoader.visibility = View.GONE
                binding!!.transactionError.visibility = View.VISIBLE
            }
        })
    }

    private fun setAccValues() {
        binding!!.transactionAccName.text = currentAccount!!.name
        val accountNumber = "( " + currentAccount!!.number + " )"
        binding!!.transactionAccNumber.text = accountNumber
        binding!!.transactionAccBalance.text = currentAccount!!.balance
    }

    private fun startRecyclerView(modelList: List<TransactionModel>) {
        recyclerView = binding!!.transactionRecyclerView
        gridLayoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView!!.layoutManager = gridLayoutManager
        transactionRecyclerAdapter = TransactionRecyclerAdapter(this, modelList)
        recyclerView!!.adapter = transactionRecyclerAdapter
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.transaction_retry_button) {
            newTransactions()
        } else if (id == R.id.transaction_back) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        transactionViewModel!!.clearData()
    }
}