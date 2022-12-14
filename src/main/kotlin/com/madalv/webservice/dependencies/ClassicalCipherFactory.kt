package com.madalv.webservice.dependencies

import ciphers.classical.implementations.CaesarPermutationCipher
import ciphers.classical.implementations.PlayfairCipher
import ciphers.classical.implementations.VigenereCipher
import com.madalv.labs.ciphers.classical.implementations.CaesarCipher
import com.madalv.labs.ciphers.classical.Cipher

val classicalCipherFactory = ClassicalCipherFactory()

class ClassicalCipherFactory {
    fun getCipher(type: String): Cipher {
        return when (type) {
            "caesar" -> CaesarCipher("abcdefghijklmnopqrstuvwxyz")
            "caesar-permutation" -> CaesarPermutationCipher(14895235, "abcdefghijklmnopqrstuvwxyz")
            "playfair" -> PlayfairCipher("abcdefghijklmnopqrstuvwxyz")
            "vigenere" -> VigenereCipher("abcdefghijklmnopqrstuvwxyz")
            else -> CaesarCipher("abcdefghijklmnopqrstuvwxyz")
        }
    }
}


