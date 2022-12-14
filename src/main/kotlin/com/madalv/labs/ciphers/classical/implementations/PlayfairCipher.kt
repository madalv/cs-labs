package ciphers.classical.implementations

import com.madalv.labs.ciphers.classical.Cipher
import java.util.*

class PlayfairCipher(private val alphabet: String) : Cipher {

    private val extraKey = "X"

    override fun encrypt(plaintext: String, key: Any): String {

        val pt = preparePlaintext(plaintext)
        var ciphertext = ""

        val table = constructTable(key as String)

        for (i in pt.indices step 2)
            ciphertext += transformDigraph(table, pt[i], pt[i + 1], 1, 5)
        return ciphertext
    }

    override fun decrypt(ciphertext: String, key: Any): String {
        val pt = preparePlaintext(ciphertext)
        var plaintext = ""

        val table = constructTable(key as String)

        for (i in pt.indices step 2)
            plaintext += transformDigraph(table, pt[i], pt[i + 1], -1, -5)
        return plaintext
    }

    private fun preparePlaintext(p: String): String {
        var s = p.uppercase(Locale.ENGLISH)
            .replace(" ", "").replace("J", "I")

        for (i in s.indices step 2)
            if (s[i] == s[i + 1]) // add rare letter inbetween 2 repeating letters
                s = s.substring(0, i + 1) + extraKey + s.substring(i + 1)

        if (s.length % 2 != 0) s += extraKey // if nr of letters is odd, add rare letter
        return s
    }

    private fun transformDigraph(table: String, l1: Char, l2: Char, rowShit: Int, colShift: Int): String {
        val l1Index = table.indexOf(l1)
        val l2Index = table.indexOf(l2)
        val l1Row = l1Index / 5
        val l1Col = l1Index % 5
        val l2Row = l2Index / 5
        val l2Col = l2Index % 5

        return if (l1Row == l2Row) // if on the same row, get letters directly to the right
            getLettersWithShift(table, l1Index, l2Index, rowShit)
        else if (l1Col == l2Col) // if on the same column, get letters directly below
            getLettersWithShift(table, l1Index, l2Index, colShift)
        // else get letters from the opposite corners of the minimatrix
        else getOppositeCorners(table, l1Row, l1Col, l2Row, l2Col)
    }

    private fun constructTable(key: String): String {
        var table = alphabet.uppercase()
        for (i in key.length - 1 downTo 0)
            table = key[i] + table.replace(key[i].toString(), "")
        return table.replace("J", "")
    }

    private fun getLettersWithShift(table: String, l1Index: Int, l2Index: Int, shift: Int): String {
        return table[(l1Index + shift) % table.length].toString() + table[(l2Index + shift) % table.length]
    }

    private fun getOppositeCorners(table: String, l1Row: Int, l1Col: Int, l2Row: Int, l2Col: Int): String {
        return table[l1Row * 5 + l2Col].toString() + table[l2Row * 5 + l1Col]
    }
}