package com.android.libraries.entitlement.eapaka;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import com.android.libraries.entitlement.ServiceEntitlementException;
import com.android.libraries.entitlement.utils.BytesConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
public class EapAkaResponse {
    private String mResponse;
    private String mSynchronizationFailureResponse;

    private EapAkaResponse() {
    }

    public String response() {
        return this.mResponse;
    }

    public String synchronizationFailureResponse() {
        return this.mSynchronizationFailureResponse;
    }

    public static EapAkaResponse respondToEapAkaChallenge(Context context, int i, EapAkaChallenge eapAkaChallenge, String str) throws ServiceEntitlementException {
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
        try {
            String iccAuthentication = telephonyManagerCreateForSubscriptionId.getIccAuthentication(2, 129, eapAkaChallenge.getSimAuthenticationRequest());
            if (iccAuthentication == null) {
                throw new ServiceEntitlementException(20, "EAP-AKA response is null!");
            }
            EapAkaSecurityContext eapAkaSecurityContextFrom = EapAkaSecurityContext.from(iccAuthentication);
            EapAkaResponse eapAkaResponse = new EapAkaResponse();
            byte[] res = eapAkaSecurityContextFrom.getRes();
            byte[] ik = eapAkaSecurityContextFrom.getIk();
            byte[] ck = eapAkaSecurityContextFrom.getCk();
            byte[] auts = eapAkaSecurityContextFrom.getAuts();
            if (res == null || ik == null || ck == null) {
                if (auts != null) {
                    eapAkaResponse.mSynchronizationFailureResponse = Base64.encodeToString(generateEapAkaSynchronizationFailureResponse(auts, eapAkaChallenge.getIdentifier()), 2).trim();
                    return eapAkaResponse;
                }
                throw new ServiceEntitlementException(20, "Invalid SIM EAP-AKA authentication response!");
            }
            MasterKey masterKeyCreate = MasterKey.create(EapAkaApi.getImsiEap(telephonyManagerCreateForSubscriptionId.getSimOperator(), telephonyManagerCreateForSubscriptionId.getSubscriberId(), str), ik, ck);
            if (masterKeyCreate == null || masterKeyCreate.getAut() == null) {
                throw new ServiceEntitlementException(20, "Can't generate K_Aut!");
            }
            byte[] bArrGenerateEapAkaChallengeResponse = generateEapAkaChallengeResponse(res, eapAkaChallenge.getIdentifier(), masterKeyCreate.getAut());
            if (bArrGenerateEapAkaChallengeResponse == null) {
                throw new ServiceEntitlementException(20, "Failed to generate EAP-AKA Challenge Response data!");
            }
            eapAkaResponse.mResponse = Base64.encodeToString(bArrGenerateEapAkaChallengeResponse, 2).trim();
            return eapAkaResponse;
        } catch (UnsupportedOperationException e) {
            throw new ServiceEntitlementException(20, "UnsupportedOperationException" + e.toString());
        }
    }

    static byte[] generateEapAkaChallengeResponse(byte[] bArr, byte b, byte[] bArr2) {
        byte[] bArrCreateEapAkaChallengeResponse;
        byte[] bArrCalculateMac;
        if (bArr == null || bArr2 == null || (bArrCalculateMac = calculateMac(bArr2, (bArrCreateEapAkaChallengeResponse = createEapAkaChallengeResponse(bArr, b)))) == null) {
            return null;
        }
        System.arraycopy(bArrCalculateMac, 0, bArrCreateEapAkaChallengeResponse, bArr.length + 16, bArrCalculateMac.length);
        return bArrCreateEapAkaChallengeResponse;
    }

    static byte[] generateEapAkaSynchronizationFailureResponse(byte[] bArr, byte b) {
        int length = bArr.length + 10;
        byte[] bArr2 = new byte[length];
        bArr2[0] = 2;
        bArr2[1] = b;
        byte[] bArrConvertIntegerTo4Bytes = BytesConverter.convertIntegerTo4Bytes(length);
        bArr2[2] = bArrConvertIntegerTo4Bytes[2];
        bArr2[3] = bArrConvertIntegerTo4Bytes[3];
        bArr2[4] = 23;
        bArr2[5] = 4;
        bArr2[6] = 0;
        bArr2[7] = 0;
        bArr2[8] = 4;
        bArr2[9] = 4;
        System.arraycopy(bArr, 0, bArr2, 10, bArr.length);
        return bArr2;
    }

    private static byte[] createEapAkaChallengeResponse(byte[] bArr, byte b) {
        int length = bArr.length + 32;
        byte[] bArr2 = new byte[length];
        bArr2[0] = 2;
        bArr2[1] = b;
        byte[] bArrConvertIntegerTo4Bytes = BytesConverter.convertIntegerTo4Bytes(length);
        bArr2[2] = bArrConvertIntegerTo4Bytes[2];
        bArr2[3] = bArrConvertIntegerTo4Bytes[3];
        bArr2[4] = 23;
        bArr2[5] = 1;
        bArr2[6] = 0;
        bArr2[7] = 0;
        bArr2[8] = 3;
        bArr2[9] = (byte) (((bArr.length + 4) / 4) & 255);
        byte[] bArrConvertIntegerTo4Bytes2 = BytesConverter.convertIntegerTo4Bytes(bArr.length * 8);
        bArr2[10] = bArrConvertIntegerTo4Bytes2[2];
        bArr2[11] = bArrConvertIntegerTo4Bytes2[3];
        System.arraycopy(bArr, 0, bArr2, 12, bArr.length);
        int length2 = bArr.length;
        bArr2[12 + length2] = 11;
        bArr2[length2 + 13] = 5;
        bArr2[length2 + 14] = 0;
        bArr2[length2 + 15] = 0;
        Arrays.fill(bArr2, length2 + 16, length2 + 32, (byte) 0);
        return bArr2;
    }

    private static byte[] calculateMac(byte[] bArr, byte[] bArr2) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(bArr, "HmacSHA1"));
            byte[] bArrDoFinal = mac.doFinal(bArr2);
            if (bArrDoFinal != null && bArrDoFinal.length == 20) {
                byte[] bArr3 = new byte[16];
                System.arraycopy(bArrDoFinal, 0, bArr3, 0, 16);
                return bArr3;
            }
            Log.e("ServiceEntitlement", "Invalid result! length should be 20, but " + bArrDoFinal.length);
            return null;
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            Log.e("ServiceEntitlement", "calculateMac failed!", e);
            return null;
        }
    }
}
