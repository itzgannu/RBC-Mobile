package com.rbc.rbcmobile.model

import com.rbc.rbcaccountlibrary.Transaction
import java.io.Serializable
import java.util.*

class TransactionModel : Serializable, Comparable<TransactionModel> {
    var amount: String? = null
    var date: Calendar? = null
    var description: String? = null

    constructor() {}

    constructor(amount: String?, date: Calendar?, description: String?) {
        this.amount = amount
        this.date = date
        this.description = description
    }

    constructor(transaction: Transaction) {
        amount = transaction.amount
        date = transaction.date
        description = transaction.description
    }

    fun setTransactionModelList(transactionList: List<Transaction>): List<TransactionModel> {
        val transactionModels: MutableList<TransactionModel> = ArrayList()
        for (transaction in transactionList) {
            val model = TransactionModel(transaction)
            transactionModels.add(model)
        }
        return transactionModels
    }

    override fun compareTo(other: TransactionModel): Int {
        return date!!.time.compareTo(other.date!!.time)
    }
}