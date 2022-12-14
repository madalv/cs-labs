# Lab 4 Report

### Course: Cryptography & Security

### Author: Magal Vlada, FAF-203

----

## Overview:

&ensp;&ensp;&ensp; Hashing is a technique used to compute a new representation of an existing value, message or any piece of text. The new representation is also commonly called a digest of the initial text, and it is a one way function meaning that it should be impossible to retrieve the initial content from the digest.

&ensp;&ensp;&ensp; Such a technique has the following usages:
* Offering confidentiality when storing passwords,
* Checking for integrity for some downloaded files or content,
* Creation of digital signatures, which provides integrity and non-repudiation.

&ensp;&ensp;&ensp; In order to create digital signatures, the initial message or text needs to be hashed to get the digest. After that, the digest is to be encrypted using a public key encryption cipher. Having this, the obtained digital signature can be decrypted with the public key and the hash can be compared with an additional hash computed from the received message to check the integrity of it.

## Objectives:
1. Get familiar with the hashing techniques/algorithms.
2. Use an appropriate hashing algorithms to store passwords in a local DB.
    1. You can use already implemented algortihms from libraries provided for your language.
    2. The DB choise is up to you, but it can be something simple, like an in memory one.
3. Use an asymmetric cipher to implement a digital signature process for a user message.
    1. Take the user input message.
    2. Preprocess the message, if needed.
    3. Get a digest of it via hashing.
    4. Encrypt it with the chosen cipher.
    5. Perform a digital signature check by comparing the hash of the message with the decrypted one.

## Implementation Description:

### Hashing

`AccountManager` deals with addding accounts to the datastore and verifying the password hash.

```kt
 fun addAccount(email: String, password: String): PrivateKey {

     if (db.get(email) != null) {
         throw Exception("Account already exists.")
     }

     val keyPair = kpg.genKeyPair()
     val pubk = keyPair.public

     val acc = Account(email,
         messageDigest.digest(password.toByteArray()), pubk)
     db.set(email,acc)

     return keyPair.private
 }
```

`addAccount` creates a new account, storing the public key (necessary for digital signatures), password hash and email
in the in-memory datastore. It returns a private key, which is used for digital signatures.

```kt
 fun verifyPassword(email: String, password: String): Boolean {
     if (db.get(email) == null) {
         throw Exception("No such account.")
     }
     return messageDigest.digest(password.toByteArray()).contentEquals(db.get(email)!!.password)
 }
```

`verifyPassword` verifies the password hash against the hash in the datastore and returns the result.

### Digital Signatures

`MessageManager` deals with signing and verifying messages.

```kt
 fun signMessage(message: String, prik: PrivateKey): ByteArray {
     val signature: Signature = Signature.getInstance("SHA256withRSA")
     val bytes = message.toByteArray()

     signature.apply {
         initSign(prik)
         update(bytes)
     }
     return signature.sign()
 }
```

`signMessage` takes in the message and private key and returns the signed message. Here we get a instance of a
`SHA256withRSA` Signature (the message is digested with SHA-256 and encrypted with RSA).

```kt 
 fun verifySignature(message: String, signed: ByteArray, email: String): Boolean {
     val bytes = message.toByteArray()
     val signature: Signature = Signature.getInstance("SHA256withRSA")
     val acc = accountManager.getAccount(email)

     signature.apply {
         initVerify(acc.publicKey)
         update(bytes)
     }
     return signature.verify(signed)
 }
```

`verifySignature` verifies the signed message against the original message. The signed message first gets decrypted 
using the public key stored in the datastore, then compares the hashes and returns the result. 

## Conclusions:

In this lab I got familiar with hashing techniques, and used RSA to store passwords to a in-memory datastore.
Also I used RSA and SHA-256 to create a digital signatures and verify the integrity of a message. Tests for this lab can be found in `tests/hashing`.
