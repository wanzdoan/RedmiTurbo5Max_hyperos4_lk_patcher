package com.android.phone.callcomposer;

import android.text.TextUtils;
import android.util.Log;
import androidx.recyclerview.widget.ChildHelper$$ExternalSyntheticBUOutline0;
import com.google.common.io.BaseEncoding;
import gov.nist.javax.sip.address.GenericURI;
import gov.nist.javax.sip.header.Authorization;
import gov.nist.javax.sip.header.WWWAuthenticate;
import gov.nist.javax.sip.parser.WWWAuthenticateParser;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class DigestAuthUtils {
    private static final String AUTH_QOP = "auth";
    private static final int CNONCE_LENGTH_BYTES = 16;
    private static final String MD5_ALGORITHM = "md5";
    private static final String TAG = "DigestAuthUtils";
    public static final String WWW_AUTHENTICATE = "www-authenticate";

    public static WWWAuthenticate parseAuthenticateHeader(String str) {
        try {
            return (WWWAuthenticate) new WWWAuthenticateParser("www-authenticate: " + str).parse();
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing received auth header: " + e);
            return null;
        }
    }

    public static String generateAuthorizationHeader(WWWAuthenticate wWWAuthenticate, GbaCredentials gbaCredentials, String str, String str2) {
        if (!TextUtils.isEmpty(wWWAuthenticate.getAlgorithm()) && !MD5_ALGORITHM.equals(wWWAuthenticate.getAlgorithm().toLowerCase(Locale.ROOT))) {
            Log.e(TAG, "This client only supports MD5 auth");
            return "";
        }
        if (!TextUtils.isEmpty(wWWAuthenticate.getQop()) && !AUTH_QOP.equals(wWWAuthenticate.getQop().toLowerCase(Locale.ROOT))) {
            Log.e(TAG, "This client only supports the auth qop");
            return "";
        }
        String strMakeClientNonce = makeClientNonce();
        String strComputeResponse = computeResponse(wWWAuthenticate.getNonce(), strMakeClientNonce, AUTH_QOP, gbaCredentials.getTransactionId(), wWWAuthenticate.getRealm(), gbaCredentials.getKey(), str, str2);
        Authorization authorization = new Authorization();
        try {
            authorization.setScheme(wWWAuthenticate.getScheme());
            authorization.setUsername(gbaCredentials.getTransactionId());
            authorization.setURI(new WorkaroundURI(str2));
            authorization.setRealm(wWWAuthenticate.getRealm());
            authorization.setQop(AUTH_QOP);
            authorization.setNonce(wWWAuthenticate.getNonce());
            authorization.setCNonce(strMakeClientNonce);
            authorization.setNonceCount(1);
            authorization.setResponse(strComputeResponse);
            authorization.setOpaque(wWWAuthenticate.getOpaque());
            authorization.setAlgorithm(wWWAuthenticate.getAlgorithm());
            return authorization.encodeBody();
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing while constructing reply header: " + e);
            return null;
        }
    }

    public static String computeResponse(String str, String str2, String str3, String str4, String str5, byte[] bArr, String str6, String str7) {
        String strGenerateA1Hash = generateA1Hash(str4, str5, bArr);
        String strGenerateA2Hash = generateA2Hash(str6, str7);
        MessageDigest md5Digest = getMd5Digest();
        md5Digest.update(String.join(":", strGenerateA1Hash, str, "00000001", str2, str3, strGenerateA2Hash).getBytes());
        return base16(md5Digest.digest());
    }

    private static String makeClientNonce() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        return base16(bArr);
    }

    private static String generateA1Hash(String str, String str2, byte[] bArr) {
        MessageDigest md5Digest = getMd5Digest();
        md5Digest.update(String.join(":", str, str2, BaseEncoding.base64().encode(bArr)).getBytes());
        return base16(md5Digest.digest());
    }

    private static String generateA2Hash(String str, String str2) {
        MessageDigest md5Digest = getMd5Digest();
        md5Digest.update(String.join(":", str, str2).getBytes());
        return base16(md5Digest.digest());
    }

    private static String base16(byte[] bArr) {
        return BaseEncoding.base16().encode(bArr).toLowerCase(Locale.ROOT);
    }

    private static MessageDigest getMd5Digest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            ChildHelper$$ExternalSyntheticBUOutline0.m("Couldn't find MD5 algorithm: ", e);
            return null;
        }
    }

    class WorkaroundURI extends GenericURI {
        public WorkaroundURI(String str) {
            this.uriString = str;
            this.scheme = "";
        }
    }
}
