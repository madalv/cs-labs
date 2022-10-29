package tests.symmetric;

import ciphers.symmetric.implementations.Rabbit;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class RabbitTest {
    private Rabbit rabbit;
    private String message;
    private String key;
    private String IV;

    @Before
    public void setUp() throws Exception {
        message = "HelloRabbitCipher!";
        key = "verysecretkey";
        String iv = "ivivivivivivivivivivivivivi";
        boolean addPadding = true;
        boolean trimPadding = true;
        rabbit = new Rabbit(iv, addPadding, trimPadding, Charset.defaultCharset());
    }

    @Test
    public void testCryptMessage() {
        final byte[] encryptedMessage = rabbit.encrypt(message, key);
        final String decryptedString = rabbit.decrypt(encryptedMessage, key);
        assertEquals(message, decryptedString);
    }

}