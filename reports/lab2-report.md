# Lab 2 Report

### Course: Cryptography & Security

### Author: Magal Vlada, FAF-203

----

## Overview:

Symmetric Cryptography deals with the encryption of plain text when having only one encryption key which needs to remain
private. Based on the way the plain text is processed/encrypted there are 2 types of ciphers:

- Stream ciphers:
    - The encryption is done one byte at a time.
    - Stream ciphers use confusion to hide the plain text.
    - Make use of substitution techniques to modify the plain text.
    - The implementation is fairly complex.
    - The execution is fast.
- Block ciphers:
    - The encryption is done one block of plain text at a time.
    - Block ciphers use confusion and diffusion to hide the plain text.
    - Make use of transposition techniques to modify the plain text.
    - The implementation is simpler relative to the stream ciphers.
    - The execution is slow compared to the stream ciphers.

## Objectives:

1. Get familiar with the symmetric cryptography, stream and block ciphers.

2. Implement an example of a stream cipher.

3. Implement an example of a block cipher.

4. The implementation should, ideally follow the abstraction/contract/interface used in the previous laboratory work.

5. Please use packages/directories to logically split the files that you will have.

6. As in the previous task, please use a client class or test classes to showcase the execution of your programs.

## Implementation Description

### Rabbit Stream Cipher

Rabbit is a synchronous stream cipher that was first introduced in 2003. Its steps are as follows:

#### Key Setup

The counter carry bit b is initialized to zero. The state and
counter words are derived from the key K. The key is divided into subkeys, and the
inner state (arrays `C` and `X`, and counter bit `b`) is initialized. This is done in the
`setupKey` function:

```java
public void setupKey(final short[]key){
        X[0]=key[1]<<16|key[0]&0xFFFF;
        X[1]=key[6]<<16|key[5]&0xFFFF;
        X[2]=key[3]<<16|key[2]&0xFFFF;
        X[3]=key[0]<<16|key[7]&0xFFFF;
        X[4]=key[5]<<16|key[4]&0xFFFF;
        X[5]=key[2]<<16|key[1]&0xFFFF;
        X[6]=key[7]<<16|key[6]&0xFFFF;
        X[7]=key[4]<<16|key[3]&0xFFFF;
        /* ...*/
        nextState();
        nextState();
        nextState();
        nextState();
        /* ...*/
        }
```

