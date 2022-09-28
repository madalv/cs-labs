package lab1

import lab1.implementations.CaesarCipher
import lab1.implementations.CaesarPermutationCipher
import lab1.implementations.VigenereCipher

fun main() {
    val alphabetLatin = "abcdefghijklmnopqrstuvwxyz"
    val seed: Long = 6
    val caesarKey = 28
    val vigenereKey = "BEST"
    val cleartext = "ATTaCK AT MiDNIGht"
    val cleartext2 = "CyberSecurity"


    val caesarCipher: Cipher = CaesarCipher(alphabetLatin)
    val caesarPermutationCipher: Cipher = CaesarPermutationCipher(seed, alphabetLatin)
    val vigenereCipher: Cipher = VigenereCipher(alphabetLatin)


    val ciphertextCaesar = caesarCipher.encrypt(cleartext, caesarKey)
    println("Caesar Cipher (K=$caesarKey) encrypted '$cleartext' to '$ciphertextCaesar'. \n" +
            "Caesar Cipher decrypted '$ciphertextCaesar' to '${caesarCipher.decrypt(ciphertextCaesar, caesarKey)}'. ")

    val ciphertextCaesarPerm = caesarPermutationCipher.encrypt(cleartext, caesarKey)
    println("Caesar Permutation Cipher (K=$caesarKey, SEED=$seed) encrypted '$cleartext' to '$ciphertextCaesarPerm'. \n" +
            "Caesar Permutation Cipher decrypted '$ciphertextCaesarPerm' to '${caesarPermutationCipher.decrypt(ciphertextCaesar, caesarKey)}'. ")

    val ciphertextVigenere = vigenereCipher.encrypt(cleartext2, vigenereKey)
    println("Vigenere Cipher (K=$vigenereKey) encrypted '$cleartext2' to '$ciphertextVigenere'. \n" +
            "Vigenere Cipher decrypted '$ciphertextVigenere' to '${vigenereCipher.decrypt(ciphertextVigenere, vigenereKey)}'. ")


}