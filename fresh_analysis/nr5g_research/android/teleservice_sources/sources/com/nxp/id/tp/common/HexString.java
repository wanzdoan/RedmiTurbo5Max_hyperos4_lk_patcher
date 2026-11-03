package com.nxp.id.tp.common;

import com.xiaomi.internal.telephony.DynamicSarService;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class HexString implements Comparable, Cloneable, Iterable {
    public static final HexString EMPTY = new HexString("");
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private byte[] data;

    public static HexString fromString(String str) {
        if (str == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Input may not be null");
            return null;
        }
        if (str.startsWith("0x")) {
            str = str.substring(2);
        }
        String strReplace = str.replace(" ", "").replace("\t", "").replace("\r", "").replace("\n", "");
        for (char c : strReplace.toCharArray()) {
            if (!isHexChar(c)) {
                throw new NumberFormatException(strReplace + " is not a valid hex string, found '" + c + "'");
            }
        }
        if (strReplace.length() % 2 != 0) {
            strReplace = "0".concat(strReplace);
        }
        return new HexString(strReplace);
    }

    public static HexString fromAsciiString(String str) {
        return new HexString(str.getBytes());
    }

    public static HexString fromByteArray(byte[] bArr) {
        return fromByteArray(bArr, 0, bArr.length);
    }

    public static HexString fromByteArray(byte[] bArr, int i) {
        return fromByteArray(bArr, i, bArr.length);
    }

    public static HexString fromByteArray(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Input may not be null");
            return null;
        }
        if (i < 0 || (i != 0 && i > bArr.length)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid from index ", i);
            return null;
        }
        if (i2 < i || i2 < 0 || (i2 != 0 && i2 > bArr.length)) {
            HexString$$ExternalSyntheticBUOutline1.m("Invalid to index ", i2);
            return null;
        }
        int i3 = i2 - i;
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i, bArr2, 0, i3);
        return new HexString(bArr2);
    }

    public static HexString fromByte(byte b) {
        return new HexString(new byte[]{b});
    }

    public static HexString fromInteger(int i) {
        int iMax = Math.max(1, 4 - (Integer.numberOfLeadingZeros(i) >> 3));
        byte[] bArr = new byte[iMax];
        int i2 = (iMax - 1) * 8;
        int i3 = 0;
        while (i3 < iMax) {
            bArr[i3] = (byte) (i >> i2);
            i3++;
            i2 -= 8;
        }
        return new HexString(bArr);
    }

    public static HexString fromBigInteger(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length > 1 && byteArray[0] == 0) {
            byteArray = Arrays.copyOfRange(byteArray, 1, byteArray.length);
        }
        return new HexString(byteArray);
    }

    public static HexString fromFile(File file, boolean z) throws Throwable {
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                HexString hexStringFromInputStream = fromInputStream(fileInputStream2, z);
                fileInputStream2.close();
                return hexStringFromInputStream;
            } catch (Throwable th) {
                th = th;
                fileInputStream = fileInputStream2;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static HexString fromInputStream(InputStream inputStream, boolean z) throws IOException {
        if (z) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(inputStream.available());
            byte[] bArr = new byte[4096];
            while (true) {
                int i = inputStream.read(bArr);
                if (i >= 0) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    return new HexString(byteArrayOutputStream.toByteArray());
                }
            }
        } else {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line.trim());
                } else {
                    return fromString(sb.toString());
                }
            }
        }
    }

    public static HexString zero(int i) {
        if (i < 0) {
            Folme$$ExternalSyntheticBUOutline0.m("Bit length may not be negative");
            return null;
        }
        if (i == 0) {
            return EMPTY;
        }
        return new HexString(new byte[i]);
    }

    protected HexString(String str) {
        int length = str.length();
        this.data = new byte[length >> 1];
        for (int i = 0; i < length; i += 2) {
            this.data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
    }

    protected HexString(byte[] bArr) {
        this.data = bArr;
    }

    public final byte[] toByteArray() {
        return toByteArray(0, this.data.length);
    }

    public final byte[] toByteArray(int i, int i2) {
        int i3;
        if (i >= 0) {
            byte[] bArr = this.data;
            if (i <= bArr.length) {
                if (i2 < 0 || (i3 = i2 + i) > bArr.length) {
                    Folme$$ExternalSyntheticBUOutline0.m("Length out of bounds");
                    return null;
                }
                return Arrays.copyOfRange(bArr, i, i3);
            }
        }
        Folme$$ExternalSyntheticBUOutline0.m("Offset out of bounds");
        return null;
    }

    public final int toInteger() {
        byte[] bArr = this.data;
        if (bArr.length <= 4) {
            int i = 0;
            if (bArr.length != 4 || bArr[0] <= 127) {
                int length = bArr.length - 1;
                int i2 = 0;
                while (true) {
                    byte[] bArr2 = this.data;
                    if (i >= bArr2.length) {
                        return i2;
                    }
                    i2 |= (bArr2[i] & TransitionInfo.INIT) << (length << 3);
                    i++;
                    length--;
                }
            }
        }
        throw new NumberFormatException("Cannot convert to integer, input too large");
    }

    public final long toLong() {
        byte[] bArr = this.data;
        if (bArr.length <= 8) {
            int i = 0;
            if (bArr.length != 8 || bArr[0] <= 127) {
                int length = bArr.length - 1;
                long j = 0;
                while (true) {
                    byte[] bArr2 = this.data;
                    if (i >= bArr2.length) {
                        return j;
                    }
                    j |= ((long) (bArr2[i] & TransitionInfo.INIT)) << (length << 3);
                    i++;
                    length--;
                }
            }
        }
        throw new NumberFormatException("Cannot convert to integer, input too large");
    }

    public final BigInteger toBigInteger() {
        return new BigInteger(1, this.data);
    }

    public final String toString() {
        return toString(0, this.data.length);
    }

    public final String toString(int i, int i2) {
        if (i >= 0) {
            byte[] bArr = this.data;
            if (i <= bArr.length) {
                if (i2 < 0 || i + i2 > bArr.length) {
                    Folme$$ExternalSyntheticBUOutline0.m("Length out of bounds");
                    return null;
                }
                char[] cArr = new char[i2 << 1];
                int i3 = 0;
                while (i3 < i2) {
                    byte b = this.data[i];
                    int i4 = b & TransitionInfo.INIT;
                    int i5 = i3 << 1;
                    char[] cArr2 = HEX_CHARS;
                    cArr[i5] = cArr2[i4 >>> 4];
                    cArr[i5 + 1] = cArr2[b & 15];
                    i3++;
                    i++;
                }
                return new String(cArr);
            }
        }
        Folme$$ExternalSyntheticBUOutline0.m("Offset out of bounds");
        return null;
    }

    public final String toAsciiString() {
        return new String(this.data);
    }

    public final int length() {
        return this.data.length;
    }

    public final byte at(int i) {
        if (i >= 0) {
            byte[] bArr = this.data;
            if (i < bArr.length) {
                return bArr[i];
            }
        }
        HexString$$ExternalSyntheticBUOutline0.m(i);
        return (byte) 0;
    }

    public final HexString substring(int i) {
        if (i >= 0) {
            byte[] bArr = this.data;
            if (i <= bArr.length) {
                int length = bArr.length - i;
                byte[] bArr2 = new byte[length];
                System.arraycopy(bArr, i, bArr2, 0, length);
                return new HexString(bArr2);
            }
        }
        HexString$$ExternalSyntheticBUOutline0.m(i);
        return null;
    }

    public final HexString substring(int i, int i2) {
        if (i >= 0) {
            byte[] bArr = this.data;
            if (i <= bArr.length) {
                if (i2 < 0 || i2 > bArr.length) {
                    HexString$$ExternalSyntheticBUOutline0.m(i2);
                    return null;
                }
                if (i2 < i) {
                    Folme$$ExternalSyntheticBUOutline0.m("To-Index cannot be smaller than 'From-Index");
                    return null;
                }
                int i3 = i2 - i;
                byte[] bArr2 = new byte[i3];
                System.arraycopy(bArr, i, bArr2, 0, i3);
                return new HexString(bArr2);
            }
        }
        HexString$$ExternalSyntheticBUOutline0.m(i);
        return null;
    }

    public HexString replaceAt(int i, HexString hexString) {
        if (i > length()) {
            Folme$$ExternalSyntheticBUOutline0.m("Index out of bounds");
            return null;
        }
        HexString hexStringSubstring = EMPTY;
        HexString hexStringSubstring2 = i != 0 ? substring(0, i) : hexStringSubstring;
        if (hexString.length() + i < length()) {
            hexStringSubstring = substring(i + hexString.length(), length());
        }
        return hexString.prepend(hexStringSubstring2).append(hexStringSubstring);
    }

    public final boolean startsWith(HexString hexString) {
        if (hexString == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Sequence may not be null");
            return false;
        }
        if (hexString.data.length > this.data.length) {
            return false;
        }
        int i = 0;
        while (true) {
            byte[] bArr = hexString.data;
            if (i >= bArr.length) {
                return true;
            }
            if (this.data[i] != bArr[i]) {
                return false;
            }
            i++;
        }
    }

    public final HexString prepend(byte[] bArr) {
        if (bArr == null) {
            Folme$$ExternalSyntheticBUOutline0.m("toPrepend may not be null");
            return null;
        }
        byte[] bArr2 = new byte[this.data.length + bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        byte[] bArr3 = this.data;
        System.arraycopy(bArr3, 0, bArr2, bArr.length, bArr3.length);
        return new HexString(bArr2);
    }

    public final HexString prepend(HexString hexString) {
        if (hexString == null) {
            Folme$$ExternalSyntheticBUOutline0.m("toPrepend may not be null");
            return null;
        }
        return prepend(hexString.data);
    }

    public final HexString append(byte[] bArr) {
        if (bArr == null) {
            Folme$$ExternalSyntheticBUOutline0.m("toAppend may not be null");
            return null;
        }
        byte[] bArr2 = this.data;
        byte[] bArr3 = new byte[bArr2.length + bArr.length];
        System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
        System.arraycopy(bArr, 0, bArr3, this.data.length, bArr.length);
        return new HexString(bArr3);
    }

    public final HexString append(HexString hexString) {
        if (hexString == null) {
            Folme$$ExternalSyntheticBUOutline0.m("toAppend may not be null");
            return null;
        }
        return append(hexString.data);
    }

    public final boolean isEmpty() {
        return this.data.length == 0;
    }

    public final HexString padToBlockSize(boolean z, byte b, int i) {
        byte[] bArr = this.data;
        int length = bArr.length + (i - (bArr.length % i));
        byte[] bArr2 = new byte[length];
        if (z) {
            System.arraycopy(bArr, 0, bArr2, length - bArr.length, bArr.length);
            Arrays.fill(bArr2, 0, length - this.data.length, b);
        } else {
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            Arrays.fill(bArr2, this.data.length, length, b);
        }
        return new HexString(bArr2);
    }

    public final boolean testBit(int i) {
        if (i >= 0) {
            byte[] bArr = this.data;
            if (i < bArr.length * 8) {
                return ((bArr[i >> 3] & TransitionInfo.INIT) & (DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY >> (i % 8))) > 0;
            }
        }
        Folme$$ExternalSyntheticBUOutline0.m("Invalid index");
        return false;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof HexString) {
            return Arrays.equals(this.data, ((HexString) obj).data);
        }
        if (obj instanceof String) {
            return Arrays.equals(this.data, fromString((String) obj).data);
        }
        if (obj instanceof byte[]) {
            return Arrays.equals(this.data, (byte[]) obj);
        }
        if (obj instanceof BigInteger) {
            return toBigInteger().equals(obj);
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).equals(Integer.valueOf(toInteger()));
        }
        if (obj instanceof Long) {
            return ((Long) obj).equals(Long.valueOf(toLong()));
        }
        return false;
    }

    public final int hashCode() {
        return Arrays.hashCode(this.data);
    }

    @Override // java.lang.Comparable
    public final int compareTo(HexString hexString) {
        if (hexString == null) {
            return 1;
        }
        int i = 0;
        while (true) {
            int iMin = Math.min(this.data.length, hexString.data.length);
            byte[] bArr = this.data;
            if (i < iMin) {
                byte b = bArr[i];
                byte b2 = hexString.data[i];
                if (b != b2) {
                    return b - b2;
                }
                i++;
            } else {
                return bArr.length - hexString.data.length;
            }
        }
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public final HexString m4777clone() {
        byte[] bArr = this.data;
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, 0, bArr2, 0, length);
        return new HexString(bArr2);
    }

    protected static final boolean isHexChar(char c) {
        return Character.digit(c, 16) != -1;
    }

    @Override // java.lang.Iterable
    public Iterator iterator() {
        return new Iterator() { // from class: com.nxp.id.tp.common.HexString.1
            private int i = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.i < HexString.this.data.length;
            }

            @Override // java.util.Iterator
            public Byte next() {
                byte[] bArr = HexString.this.data;
                int i = this.i;
                this.i = i + 1;
                return Byte.valueOf(bArr[i]);
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("HexString is mutable, remove not supported.");
            }
        };
    }
}
