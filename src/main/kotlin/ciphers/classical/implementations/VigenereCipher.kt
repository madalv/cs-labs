package ciphers.classical.implementations

class VigenereCipher(alphabet: String) : CaesarCipher(alphabet) {

    override fun encrypt(plaintext: String, key: Any): String {
        var ciphertext = ""
        plaintext.forEachIndexed { index, c ->
            val shift = calculateKeyLetterIndex(key as String, index)
            // for each char, CiphertextIndex = (PlaintextIndex + KeyLetterIndex) mod 26
            // uses the Caesar encrypt function to implement the formula above
            ciphertext += super.encrypt(c.toString(), shift)
        }
        return ciphertext
    }

    override fun decrypt(ciphertext: String, key: Any): String {
        var plaintext = ""
        ciphertext.forEachIndexed { index, c ->
            val shift = calculateKeyLetterIndex(key as String, index)
            // for each char, DecryptedIndex = (CiphertextIndex - KeyLetterIndex) mod 26
            // uses the Caesar transform function to implement the formula above
            plaintext += super.transformCharacter(-shift, c)
        }
        return plaintext
    }

    private fun calculateKeyLetterIndex(keyString: String, index: Int): Int {
        // get current key letter based on the index of the current text character
        val keyLetter = keyString[index % keyString.length]
        // check if key letter is upper case or lower
        val indexOfKeyLetter = alphabet.indexOf(keyLetter)
        return if (indexOfKeyLetter != -1) indexOfKeyLetter
        else alphabetUpper.indexOf(keyLetter)
    }
}