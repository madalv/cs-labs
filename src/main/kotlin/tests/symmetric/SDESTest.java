package tests.symmetric;

import ciphers.symmetric.implementations.SDES;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SDESTest {
    @Test
    public void encrypt() {
        byte msg = 0b00010111;
        int[] keys = new int[]{0b10100100, 0b10010010};
        int[] oldKeys = new int[]{0b10100100, 0b10010010};

        assertEquals(0b00011010, SDES.encrypt(msg, keys));
        assertArrayEquals(oldKeys, keys);
    }

    @Test
    public void decrypt() {
        byte cryptoText = 0b00011010;
        byte result = 0b00010111;
        int[] keys = new int[]{0b10100100, 0b10010010};
        int[] oldKeys = new int[]{0b10100100, 0b10010010};

        assertEquals(result, SDES.decrypt(cryptoText, keys));
        assertArrayEquals(oldKeys, keys);
    }

    @Test
    public void encryptAndDecryptSymmetry() {
        int[] keys = new int[]{0b10100100, 0b10010010};
        byte text = 0b1110011;
        int[] oldKeys = new int[]{0b10100100, 0b10010010};

        assertEquals(text, SDES.decrypt(SDES.encrypt(text, keys), keys));
        assertArrayEquals(oldKeys, keys);
    }
    
}
