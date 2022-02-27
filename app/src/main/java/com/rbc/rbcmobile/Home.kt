package com.rbc.rbcmobile

import androidx.appcompat.app.AppCompatActivity
import com.rbc.rbcmobile.viewmodel.AccountViewModel
import androidx.recyclerview.widget.RecyclerView
import com.rbc.rbcmobile.adapter.AccountRecyclerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import android.os.Bundle
import com.rbc.rbcmobile.databinding.ActivityHomeBinding
import com.rbc.rbcmobile.model.AccountModel
import java.util.*

class Home : AppCompatActivity() {
    private var binding: ActivityHomeBinding? = null
    private var accountViewModel: AccountViewModel? = null

    private var recyclerView: RecyclerView? = null
    private var accountRecyclerAdapter: AccountRecyclerAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setWelcomeMessage()
        accountViewModel = AccountViewModel.getInstance(application)
        observeLiveDataFromVM()
        newList
    }

    private val newList: Unit
        get() {
            accountViewModel!!.getAllAccountsList()
        }

    private fun observeLiveDataFromVM() {
        accountViewModel!!.mutableLiveDataAccounts.observe(
            this,
            { accountModels -> startRecyclerView(accountModels) })
    }

    private fun startRecyclerView(accountModels: List<AccountModel>) {
        recyclerView = binding!!.homeRecyclerView
        gridLayoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView!!.layoutManager = gridLayoutManager
        accountRecyclerAdapter = AccountRecyclerAdapter(this, accountModels)
        recyclerView!!.adapter = accountRecyclerAdapter
    }

    private fun setWelcomeMessage() {
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar[Calendar.HOUR_OF_DAY]
        var title = "Welcome"
        if (timeOfDay in 1..3) {
            title = "Hello, Night Owl"
            binding!!.homeWelcomeMessage.text = title
        }
        when (timeOfDay) {
            in 4..11 -> {
                title = "Good Morning"
                binding!!.homeWelcomeMessage.text = title
            }
            in 12..15 -> {
                title = "Good Afternoon"
                binding!!.homeWelcomeMessage.text = title
            }
            in 16..23 -> {
                title = "Good Evening"
                binding!!.homeWelcomeMessage.text = title
            }
            else -> {
                binding!!.homeWelcomeMessage.text = title
            }
        }
    }
}