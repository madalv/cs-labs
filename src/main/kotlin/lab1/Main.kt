package lab1

import lab1.implementations.CaesarCipher
import lab1.implementations.CaesarPermutationCipher
import lab1.implementations.PlayfairCipher
import lab1.implementations.VigenereCipher

fun main() {
    val alphabetLatin = "abcdefghijklmnopqrstuvwxyz"
    val seed: Long = 6
    val caesarKey = 28
    val vigenereKey = "BEST"
    val playfairKey = "DIAMOND"
    val plaintext = "ATTaCK AT MiDNIGht"
    val plaintext2 = "CyberSecurity"
    val plaintext3 = "BIRMINGHAM"


    val caesarCipher: Cipher = CaesarCipher(alphabetLatin)
    val caesarPermutationCipher: Cipher = CaesarPermutationCipher(seed, alphabetLatin)
    val vigenereCipher: Cipher = VigenereCipher(alphabetLatin)
    // note: will only work with the Latin alphabet
    val playfairCipher: Cipher = PlayfairCipher(alphabetLatin)


    val ciphertextCaesar = caesarCipher.encrypt(plaintext, caesarKey)
    println("Caesar Cipher (K=$caesarKey) encrypted '$plaintext' to '$ciphertextCaesar'. \n" +
            "Caesar Cipher decrypted '$ciphertextCaesar' to '${caesarCipher.decrypt(ciphertextCaesar, caesarKey)}'. ")

    val ciphertextCaesarPerm = caesarPermutationCipher.encrypt(plaintext, caesarKey)
    println("Caesar Permutation Cipher (K=$caesarKey, SEED=$seed) encrypted '$plaintext' to '$ciphertextCaesarPerm'. \n" +
            "Caesar Permutation Cipher decrypted '$ciphertextCaesarPerm' to '${caesarPermutationCipher.decrypt(ciphertextCaesar, caesarKey)}'. ")

    val ciphertextVigenere = vigenereCipher.encrypt(plaintext2, vigenereKey)
    println("Vigenere Cipher (K=$vigenereKey) encrypted '$plaintext2' to '$ciphertextVigenere'. \n" +
            "Vigenere Cipher decrypted '$ciphertextVigenere' to '${vigenereCipher.decrypt(ciphertextVigenere, vigenereKey)}'. ")

    val ciphertextPlayfair = playfairCipher.encrypt(plaintext3, playfairKey)
    println("Playfair Cipher (K=$playfairKey) encrypted '$plaintext3' to '$ciphertextPlayfair'. \n" +
            "Playfair Cipher decrypted '$ciphertextPlayfair' to '${playfairCipher.decrypt(ciphertextPlayfair, playfairKey)}'. ")

}