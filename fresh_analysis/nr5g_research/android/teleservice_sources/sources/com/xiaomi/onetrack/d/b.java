package com.xiaomi.onetrack.d;

import android.content.Context;
import android.util.Base64;
import com.xiaomi.onetrack.util.r;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;

/* JADX INFO: loaded from: classes2.dex */
public abstract class b {
    public static final String a = b();

    public static synchronized String a(Context context, String str) {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        a(context, keyStore);
        Certificate certificate = keyStore.getCertificate("RSA_KEY");
        if (certificate == null) {
            return null;
        }
        cipher.init(1, certificate.getPublicKey());
        return Base64.encodeToString(cipher.doFinal(str.getBytes("UTF-8")), 0);
    }

    public static synchronized String b(Context context, String str) {
        Cipher cipher;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        a(context, keyStore);
        cipher.init(2, (PrivateKey) keyStore.getKey("RSA_KEY", null));
        return new String(cipher.doFinal(Base64.decode(str, 0)), "UTF-8");
    }

    private static void a(Context context, KeyStore keyStore) {
        try {
            if (keyStore.containsAlias("RSA_KEY")) {
                return;
            }
            a();
        } catch (Throwable th) {
            r.b("AndroidKeyStore", "createKey e", th);
        }
    }

    private static void a() throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        Class<?> cls = Class.forName("android.security.keystore.KeyGenParameterSpec$Builder");
        Constructor<?> constructor = cls.getConstructor(String.class, Integer.TYPE);
        Class<?> cls2 = Class.forName("android.security.keystore.KeyProperties");
        Object objNewInstance = constructor.newInstance("RSA_KEY", Integer.valueOf(cls2.getDeclaredField("PURPOSE_ENCRYPT").getInt(null) | cls2.getDeclaredField("PURPOSE_DECRYPT").getInt(null)));
        cls.getMethod("setDigests", String[].class).invoke(objNewInstance, new String[]{(String) cls2.getDeclaredField("DIGEST_SHA256").get(null), (String) cls2.getDeclaredField("DIGEST_SHA512").get(null)});
        cls.getMethod("setEncryptionPaddings", String[].class).invoke(objNewInstance, new String[]{(String) cls2.getDeclaredField("ENCRYPTION_PADDING_RSA_PKCS1").get(null)});
        Class[] clsArr = new Class[0];
        Object objInvoke = cls.getMethod("build", null).invoke(objNewInstance, null);
        Class<?> cls3 = Class.forName("java.security.KeyPairGenerator");
        KeyPairGenerator keyPairGenerator = (KeyPairGenerator) cls3.getMethod("getInstance", String.class, String.class).invoke(null, "RSA", "AndroidKeyStore");
        cls3.getMethod("initialize", AlgorithmParameterSpec.class).invoke(keyPairGenerator, objInvoke);
        keyPairGenerator.generateKeyPair();
    }

    private static String b() {
        try {
            return new String("h*Y31]c!@5Ns_jPsvej!@3[(*w(!l".getBytes(), "UTF-8");
        } catch (Exception e) {
            r.b("AndroidKeyStore", e.getMessage());
            return "";
        }
    }
}
