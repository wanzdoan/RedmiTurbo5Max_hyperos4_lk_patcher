package com.ot.pubsub.c;

import android.util.Base64;
import com.ot.pubsub.util.i;
import com.ot.pubsub.util.j;
import com.xiaomi.internal.telephony.DynamicSarService;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
public class a {
    public static final String a = b();
    public static final String b = c();
    private static final String c = "AES";
    private static final String d = "AES/ECB/PKCS5Padding";
    private static final String e = "AES";
    private static KeyGenerator f;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            f = keyGenerator;
            keyGenerator.init(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        } catch (Exception e2) {
            j.b("AES", "AesUtil e", e2);
        }
    }

    public static byte[] a() {
        return f.generateKey().getEncoded();
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
            Cipher cipher = Cipher.getInstance(d);
            cipher.init(1, secretKeySpec);
            return cipher.doFinal(bArr);
        } catch (Exception e2) {
            j.b("AES", "encrypt exception:", e2);
            return null;
        }
    }

    private static byte[] e(String str, String str2) {
        return a(str.getBytes(), str2.getBytes());
    }

    private static byte[] a(byte[] bArr, String str) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(b(str), "AES");
            Cipher cipher = Cipher.getInstance(d);
            cipher.init(2, secretKeySpec);
            return cipher.doFinal(bArr);
        } catch (Exception e2) {
            j.a("AES", "decrypt exception:", e2);
            return null;
        }
    }

    public static byte[] b(byte[] bArr, byte[] bArr2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
            Cipher cipher = Cipher.getInstance(d);
            cipher.init(2, secretKeySpec);
            return cipher.doFinal(bArr);
        } catch (Exception e2) {
            j.b("AES", "decrypt exception:", e2);
            j.b("AES", "content len=" + bArr.length + ", passwd len=" + bArr2.length);
            return null;
        }
    }

    private static byte[] b(String str) {
        if (str != null) {
            return str.getBytes();
        }
        return null;
    }

    private static byte[] c(String str) {
        if (str == null || str.length() < 1) {
            return null;
        }
        byte[] bArr = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            int i2 = i * 2;
            int i3 = i2 + 1;
            bArr[i] = (byte) ((Integer.parseInt(str.substring(i2, i3), 16) * 16) + Integer.parseInt(str.substring(i3, i2 + 2), 16));
        }
        return bArr;
    }

    public static String a(String str, String str2) {
        return i.a(e(str, str2));
    }

    public static String b(String str, String str2) {
        return Base64.encodeToString(e(str, str2), 10);
    }

    public static String c(String str, String str2) {
        return new String(a(c(str), str2));
    }

    public static String d(String str, String str2) {
        return new String(a(Base64.decode(str, 10), str2));
    }

    private static String b() {
        try {
            return new String("6AiSfshj3pD/9r91".getBytes(), "UTF-8");
        } catch (Exception e2) {
            j.b("AES", e2.getMessage());
            return "";
        }
    }

    private static String c() {
        try {
            return new String("h*Y31]c!@5Ns_jPsvej!@3[(*w(!l".getBytes(), "UTF-8");
        } catch (Exception e2) {
            j.b("AES", e2.getMessage());
            return "";
        }
    }

    public static String a(String str) {
        try {
            char[] charArray = (str + b.a).toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                for (int i2 = 0; i2 < charArray.length - 1; i2++) {
                    char c2 = charArray[i];
                    char c3 = charArray[i2];
                    if (c2 < c3) {
                        charArray[i] = c3;
                        charArray[i2] = c2;
                    }
                }
            }
            return d.h(new String(charArray));
        } catch (Exception unused) {
            j.b("AES", "encodeFromSalt ");
            return "";
        }
    }
}
