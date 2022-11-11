package hashing.db

import hashing.account.Account

class LocalDatabase: Database {
    private val db = HashMap<String, Account>()

    override fun set(key: String, acc: Account) {
        try { db[key] = acc }
        catch (e: Exception) { println(e.message) }
    }

    override fun get(key: String): Account? {
        return db.getOrDefault(key, null)
    }
}