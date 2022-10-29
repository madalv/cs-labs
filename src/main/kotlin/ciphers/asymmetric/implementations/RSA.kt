package ciphers.asymmetric.implementations

import ciphers.asymmetric.AsymmetricCipher
import java.util.*
import kotlin.math.abs

class RSA (
    p: Long, // random key 1
    q: Long	// random key 2
): AsymmetricCipher {

    private val n: Long
    private val e: Long  // public part
    private val d: Long  // private part

    init {
        if (! isCoPrime(p, q)) throw IllegalArgumentException("Given p and q must be co-prime.")
        n = p * q
        // lambda function aka reduced lambda,
        // "equivalent" to lambda in the context of rsa
        val lambda = lcm(p - 1, q - 1)
        e = calculateE(lambda)
        d = calculateD(lambda)
    }

    override fun encrypt(plaintext: Long): Long {
        return convert (plaintext, e)
    }
    override fun decrypt(ciphertext: Long): Long {
        return convert (ciphertext, d)
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

    private fun convert(value1: Long, value2: Long): Long {
        // calculate manually
        var result = 1L
        for (i in 1 .. value2) {
            result *= value1

            if (result >= n)
                result = result.rem ( n )
        }

        return result
    }

    companion object {
        private var lastPrimeCheckValue = 2L
        private val primeSet = mutableSetOf(2L)

        private fun updatePrimeSet(value : Long) {
            if (value > lastPrimeCheckValue) {
                // check all numbers to currect target
                for (v in lastPrimeCheckValue + 1 .. value) {

                    var bPrime = true // presume to be prime
                    testRem@ for (p in primeSet) {
                        if (v.rem ( p ) == 0L) {
                            // not prime
                            bPrime = false
                            break@testRem
                        }
                    }

                    if (bPrime)
                        primeSet.add (v)
                }
                lastPrimeCheckValue = value
            }
        }

        fun isCoPrime(v1: Long, v2: Long ): Boolean {
            updatePrimeSet(if (v1 > v2) v1 else v2)
            for (p in primeSet) {
                if (p > v1 || p > v2)
                    return true
                if (v1.rem(p) == 0L && v2.rem (p) == 0L)
                    return false
            }
            return true
        }

        // least common multiple
        fun lcm(p: Long, q: Long): Long {
            if (p <= 0 || q <= 0)
                throw IllegalArgumentException ("value must be positive (>= 1).")

            var p1 = p
            var q1 = q
            while (p1 != q1) {
                if (p1 < q1)
                    p1 += p
                else
                    q1 += q
            }
            return p1
        }
    }
}