package ciphers.classical.implementations

import com.madalv.labs.ciphers.classical.implementations.CaesarCipher
import kotlin.random.Random

// derived class of Caesar cipher, uses the same methods as the super class,
// just with a permuted alphabet (fed to Caesar constructor)
class CaesarPermutationCipher(seed: Long, alphabet: String) :
    CaesarCipher(alphabet.toList().shuffled(Random(seed)).joinToString(""))