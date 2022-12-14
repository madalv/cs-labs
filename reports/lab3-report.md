# Lab 3 Report

### Course: Cryptography & Security

### Author: Magal Vlada, FAF-203

----

## Overview:

&ensp;&ensp;&ensp; Asymmetric Cryptography (a.k.a. Public-Key Cryptography)deals with the encryption of plain text when having 2 keys, one being public and the other one private. The keys form a pair and despite being different they are related.

&ensp;&ensp;&ensp; As the name implies, the public key is available to the public but the private one is available only to the authenticated recipients.

&ensp;&ensp;&ensp; A popular use case of the asymmetric encryption is in SSL/TLS certificates along side symmetric encryption mechanisms. It is necessary to use both types of encryption because asymmetric ciphers are computationally expensive, so these are usually used for the communication initiation and key exchange, or sometimes called handshake. The messages after that are encrypted with symmetric ciphers.

## Objectives:
1. Get familiar with the asymmetric cryptography mechanisms.

2. Implement an example of an asymmetric cipher.

3. As in the previous task, please use a client class or test classes to showcase the execution of your programs.

## Implementation Description:

### RSA 

1. **Generating the keys:**

   - The `RSA` class takes as input 2 keys, `p` and `q`, which must be co-prime.
   - Calculate`n = p * q`.
   - Calculate `λ(pq) = lcm(p−1, q−1)`, where `lcm` = least common multiple (the Carmichael function)
   - Select integer `e`, so it is co-orime to `λ(pq)`  and `1 < e < λ(pq)`. ** The pair (n, e) = public key.**
   - Calculate integer `d`, such that `e * d = 1 mod λ(pq)`. ** The pair (n, d) = private key.**

```kt
    init {
        if (! isCoPrime(p, q)) throw IllegalArgumentException("Given p and q must be co-prime.")
        n = p * q
        // lambda function aka reduced lambda,
        // "equivalent" to lambda in the context of rsa
        val lambda = lcm(p - 1, q - 1)
        e = calculateE(lambda)
        d = calculateD(lambda)
    }
    
        private fun calculateD(lambda: Long): Long {
        var d = 1L
        while (d <= 0L || ( e * d ).rem ( lambda ) != 1L) {
            d ++
        }
        return d
    }

    private fun calculateE(lambda: Long): Long {
        val rnd = Random()
        var e = 0L
        while (e <= 1L || ! isCoPrime(lambda, e)) {
            e = ( abs (
                rnd.nextLong()) + 2L
                    ).rem(lambda)
        }
        return e
    }
```

2. **Encryption/Decryption:**

The encryption scheme is done as such, where C is the ciphertext and P - the plaintext:

$$C = P^e mod n$$

Now the decryption:

$$P = C^d mod n$$

Both the `encrypt` and `decrypt` functions use the `convert` function,
since both encryption and decryption are quite similar:

```kt
    private fun convert(value1: Long, value2: Long): Long {
        var result = 1L
        for (i in 1 .. value2) {
            result *= value1

            if (result >= n)
                result = result.rem ( n )
        }
        return result
    }
```

## Conclusions:

In this lab I got familiar with the asymmetric cryptography mechanisms, 
researched and implemented the RSA (Rivest–Shamir–Adleman) algorithm. 
A test for RSA encryption/decryption can be found in the `tests/asymmetric` folder.

