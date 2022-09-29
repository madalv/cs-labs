# Lab 1 Report

### Course: Cryptography & Security
### Author: Magal Vlada, FAF-203

----

## Theory
If needed and written by the author.


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


```kotlin
override fun encrypt(plaintext: String, key: Any): String {
  var ciphertext = ""
  for (char in plaintext) ciphertext += transformCharacter(key as Int, char)
  return ciphertext
}
```

### Caesar Permutation Cipher 

### Vigenere Cipher

### Playfair Cipher


## Conclusions