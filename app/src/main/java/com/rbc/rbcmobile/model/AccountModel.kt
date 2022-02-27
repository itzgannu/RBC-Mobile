package com.rbc.rbcmobile.model

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import java.io.Serializable
import java.util.ArrayList

class AccountModel : Serializable {
    var balance: String? = null
    var name: String? = null
    var number: String? = null
    var accountType: AccountType? = null

    constructor() {}

    constructor(account: Account) {
        balance = account.balance
        name = account.name
        number = account.number
        accountType = account.type
    }

    fun setAccountModelList(accountList: List<Account>): List<AccountModel> {
        val accountModels: MutableList<AccountModel> = ArrayList()
        for (account in accountList) {
            val model = AccountModel(account)
            accountModels.add(model)
        }
        return accountModels
    }
}