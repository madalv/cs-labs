package com.madalv.labs.tests.hashing;

import com.madalv.labs.hashing.account.AccountManager;
import hashing.messages.MessageManager;
import org.junit.Test;

import java.security.PrivateKey;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DigitalSignatureTest {

    AccountManager accountManager = AccountManager.INSTANCE;
    MessageManager messageManager = MessageManager.INSTANCE;

    @Test public void testCorrectSignature() {

        String message = "hello world";
        String email = "v@magal.com";
        PrivateKey prik = accountManager.addAccount(email, "password");

        byte[] signed = messageManager.signMessage(message, prik);

        assertTrue(messageManager.verifySignature(message, signed, email));
    }

    @Test public void testWrongSignature() {

        String message = "hello world";
        String email = "v@magal.com";
        PrivateKey prik = accountManager.addAccount(email, "password");

        byte[] signed = messageManager.signMessage(message, prik);

        assertFalse(messageManager.verifySignature(message + "j", signed, email));
    }
}
