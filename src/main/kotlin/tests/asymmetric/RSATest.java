package tests.asymmetric;

import ciphers.asymmetric.AsymmetricCipher;
import ciphers.asymmetric.implementations.RSA;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RSATest {

    @Test public void test() {
        AsymmetricCipher rsa = new RSA(61, 53);
        long plaintext = 2034L;
        long ciphertext = rsa.encrypt(plaintext);
        assertEquals(plaintext, rsa.decrypt(ciphertext));
    }
}
