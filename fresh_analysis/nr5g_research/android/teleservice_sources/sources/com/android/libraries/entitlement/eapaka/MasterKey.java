package com.android.libraries.entitlement.eapaka;

import android.text.TextUtils;
import android.util.Log;
import com.android.libraries.entitlement.utils.BytesConverter;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
class MasterKey {
    private byte[] mAut;
    private byte[] mEmsk;
    private byte[] mEncr;
    private byte[] mMasterKey;
    private byte[] mMsk;

    private static int doFunctionK(int i) {
        if (i >= 0 && i <= 19) {
            return 1518500249;
        }
        if (20 <= i && i <= 39) {
            return 1859775393;
        }
        if (40 > i || i > 59) {
            return (60 > i || i > 79) ? -1 : -899497514;
        }
        return -1894007588;
    }

    private MasterKey() {
    }

    public static MasterKey create(String str, byte[] bArr, byte[] bArr2) {
        if (TextUtils.isEmpty(str) || bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
            Log.d("ServiceEntitlement", "Can't create master key due to invalid input!");
            return null;
        }
        MasterKey masterKey = new MasterKey();
        masterKey.from(str, bArr, bArr2);
        return masterKey;
    }

    void from(String str, byte[] bArr, byte[] bArr2) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] bArr3 = new byte[bytes.length + bArr.length + bArr2.length];
        System.arraycopy(bytes, 0, bArr3, 0, bytes.length);
        int length = bytes.length;
        System.arraycopy(bArr, 0, bArr3, length, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, length + bArr.length, bArr2.length);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(bArr3);
            this.mMasterKey = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.d("ServiceEntitlement", "process SHA-1 failed", e);
        }
        generateTransientEapKeys();
    }

    void generateTransientEapKeys() {
        byte[] bArrGeneratePsudoRandomNumber = generatePsudoRandomNumber();
        if (bArrGeneratePsudoRandomNumber == null || bArrGeneratePsudoRandomNumber.length != 160) {
            Log.e("ServiceEntitlement", "Invalid TEKs data!");
            return;
        }
        byte[] bArr = new byte[16];
        this.mEncr = bArr;
        System.arraycopy(bArrGeneratePsudoRandomNumber, 0, bArr, 0, 16);
        byte[] bArr2 = new byte[16];
        this.mAut = bArr2;
        System.arraycopy(bArrGeneratePsudoRandomNumber, 16, bArr2, 0, 16);
        byte[] bArr3 = new byte[64];
        this.mMsk = bArr3;
        System.arraycopy(bArrGeneratePsudoRandomNumber, 32, bArr3, 0, 64);
        byte[] bArr4 = new byte[64];
        this.mEmsk = bArr4;
        System.arraycopy(bArrGeneratePsudoRandomNumber, 96, bArr4, 0, 64);
    }

    public byte[] getAut() {
        return this.mAut;
    }

    private byte[] generatePsudoRandomNumber() {
        byte[] bArr = this.mMasterKey;
        if (bArr == null || bArr.length != 20) {
            StringBuilder sb = new StringBuilder("Not a valid XKey!length=");
            sb.append(bArr == null ? "null" : Integer.valueOf(bArr.length));
            Log.e("ServiceEntitlement", sb.toString());
            return null;
        }
        int[] iArr = {1732584193, -271733879, -1732584194, 271733878, -1009589776};
        byte[] bArr2 = new byte[160];
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            for (int i3 = 0; i3 < 2; i3++) {
                byte[] bArrDoFunctionG = doFunctionG(iArr, bArr);
                if (bArrDoFunctionG == null || bArrDoFunctionG.length != 20) {
                    Log.e("ServiceEntitlement", "Get invalid w value from G function!");
                    return null;
                }
                System.arraycopy(bArrDoFunctionG, 0, bArr2, i, 20);
                i += 20;
                int i4 = 1;
                for (int i5 = 19; i5 >= 0; i5--) {
                    int i6 = i4 + (bArr[i5] & TransitionInfo.INIT) + (bArrDoFunctionG[i5] & TransitionInfo.INIT);
                    bArr[i5] = (byte) (i6 & 255);
                    i4 = i6 >> 8;
                }
            }
        }
        return bArr2;
    }

    private byte[] doFunctionG(int[] iArr, byte[] bArr) {
        boolean z = true;
        int i = 5;
        byte[][] bArr2 = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, 5, 4);
        for (int i2 = 0; i2 < 5; i2++) {
            System.arraycopy(BytesConverter.convertIntegerTo4Bytes(iArr[i2]), 0, bArr2[i2], 0, 4);
        }
        byte[] bArr3 = new byte[64];
        System.arraycopy(bArr, 0, bArr3, 0, 20);
        for (int i3 = 20; i3 < 64; i3++) {
            bArr3[i3] = 0;
        }
        int i4 = 80;
        byte[][] bArr4 = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, 80, 4);
        for (int i5 = 0; i5 < 16; i5++) {
            System.arraycopy(bArr3, i5 * 4, bArr4[i5], 0, 4);
        }
        for (int i6 = 16; i6 < 80; i6++) {
            bArr4[i6] = doFunctionS(1, doXor(bArr4[i6 - 3], bArr4[i6 - 8], bArr4[i6 - 14], bArr4[i6 - 16]));
        }
        byte[] bArrConvertIntegerTo4Bytes = new byte[4];
        byte[] bArr5 = new byte[4];
        byte[] bArr6 = new byte[4];
        byte[] bArr7 = new byte[4];
        byte[] bArr8 = new byte[4];
        System.arraycopy(bArr2[0], 0, bArrConvertIntegerTo4Bytes, 0, 4);
        System.arraycopy(bArr2[1], 0, bArr5, 0, 4);
        System.arraycopy(bArr2[2], 0, bArr6, 0, 4);
        char c = 3;
        System.arraycopy(bArr2[3], 0, bArr7, 0, 4);
        System.arraycopy(bArr2[4], 0, bArr8, 0, 4);
        int i7 = 0;
        while (i7 < i4) {
            boolean z2 = z;
            int iIntValue = new BigInteger(doFunctionS(i, bArrConvertIntegerTo4Bytes)).intValue() + doFunctionF(i7, bArr5, bArr6, bArr7) + new BigInteger(bArr8).intValue() + new BigInteger(bArr4[i7]).intValue() + doFunctionK(i7);
            byte[] bArrDoFunctionS = doFunctionS(30, bArr5);
            i7++;
            c = c;
            bArr5 = bArrConvertIntegerTo4Bytes;
            bArr8 = bArr7;
            i4 = 80;
            bArrConvertIntegerTo4Bytes = BytesConverter.convertIntegerTo4Bytes(iIntValue);
            bArr7 = bArr6;
            z = z2;
            bArr6 = bArrDoFunctionS;
            i = 5;
        }
        boolean z3 = z;
        char c2 = c;
        bArr2[0] = addTwoBytes(bArr2[0], bArrConvertIntegerTo4Bytes);
        bArr2[z3 ? 1 : 0] = addTwoBytes(bArr2[z3 ? 1 : 0], bArr5);
        bArr2[2] = addTwoBytes(bArr2[2], bArr6);
        bArr2[c2] = addTwoBytes(bArr2[c2], bArr7);
        bArr2[4] = addTwoBytes(bArr2[4], bArr8);
        byte[] bArr9 = new byte[20];
        System.arraycopy(bArr2[0], 0, bArr9, 0, 4);
        System.arraycopy(bArr2[z3 ? 1 : 0], 0, bArr9, 4, 4);
        System.arraycopy(bArr2[2], 0, bArr9, 8, 4);
        System.arraycopy(bArr2[c2], 0, bArr9, 12, 4);
        System.arraycopy(bArr2[4], 0, bArr9, 16, 4);
        return bArr9;
    }

    private static byte[] addTwoBytes(byte[] bArr, byte[] bArr2) {
        return BytesConverter.convertIntegerTo4Bytes(new BigInteger(bArr).add(new BigInteger(bArr2)).intValue());
    }

    private static byte[] doFunctionS(int i, byte[] bArr) {
        return BytesConverter.convertIntegerTo4Bytes(new BigInteger(bArr).shiftLeft(i).or(BigInteger.valueOf(new BigInteger(bArr).intValue() >>> (32 - i))).intValue());
    }

    private static byte[] doXor(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        BigInteger bigInteger = new BigInteger(bArr);
        BigInteger bigInteger2 = new BigInteger(bArr2);
        BigInteger bigInteger3 = new BigInteger(bArr3);
        return BytesConverter.convertIntegerTo4Bytes(bigInteger.xor(bigInteger2).xor(bigInteger3).xor(new BigInteger(bArr4)).intValue());
    }

    private static int doFunctionF(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        BigInteger bigInteger = new BigInteger(bArr);
        BigInteger bigInteger2 = new BigInteger(bArr2);
        BigInteger bigInteger3 = new BigInteger(bArr3);
        BigInteger bigIntegerValueOf = BigInteger.valueOf(-1L);
        if (i >= 0 && i <= 19) {
            bigIntegerValueOf = bigInteger.and(bigInteger2).or(bigInteger.not().and(bigInteger3));
        } else if (20 <= i && i <= 39) {
            bigIntegerValueOf = bigInteger.xor(bigInteger2).xor(bigInteger3);
        } else if (40 <= i && i <= 59) {
            bigIntegerValueOf = bigInteger.and(bigInteger2).or(bigInteger.and(bigInteger3)).or(bigInteger2.and(bigInteger3));
        } else if (60 <= i && i <= 79) {
            bigIntegerValueOf = bigInteger.xor(bigInteger2).xor(bigInteger3);
        }
        return bigIntegerValueOf.intValue();
    }
}
