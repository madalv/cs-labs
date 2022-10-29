package ciphers.asymmetric

interface AsymmetricCipher {
    fun encrypt(plaintext: Long): Long
    fun decrypt(ciphertext: Long): Long
}