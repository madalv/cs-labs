package hashing.account

import hashing.db.Database
import hashing.db.LocalDatabase
import java.lang.Exception
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.PrivateKey

object AccountManager {
    private val db: Database = LocalDatabase()
    private val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
    private val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")

    fun addAccount(email: String, password: String): PrivateKey {

        if (db.get(email) != null) {
            throw Exception("Account already exists.")
        }

        val keyPair = kpg.genKeyPair()
        val pubk = keyPair.public

        val acc = Account(email,
            messageDigest.digest(password.toByteArray()), pubk)
        db.set(email,acc)

        return keyPair.private
    }

    fun verifyPassword(email: String, password: String): Boolean {
        if (db.get(email) == null) {
            throw Exception("No such account.")
        }
        return messageDigest.digest(password.toByteArray()).contentEquals(db.get(email)!!.password)
    }

    fun getAccount(email: String): Account {
        if (db.get(email) == null) {
            throw Exception("No such account.")
        }
        return db.get(email)!!
    }
}