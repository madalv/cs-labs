package lab1.implementations

import lab1.Cipher
import java.util.*

open class CaesarCipher(val alphabet: String): Cipher {
    protected val alphabetUpper = alphabet.uppercase(Locale.ENGLISH)

    override fun encrypt(plaintext: String, key: Any): String {
        var ciphertext = ""
        for (char in plaintext) ciphertext += transformCharacter(key as Int, char)
        return ciphertext
    }

    override fun decrypt(ciphertext: String, key: Any): String {
        val decryptKey = alphabet.length - key as Int % alphabet.length
        return this.encrypt(ciphertext, decryptKey)
    }

    protected fun transformCharacter(key: Int, char: Char): Char {
        return if (char.isUpperCase()) {
            val ix = alphabetUpper.indexOf(char)
            alphabetUpper[calculateTransformedIndex(ix, key)]
        }
        else if (char.isLowerCase()) {
            val ix = alphabet.indexOf(char)
            alphabet[calculateTransformedIndex(ix, key)]
        }
        else return ' '
    }

    // in case the (char index + key) is ever negative, just add alphabet.size to get correct modulo
    private fun calculateTransformedIndex(ix: Int, key: Int): Int {
        var index = (ix + key) % alphabet.length
        if (index < 0) index += alphabet.length
        return index
    }
}