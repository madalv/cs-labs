package ciphers.classical

interface Cipher {
    fun encrypt(plaintext: String, key: Any): String
    fun decrypt(ciphertext: String, key: Any): String
}