package hashing.db

import hashing.account.Account

interface Database {
    fun get(key: String): Account?
    fun set(key: String, account: Account)
}