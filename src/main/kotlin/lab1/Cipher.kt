package lab1

interface Cipher {
    fun encrypt(plaintext: String, key: Any): String
    fun decrypt(ciphertext: String, key: Any): String
}