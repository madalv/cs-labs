package hashing.messages

import hashing.account.AccountManager
import java.security.*

object MessageManager {
    private val accountManager: AccountManager = AccountManager

    fun signMessage(message: String, prik: PrivateKey): ByteArray {
        val signature: Signature = Signature.getInstance("SHA256withRSA")
        val bytes = message.toByteArray()

        signature.apply {
            initSign(prik)
            update(bytes)
        }

        return signature.sign()
    }

    fun verifySignature(message: String, signed: ByteArray, email: String): Boolean {
        val bytes = message.toByteArray()
        val signature: Signature = Signature.getInstance("SHA256withRSA")
        val acc = accountManager.getAccount(email)

        signature.apply {
            initVerify(acc.publicKey)
            update(bytes)
        }
        return signature.verify(signed)
    }
}