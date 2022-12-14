package com.madalv.webservice

import com.madalv.labs.hashing.account.AccountManager

val accountManager: AccountManager = AccountManager

fun seedDB() {
    accountManager.addAccount("jora", "jora")
    accountManager.addAccount("madalv", "p5ssword")
    accountManager.addAccount("drvasile", "secret_stuff")
}