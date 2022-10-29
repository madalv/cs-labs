package ciphers.symmetric.implementations;

import java.nio.charset.Charset;
import java.util.Arrays;

public class Rabbit {

    public Rabbit(String iv, boolean addPadding, boolean trimPadding, Charset charset) {
        this.iv = iv;
        this.addPadding = addPadding;
        this.trimPadding = trimPadding;
        b = 0;
        this.charset = charset;
    }

    private final String iv;

    private final Charset charset;
    private final boolean addPadding;
    private final boolean trimPadding;

    public static final int KEYSTREAM_LENGTH = 16;
    public static final int IV_LENGTH = 8;

    private static final int[] A = new int[]{0x4D34D34D, 0xD34D34D3,
            0x34D34D34, 0x4D34D34D, 0xD34D34D3, 0x34D34D34, 0x4D34D34D,
            0xD34D34D3};

    private final int rotl(final int value, final int shift) {
        return value << shift | value >>> 32 - shift;
    }

    private final int[] X = new int[IV_LENGTH];
    private final int[] C = new int[IV_LENGTH];
    private byte b;
    private int keyindex = 0;
    private byte[] keystream = null;


    public byte[] encrypt(final String message, String key) {
        if (message == null || key == null || charset == null
                || message.isEmpty() || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        byte[] msg = null;
        if (addPadding) {
            msg = addPadding(message.getBytes(charset));
        } else {
            msg = message.getBytes(charset);
        }
        byte[] byteKey = getKeyFromString(key, charset);

        reset();
        setupKey(byteKey);
        if (iv != null && !iv.isEmpty()) {
            byte[] byteIV = getIVFromString(iv, charset);
            setupIV(byteIV);
        }
        byte[] crypt = crypt(msg);
        reset();
        return crypt;
    }


    private byte[] getIVFromString(String iv, Charset charset) {
        return Arrays.copyOf(iv.getBytes(charset), IV_LENGTH);
    }


    private byte[] getKeyFromString(String key, Charset charset) {
        return Arrays.copyOf(key.getBytes(charset), KEYSTREAM_LENGTH);
    }


    public String decrypt(final byte[] encMessage, String key) {
        if (encMessage == null || key == null || charset == null
                || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        byte[] byteKey = getKeyFromString(key, charset);

        reset();
        setupKey(byteKey);
        if (iv != null && !iv.isEmpty()) {
            byte[] byteIV = getIVFromString(iv, charset);
            setupIV(byteIV);
        }
        byte[] crypt = crypt(encMessage);
        reset();
        if (trimPadding) {
            return new String(crypt, charset).trim();
        } else {
            return new String(crypt, charset);
        }
    }

    private byte[] addPadding(final byte[] message) {
        if (message.length % KEYSTREAM_LENGTH != 0) {
            return Arrays.copyOf(message, message.length + message.length
                    % KEYSTREAM_LENGTH);
        } else {
            return message;
        }

    }

    public byte[] crypt(final byte[] message) {
        int index = 0;
        while (index < message.length) {
            if (keystream == null || keyindex == KEYSTREAM_LENGTH) {
                keystream = keyStream();
                keyindex = 0;
            }
            for (; keyindex < KEYSTREAM_LENGTH && index < message.length; ++keyindex)
                message[index++] ^= keystream[keyindex];
        }
        return message;
    }

    private byte[] keyStream() {
        nextState();
        final byte[] s = new byte[KEYSTREAM_LENGTH];
        /* unroll */
        int x = X[6] ^ X[3] >>> 16 ^ X[1] << 16;
        s[0] = (byte) (x >>> 24);
        s[1] = (byte) (x >> 16);
        s[2] = (byte) (x >> 8);
        s[3] = (byte) x;
        x = X[4] ^ X[1] >>> 16 ^ X[7] << 16;
        s[4] = (byte) (x >>> 24);
        s[5] = (byte) (x >> 16);
        s[6] = (byte) (x >> 8);
        s[7] = (byte) x;
        x = X[2] ^ X[7] >>> 16 ^ X[5] << 16;
        s[8] = (byte) (x >>> 24);
        s[9] = (byte) (x >> 16);
        s[10] = (byte) (x >> 8);
        s[11] = (byte) x;
        x = X[0] ^ X[5] >>> 16 ^ X[3] << 16;
        s[12] = (byte) (x >>> 24);
        s[13] = (byte) (x >> 16);
        s[14] = (byte) (x >> 8);
        s[15] = (byte) x;
        return s;
    }

    private void nextState() {
        /* counter update */
        for (int j = 0; j < IV_LENGTH; ++j) {
            final long t = (C[j] & 0xFFFFFFFFL) + (A[j] & 0xFFFFFFFFL) + b;
            b = (byte) (t >>> 32);
            C[j] = (int) (t & 0xFFFFFFFF);
        }
        /* next state function */
        final int G[] = new int[IV_LENGTH];
        for (int j = 0; j < IV_LENGTH; ++j) {
            long t = X[j] + C[j] & 0xFFFFFFFFL;
            G[j] = (int) ((t *= t) ^ t >>> 32);
        }
        /* unroll */
        X[0] = G[0] + rotl(G[7], 16) + rotl(G[6], 16);
        X[1] = G[1] + rotl(G[0], 8) + G[7];
        X[2] = G[2] + rotl(G[1], 16) + rotl(G[0], 16);
        X[3] = G[3] + rotl(G[2], 8) + G[1];
        X[4] = G[4] + rotl(G[3], 16) + rotl(G[2], 16);
        X[5] = G[5] + rotl(G[4], 8) + G[3];
        X[6] = G[6] + rotl(G[5], 16) + rotl(G[4], 16);
        X[7] = G[7] + rotl(G[6], 8) + G[5];
    }

    public void reset() {
        b = 0;
        keyindex = 0;
        keystream = null;
        Arrays.fill(X, 0);
        Arrays.fill(C, 0);
    }

    public void setupIV(final byte[] IV) {
        short[] sIV = new short[IV.length >> 1];
        for (int i = 0; i < sIV.length; ++i) {
            sIV[i] = (short) ((IV[i << 1] << 8) | IV[(2 << 1) + 1]);
        }
        setupIV(sIV);
    }

    public void setupIV(final short[] iv) {
        /* unroll */
        C[0] ^= iv[1] << 16 | iv[0] & 0xFFFF;
        C[1] ^= iv[3] << 16 | iv[1] & 0xFFFF;
        C[2] ^= iv[3] << 16 | iv[2] & 0xFFFF;
        C[3] ^= iv[2] << 16 | iv[0] & 0xFFFF;
        C[4] ^= iv[1] << 16 | iv[0] & 0xFFFF;
        C[5] ^= iv[3] << 16 | iv[1] & 0xFFFF;
        C[6] ^= iv[3] << 16 | iv[2] & 0xFFFF;
        C[7] ^= iv[2] << 16 | iv[0] & 0xFFFF;

        nextState();
        nextState();
        nextState();
        nextState();
    }

    public void setupKey(final byte[] key) {
        short[] sKey = new short[key.length >> 1];
        for (int i = 0; i < sKey.length; ++i) {
            sKey[i] = (short) ((key[i << 1] << 8) | key[(2 << 1) + 1]);
        }
        setupKey(sKey);
    }

    public void setupKey(final short[] key) {
        /* unroll */
        X[0] = key[1] << 16 | key[0] & 0xFFFF;
        X[1] = key[6] << 16 | key[5] & 0xFFFF;
        X[2] = key[3] << 16 | key[2] & 0xFFFF;
        X[3] = key[0] << 16 | key[7] & 0xFFFF;
        X[4] = key[5] << 16 | key[4] & 0xFFFF;
        X[5] = key[2] << 16 | key[1] & 0xFFFF;
        X[6] = key[7] << 16 | key[6] & 0xFFFF;
        X[7] = key[4] << 16 | key[3] & 0xFFFF;
        /* unroll */
        C[0] = key[4] << 16 | key[5] & 0xFFFF;
        C[1] = key[1] << 16 | key[2] & 0xFFFF;
        C[2] = key[6] << 16 | key[7] & 0xFFFF;
        C[3] = key[3] << 16 | key[4] & 0xFFFF;
        C[4] = key[0] << 16 | key[1] & 0xFFFF;
        C[5] = key[5] << 16 | key[6] & 0xFFFF;
        C[6] = key[2] << 16 | key[3] & 0xFFFF;
        C[7] = key[7] << 16 | key[0] & 0xFFFF;
        nextState();
        nextState();
        nextState();
        nextState();
        /* unroll */
        C[0] ^= X[4];
        C[1] ^= X[5];
        C[2] ^= X[6];
        C[3] ^= X[7];
        C[4] ^= X[0];
        C[5] ^= X[1];
        C[6] ^= X[2];
        C[7] ^= X[3];
    }

}