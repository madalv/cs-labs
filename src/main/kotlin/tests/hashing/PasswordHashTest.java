package tests.hashing;

import hashing.account.AccountManager;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PasswordHashTest {

    AccountManager accountManager = AccountManager.INSTANCE;

    @Test public void testCorrectCredentials() {
        accountManager.addAccount("v@magal.com", "password");
        assertTrue(accountManager.verifyPassword("v@magal.com", "password"));
    }

    @Test public void testWrongCredentials() {
        accountManager.addAccount("v@magal.com", "password");
        assertFalse(accountManager.verifyPassword("v@magal.com", "idkman"));
    }
}
