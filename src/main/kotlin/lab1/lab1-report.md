# Lab 1 Report

### Course: Cryptography & Security
### Author: Magal Vlada, FAF-203

----

## Objectives:

* Get familiar with the basics of Cryptography and classical ciphers.
* Implement 4 types of classical cyphers:
  - Caesar cipher,
  - Caesar cipher with an alphabet permutation,
  - Vigenere cipher,
  - Playfair cipher.
* Structure the project in methods/classes/packages as needed.



## Implementation Description

### Caesar Cipher

The `CaesarCipher` Class implements the Cipher interface, and thus has an `encrypt`
function and a `decrypt` one. The only constructor parameter is a lowercase alphabet as a String.

```kotlin
override fun encrypt(plaintext: String, key: Any): String {
  var ciphertext = ""
  for (char in plaintext) ciphertext += transformCharacter(key as Int, char)
  return ciphertext
}
```

The `decrypt` function is based on the `encrypt` one:

```kotlin
override fun decrypt(ciphertext: String, key: Any): String {
    val decryptKey = alphabet.length - key as Int % alphabet.length
    return this.encrypt(ciphertext, decryptKey)
}
```

`transformCharacter` just returns a shifted letter based on the key.

### Caesar Permutation Cipher 

```kotlin
class CaesarPermutationCipher(seed: Long, alphabet : String)
    : CaesarCipher(alphabet.toList().shuffled(Random(seed)).joinToString(""))
```

That is the entire `CaesarPermutationCipher` Class. Oh, the joys of inheritance. It performs 
exactly as `CaesarCipher` just with the added bonus of a permuted alphabet. Its constructor parameters 
are the seed, used to control the shuffle of the alphabet letters, and, of course, the alphabet.

### Vigenere Cipher

`VigenereCipher` inherits from `CaesarCipher`. Its `encrypt` function is based on `CaesarCipher`'s:
```kotlin
override fun encrypt(plaintext: String, key: Any): String {
  var ciphertext = ""
  plaintext.forEachIndexed { index, c ->
    val shift = calculateKeyLetterIndex(key as String, index)
    ciphertext += super.encrypt(c.toString(), shift)
  }
  return ciphertext
}
```

Each letter of the plaintext is encrypted using Caesar, just with a different key (`shift`), based on the current plaintext
index and the key.

```kotlin
override fun decrypt(ciphertext: String, key: Any): String {
    var plaintext = ""
    ciphertext.forEachIndexed { index, c ->
        val shift = calculateKeyLetterIndex(key as String, index)
        plaintext += super.transformCharacter(-shift, c)
    }
    return plaintext
}
```

For decryption, each letter of the ciphertext is shifted "backwards", using the negative of `shift`.

### Playfair Cipher

First, the Playfair table used for encryption/decryption must be created:
```kt
 private fun constructTable(key: String): String {
    var table = alphabet.uppercase()
    for (i in key.length - 1 downTo 0)
        table = key[i] + table.replace(key[i].toString(), "")
    return table.replace("J", "")
}
```

In the implementation, it is simulated by a string which begins with the key letters, then continues with the alphabet, 
excluding repetitions (also "J").

```kotlin
override fun encrypt(plaintext: String, key: Any): String {

    val pt = preparePlaintext(plaintext)
    var ciphertext = ""
    val table = constructTable(key as String)

    for (i in pt.indices step 2)
        ciphertext += transformDigraph(table, pt[i], pt[i + 1], 1, 5)
    return ciphertext
}
```

The encryption takes place by digraphs, transforming them according to the PLayfair cipher rules:

```kotlin
        return if (l1Row == l2Row) 
            // if on the same row, get letters directly to the right
            getLettersWithShift(table, l1Index, l2Index, rowShit)
        else if (l1Col == l2Col) 
            // if on the same column, get letters directly below
            getLettersWithShift(table, l1Index, l2Index, colShift)
            // else get letters from the opposite corners of the minimatrix
        else getOppositeCorners(table, l1Row, l1Col, l2Row, l2Col)
```

The decryption process is very similar to the encryption one, just with negated  `rowShift` and 
`colShift` values.

## Conclusions

In this lab I got familiar with 4 types of classical ciphers and implemented them. It was 
relatively simple but it allowed me to better understand the ciphers I already knew and new ones,
such as Playfair.