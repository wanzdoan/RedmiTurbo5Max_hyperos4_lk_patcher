package com.xiaomi.onetrack.d;

import com.xiaomi.onetrack.util.r;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/* JADX INFO: loaded from: classes2.dex */
public abstract class e {
    public static byte[] a(byte[] bArr) {
        try {
            RSAPublicKey rSAPublicKeyA = a(com.ot.pubsub.c.e.a);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(1, rSAPublicKeyA);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            r.b(r.a("RsaUtils"), "RsaUtils encrypt exception:", e);
            return null;
        }
    }

    private static RSAPublicKey a(String str) {
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(c.a(str)));
    }
}
