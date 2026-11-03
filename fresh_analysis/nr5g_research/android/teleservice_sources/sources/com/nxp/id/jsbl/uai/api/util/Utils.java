package com.nxp.id.jsbl.uai.api.util;

import com.nxp.id.tp.common.HexString;
import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class Utils {
    private static final char[] HEX_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String integerToHexString(int i, int i2) {
        return String.format("%0" + i2 + "X", Integer.valueOf(i));
    }

    public static String integerToHexString(int i) {
        return String.format("%X", Integer.valueOf(i));
    }

    public static void checkParamNotNull(Object obj) {
        if (obj != null) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m("Parameter should not be null");
    }

    public static byte[] hexStringToByteArray(String str) {
        checkParamNotNull(str);
        String strReplace = str.toUpperCase().replace("0X", "");
        if (!strReplace.matches("([0-9A-F][0-9A-F])+")) {
            Folme$$ExternalSyntheticBUOutline0.m("Invalid hex string:".concat(strReplace));
            return null;
        }
        int length = strReplace.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(strReplace.charAt(i), 16) << 4) + Character.digit(strReplace.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static String toFormattedString(String str, int i) {
        if (str.length() > i) {
            i = str.length();
        }
        return String.format("%" + i + "." + i + "s", str);
    }

    public static String centerString(String str, int i, String str2) {
        if (str == null || i <= str.length()) {
            return str;
        }
        StringBuilder sb = new StringBuilder(i);
        for (int i2 = 0; i2 < (i - str.length()) / 2; i2++) {
            sb.append(str2);
        }
        sb.append(str);
        while (sb.length() < i) {
            sb.append(str2);
        }
        return sb.toString();
    }

    public static Integer hexStringToInteger(String str) {
        if (str.length() > 2 && (str.charAt(1) == 'X' || str.charAt(1) == 'x')) {
            return Integer.valueOf(Integer.parseInt(str.substring(2), 16));
        }
        return Integer.valueOf(Integer.parseInt(str, 16));
    }

    public static String byteToHexString(byte b) {
        char[] cArr = HEX_ARRAY;
        return new String(new char[]{cArr[(b & 240) >>> 4], cArr[b & 15]});
    }

    public static String byteArrayToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(byteToHexString(b));
        }
        return sb.toString().toUpperCase();
    }

    public static String calculateSHA256HexString(byte[] bArr) {
        return byteArrayToHexString(calculateSHA256ByteArray(bArr));
    }

    public static byte[] calculateSHA256ByteArray(byte[] bArr) {
        if (bArr == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("CryptoError: Input to generate hash is null or invalid.\n");
            return null;
        }
        byte[] byteArray = HexString.EMPTY.toByteArray();
        try {
            return MessageDigest.getInstance("SHA-256").digest(bArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return byteArray;
        }
    }

    public static byte[] calculateSHA384ByteArray(byte[] bArr) {
        if (bArr == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("CryptoError: Input to generate hash is null or invalid.\n");
            return null;
        }
        byte[] byteArray = HexString.EMPTY.toByteArray();
        try {
            return MessageDigest.getInstance("SHA-384").digest(bArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return byteArray;
        }
    }
}
