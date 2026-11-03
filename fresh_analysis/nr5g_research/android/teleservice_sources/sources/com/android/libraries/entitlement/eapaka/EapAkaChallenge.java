package com.android.libraries.entitlement.eapaka;

import android.util.Base64;
import android.util.Log;
import com.android.libraries.entitlement.ServiceEntitlementException;
import java.nio.charset.StandardCharsets;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class EapAkaChallenge {
    private byte[] mAutn;
    private byte mIdentifier = -1;
    private byte[] mRand;
    private String mSimAuthenticationRequest;

    private EapAkaChallenge() {
    }

    public static EapAkaChallenge parseEapAkaChallenge(String str) throws ServiceEntitlementException {
        try {
            byte[] bArrDecode = Base64.decode(str.getBytes(StandardCharsets.UTF_8), 0);
            EapAkaChallenge eapAkaChallenge = new EapAkaChallenge();
            if (eapAkaChallenge.parseEapAkaHeader(bArrDecode) && eapAkaChallenge.parseRandAndAutn(bArrDecode)) {
                eapAkaChallenge.mSimAuthenticationRequest = eapAkaChallenge.getSimAuthChallengeData();
                return eapAkaChallenge;
            }
            throw new ServiceEntitlementException(20, "EAP-AKA challenge message is not valid");
        } catch (IllegalArgumentException unused) {
            throw new ServiceEntitlementException(20, "EAP-AKA challenge is not a valid base64!");
        }
    }

    public String getSimAuthenticationRequest() {
        return this.mSimAuthenticationRequest;
    }

    public byte getIdentifier() {
        return this.mIdentifier;
    }

    private boolean parseEapAkaHeader(byte[] bArr) {
        if (bArr.length < 8) {
            return false;
        }
        byte b = bArr[0];
        this.mIdentifier = bArr[1];
        int i = ((bArr[2] & TransitionInfo.INIT) << 8) | (bArr[3] & TransitionInfo.INIT);
        byte b2 = bArr[4];
        byte b3 = bArr[5];
        if (b == 1 && i == bArr.length && b2 == 23 && b3 == 1) {
            return true;
        }
        Log.d("ServiceEntitlement", "Invalid EAP-AKA Header, code=" + ((int) b) + ", length=" + i + ", real length=" + bArr.length + ", type=" + ((int) b2) + ", subType=" + ((int) b3));
        return false;
    }

    private boolean parseRandAndAutn(byte[] bArr) {
        int i = 8;
        while (i < bArr.length) {
            int length = bArr.length - i;
            if (length <= 2) {
                Log.d("ServiceEntitlement", "Error! remainsLength = " + length);
                return false;
            }
            byte b = bArr[i];
            int i2 = (bArr[i + 1] & TransitionInfo.INIT) * 4;
            if (i2 > length) {
                Log.d("ServiceEntitlement", "Length Error! length is " + i2 + " but only remains " + length);
                return false;
            }
            if (b == 1) {
                if (i2 != 20) {
                    Log.d("ServiceEntitlement", "AT_RAND length is " + i2);
                    return false;
                }
                byte[] bArr2 = new byte[16];
                this.mRand = bArr2;
                System.arraycopy(bArr, i + 4, bArr2, 0, 16);
            } else if (b != 2) {
                continue;
            } else {
                if (i2 != 20) {
                    Log.d("ServiceEntitlement", "AT_AUTN length is " + i2);
                    return false;
                }
                byte[] bArr3 = new byte[16];
                this.mAutn = bArr3;
                System.arraycopy(bArr, i + 4, bArr3, 0, 16);
            }
            i += i2;
        }
        if (this.mRand != null && this.mAutn != null) {
            return true;
        }
        Log.d("ServiceEntitlement", "Invalid Type Datas!");
        return false;
    }

    private String getSimAuthChallengeData() {
        byte[] bArr = new byte[34];
        bArr[0] = 16;
        System.arraycopy(this.mRand, 0, bArr, 1, 16);
        bArr[17] = 16;
        System.arraycopy(this.mAutn, 0, bArr, 18, 16);
        return Base64.encodeToString(bArr, 2).trim();
    }
}
