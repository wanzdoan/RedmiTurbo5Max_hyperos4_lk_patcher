package com.android.libraries.entitlement.eapaka;

import android.util.Base64;
import android.util.Log;
import com.android.libraries.entitlement.ServiceEntitlementException;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
class EapAkaSecurityContext {
    private byte[] mAuts;
    private byte[] mCk;
    private byte[] mIk;
    private byte[] mRes;
    private boolean mValid;

    private EapAkaSecurityContext() {
    }

    public static EapAkaSecurityContext from(String str) throws ServiceEntitlementException {
        EapAkaSecurityContext eapAkaSecurityContext = new EapAkaSecurityContext();
        eapAkaSecurityContext.parseResponseData(str);
        if (eapAkaSecurityContext.isValid()) {
            return eapAkaSecurityContext;
        }
        throw new ServiceEntitlementException(20, "Invalid SIM EAP-AKA authentication response!");
    }

    private void parseResponseData(String str) {
        try {
            byte[] bArrDecode = Base64.decode(str.getBytes(StandardCharsets.UTF_8), 0);
            Log.d("ServiceEntitlement", "Decoded response data length = " + bArrDecode.length + " bytes");
            if (bArrDecode.length == 0) {
                return;
            }
            byte b = bArrDecode[0];
            if (b != -37) {
                if (b == -36) {
                    byte[] tag = parseTag(1, bArrDecode);
                    this.mAuts = tag;
                    if (tag == null) {
                        Log.d("ServiceEntitlement", "Invalid data: can't parse AUTS!");
                        return;
                    } else {
                        this.mValid = true;
                        return;
                    }
                }
                Log.d("ServiceEntitlement", "Not a valid tag, tag=" + ((int) bArrDecode[0]));
                return;
            }
            byte[] tag2 = parseTag(1, bArrDecode);
            this.mRes = tag2;
            if (tag2 == null) {
                Log.d("ServiceEntitlement", "Invalid data: can't parse RES!");
                return;
            }
            int length = tag2.length + 2;
            byte[] tag3 = parseTag(length, bArrDecode);
            this.mCk = tag3;
            if (tag3 == null) {
                Log.d("ServiceEntitlement", "Invalid data: can't parse CK!");
                return;
            }
            byte[] tag4 = parseTag(length + tag3.length + 1, bArrDecode);
            this.mIk = tag4;
            if (tag4 == null) {
                Log.d("ServiceEntitlement", "Invalid data: can't parse IK!");
            } else {
                this.mValid = true;
            }
        } catch (IllegalArgumentException unused) {
            Log.e("ServiceEntitlement", "Response is not a valid base-64 content");
        }
    }

    private byte[] parseTag(int i, byte[] bArr) {
        if (i >= bArr.length) {
            Log.d("ServiceEntitlement", "No length byte!");
            return null;
        }
        int i2 = bArr[i] & 255;
        if (i + i2 >= bArr.length) {
            Log.d("ServiceEntitlement", "Invalid data length!");
            return null;
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i + 1, bArr2, 0, i2);
        return bArr2;
    }

    private boolean isValid() {
        return this.mValid;
    }

    public byte[] getRes() {
        return this.mRes;
    }

    public byte[] getCk() {
        return this.mCk;
    }

    public byte[] getIk() {
        return this.mIk;
    }

    public byte[] getAuts() {
        return this.mAuts;
    }
}
