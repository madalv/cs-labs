package ciphers.symmetric.implementations;

public class SDES {

    public static int encrypt(int c, int[] keys) {
        int result = f(IP(c), keys[0]);
        result = (result << 28) >>> 24 | (result >>> 4);
        result = f(result, keys[1]);
        return inverseIP(result);
    }

    public static int decrypt(int c, int[] keys) {
        int[] newKeys = new int[2];
        newKeys[0] = keys[1];
        newKeys[1] = keys[0];
        return encrypt(c, newKeys);
    }

    static int f(int plainText, int subKey) {
        int L = plainText >>> 4;
        int R = plainText << 28 >>> 28;
        return (L ^ F(R, subKey)) << 4 | R;
    }

    static int IP(int plainText) {
        return permute(plainText, 1, 3, 0, 4, 7, 5, 2, 6);
    }

    static int inverseIP(int cryptoText) {
        return permute(cryptoText, 2, 0, 6, 1, 3, 5, 7, 4);
    }

    static int permute(int bits, int... pos) {
        int permutedBits = 0;
        for (int i = 0; i < pos.length; i++)
            permutedBits |= ((bits >>> pos[i]) & 1) << i;
        return permutedBits;
    }

    static int F(int plainText, int subKey) {
        int permutation = permute(plainText, 3, 0, 1, 2, 1, 2, 3, 0);
        permutation ^= subKey;

        int substituted = 0;
        int i = ((permutation & (1 << 7)) >>> 6) | (permutation & (1 << 4)) >>> 4;
        int j = ((permutation & (1 << 6)) >>> 5) | (permutation & (1 << 5)) >>> 5;
        substituted |= S0[i][j] << 2;
        i = ((permutation & (1 << 3)) >>> 2) | (permutation & 1);
        j = ((permutation & (1 << 2)) >>> 1) | (permutation & (1 << 1)) >>> 1;
        substituted |= S1[i][j];

        return permute(substituted, 3, 1, 0, 2);
    }

    private final static int[][] S0 = new int[][]{
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 1}
    };

    private final static int[][] S1 = new int[][]{
            {1, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3}
    };
}