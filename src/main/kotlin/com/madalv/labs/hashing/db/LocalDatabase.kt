package com.madalv.labs.hashing.db

import hashing.account.Account
import hashing.db.Database

class LocalDatabase: Database {
    private val db = HashMap<String, Account>()

    override fun set(key: String, account: Account) {
        try { db[key] = account }
        catch (e: Exception) { println(e.message) }
    }

    override fun get(key: String): Account? {
        return db.getOrDefault(key, null)
    }
}