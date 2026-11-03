package com.android.internal.telephony.ntnphone;

import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.uicc.IccConstants;

/* JADX INFO: loaded from: classes.dex */
public final class ByteArrayConverter {
    public static String byteArrayToPathString(byte[] bArr) throws IllegalArgumentException {
        String strConcat = "";
        if (bArr.length % 2 != 0) {
            return "";
        }
        byte[] bArr2 = new byte[2];
        for (int i = 0; i < bArr.length; i += 2) {
            System.arraycopy(bArr, i, bArr2, 0, 2);
            String strByteArrayToHexString = byteArrayToHexString(bArr2);
            if (!strByteArrayToHexString.equalsIgnoreCase(IccConstants.MF_SIM)) {
                strConcat = strConcat.concat(strByteArrayToHexString);
                if (i != bArr.length - 2) {
                    strConcat = strConcat.concat(":");
                }
            }
        }
        return strConcat;
    }

    public static String byteArrayToHexString(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < i2; i3++) {
            sb.append(String.format("%02x", Integer.valueOf(bArr[i + i3] & 255)));
        }
        return sb.toString();
    }

    public static String byteArrayToHexString(byte[] bArr, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        while (i < bArr.length) {
            stringBuffer.append(Integer.toHexString((bArr[i] & 255) + CallFailCause.RADIO_UPLINK_FAILURE).substring(1));
            i++;
        }
        return stringBuffer.toString();
    }

    public static String byteArrayToHexString(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        return byteArrayToHexString(bArr, 0, bArr.length);
    }

    public static byte[] hexStringToByteArray(String str, int i, int i2) {
        if (i2 % 2 != 0) {
            return null;
        }
        String upperCase = str.toUpperCase();
        byte[] bArr = new byte[upperCase.length() / 2];
        for (int i3 = 0; i3 < i2; i3 += 2) {
            char cCharAt = upperCase.charAt(i3 + i);
            char cCharAt2 = upperCase.charAt(i3 + 1 + i);
            if (!isHexChar(cCharAt) || !isHexChar(cCharAt2)) {
                return null;
            }
            bArr[i3 / 2] = (byte) ((Character.digit(cCharAt, 16) << 4) + Character.digit(cCharAt2, 16));
        }
        return bArr;
    }

    public static byte[] hexStringToByteArray(String str) {
        return hexStringToByteArray(str, 0, str.length());
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) (i >>> 24), (byte) (i >>> 16), (byte) (i >>> 8), (byte) i};
    }

    public static int byteArrayToInt(byte[] bArr) {
        int length = bArr.length;
        if (length == 0) {
            return 0;
        }
        if (length == 1) {
            return bArr[0] & 255;
        }
        if (length == 2) {
            return (bArr[1] & 255) | ((bArr[0] & 255) << 8);
        }
        if (length == 3) {
            return (bArr[2] & 255) | ((bArr[0] & 255) << 16) | ((bArr[1] & 255) << 8);
        }
        return (bArr[3] & 255) | ((bArr[0] & 255) << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8);
    }

    public static boolean isHexChar(char c) {
        if (Character.isLowerCase(c)) {
            c = Character.toUpperCase(c);
        }
        if (c < '0' || c > '9') {
            return c >= 'A' && c <= 'F';
        }
        return true;
    }
}