The system is then iterated four times, each iteration consisting of
counter update and [next-state function](###next-state).

A more detailed description of the exact logic of the key setup can be found
in [Rabbit Cipher Descrption](https://www.rfc-editor.org/rfc/rfc4503#section-2.7).

#### IV Setup

After the key setup, the resulting inner state is saved as a master
state. Then the IV setup is run to obtain the first encryption
starting state. The system is then iterated four times, each iteration consisting of
counter update and [next-state function](###next-state). The `C` array (part of state) is updated like this:

```java
public void setupIV(final short[]iv){
        C[0]^=iv[1]<<16|iv[0]&0xFFFF;
        C[1]^=iv[3]<<16|iv[1]&0xFFFF;
        C[2]^=iv[3]<<16|iv[2]&0xFFFF;
        C[3]^=iv[2]<<16|iv[0]&0xFFFF;
        C[4]^=iv[1]<<16|iv[0]&0xFFFF;
        C[5]^=iv[3]<<16|iv[1]&0xFFFF;
        C[6]^=iv[3]<<16|iv[2]&0xFFFF;
        C[7]^=iv[2]<<16|iv[0]&0xFFFF;

        nextState();
        nextState();
        nextState();
        nextState();
        }
```

#### Next State & Counter Update

Before each execution of the next-state function the
counter system has to be updated. This system uses constants A1 through A7 (array `A` in the implementation).
The counter bit `b` is also updated.

```java
/* counter update */
for(int j=0;j<IV_LENGTH; ++j){
final long t=(C[j]&0xFFFFFFFFL)+(A[j]&0xFFFFFFFFL)+b;
        b=(byte)(t>>>32);
        C[j]=(int)(t&0xFFFFFFFF);
        }
```

The actual `nextStep()` function is performed like this:

```java
/* counter update stuff */
final int G[]=new int[IV_LENGTH];
        for(int j=0;j<IV_LENGTH; ++j){
        long t=X[j]+C[j]&0xFFFFFFFFL;
        G[j]=(int)((t*=t)^t>>>32);
        }

        X[0]=G[0]+rotl(G[7],16)+rotl(G[6],16);
        X[1]=G[1]+rotl(G[0],8)+G[7];
        X[2]=G[2]+rotl(G[1],16)+rotl(G[0],16);
        X[3]=G[3]+rotl(G[2],8)+G[1];
        X[4]=G[4]+rotl(G[3],16)+rotl(G[2],16);
        X[5]=G[5]+rotl(G[4],8)+G[3];
        X[6]=G[6]+rotl(G[5],16)+rotl(G[4],16);
        X[7]=G[7]+rotl(G[6],8)+G[5];
```

For the actual formulas and reasons why it happens this way please refer to a
proper [documentation](https://www.rfc-editor.org/rfc/rfc4503#section-2.6) of Rabbit.

#### Extraction Scheme

After the key and IV setup are concluded, the algorithm is iterated
in order to produce one output block, S, per round. Each
round consists of executing the counter update and next-step function and then extracting an
output S as follows:

```java
private byte[]keyStream(){
        nextState();
final byte[]s=new byte[KEYSTREAM_LENGTH];

        int x=X[6]^X[3]>>>16^X[1]<<16;
        s[0]=(byte)(x>>>24);
        s[1]=(byte)(x>>16);
        s[2]=(byte)(x>>8);
        s[3]=(byte)x;
        x=X[4]^X[1]>>>16^X[7]<<16;
        /* more of these ungodly formulas */
        }
```

#### Encryption & Decryption Scheme

Given the output `S` (from the extraction scheme), message `M`, and encrypted message `E`,
encryption and decryption are performed

```
E = M ^ S
M = E ^ S
```

Like so in the `crypt` function:

```java
/* ... */
for(;keyindex<KEYSTREAM_LENGTH &&index<message.length;++keyindex)
        message[index++]^=keystream[keyindex];
```

For more details, check `encrypt` and `decrypt` functions in `Rabbit.java`.

### SDES Block Cipher

The overall structure of the simplified DES. The S-DES encryption algorithm
takes an 8-bit block of plaintext (example: 10111101) and a 10-bit key as input
and produces an 8-bit block of ciphertext as output. The S-DES decryption algorithm
takes an 8-bit block of ciphertext and the same 10-bit key used to produce that
ciphertext as input and produces the original 8-bit block of plaintext.

#### The Encryption Algorithm

```java
public static int encrypt(int c,int[]keys){
        int result=f(IP(c),keys[0]);
        result=(result<<28)>>>24|(result>>>4);
        result=f(result,keys[1]);
        return inverseIP(result);
        }
```

The encryption algorithm consists of following steps:

1. Initial Permutation (`IP`);
2. The `f` function, with subkey `SK1`;
3. Switching the 2 halves of the result of `f` (the 3rd line in the code example above);
4. The `f` function... again with subkey `SK2`;
5. The inverse permutation of `IP`.

**IP: Initial Permutation and its Inverse**

This is just a simple permutation of the plaintext. In this implementation, plaintext = int array.

```java
static int IP(int plainText){
        return permute(plainText,1,3,0,4,7,5,2,6);
        }

static int inverseIP(int cryptoText){
        return permute(cryptoText,2,0,6,1,3,5,7,4);
        }
```

**The `f` Function**

The most complex component of S-DES is the function `f`,
which consists of a combination of permutation and substitution functions.
The functions can be expressed as follows. Let `L `and `R` be the leftmost 4 bits and
rightmost 4 bits of the 8-bit input to `f`, and let `F` be a mapping (not necessarily one to one) from 4-bit strings to
4-bit strings. Then:

```
f(L, R) = (L ^ F(R, SK), R)
```

where `SK` is the subkey.

**The `F` function**

The `F` function takes as input 4 bits (in this implementation, 4 expanded). It:

- Expands and permutes the bits. (again, in this implementation, `F` received the bits expanded);
- XORs the bits with the `SK`;
- The first 4 bits are fed to sbox `S0` to produce a 2 bit output;
- The other 4 bits are fed to sbox `S1` to produce another 2 bit output;
- The 4 bits received from the previous 2 steps are permuted and returned.

```java
static int F(int plainText,int subKey){
        int permutation=permute(plainText,3,0,1,2,1,2,3,0);
        permutation^=subKey;

        int substituted=0;
        int i=((permutation&(1<<7))>>>6)|(permutation&(1<<4))>>>4;
        int j=((permutation&(1<<6))>>>5)|(permutation&(1<<5))>>>5;
        substituted|=S0[i][j]<<2;
        i=((permutation&(1<<3))>>>2)|(permutation&1);
        j=((permutation&(1<<2))>>>1)|(permutation&(1<<1))>>>1;
        substituted|=S1[i][j];

        return permute(substituted,3,1,0,2);
        }
```

#### Decryption

The decryption is performed exactly the same as encryption, just with a subkey switch:

```java
public static int decrypt(int c,int[]keys){
        int[]newKeys=new int[2];
        newKeys[0]=keys[1];
        newKeys[1]=keys[0];
        return encrypt(c,newKeys);
        }
```

## Conclusions

In this lab I understood that symmetric ciphers are hard as hell to implement and
thank God for libraries. This lab includes an implementation of the Rabbit stream cipher
and S-DES block cipher (DES was just way too complicated). I researched about
symmetric cryptography, stream and block ciphers.

Tests in `lab2/streamblockciphers/tests` can be run to verify ciphers.
Report is in `lab2` folder.