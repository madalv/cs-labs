package hashing.account

import java.security.PublicKey

class Account (
    val email: String,
    val password: ByteArray,
    val publicKey: PublicKey
)