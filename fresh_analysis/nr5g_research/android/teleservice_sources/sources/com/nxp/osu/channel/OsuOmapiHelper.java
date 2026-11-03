package com.nxp.osu.channel;

import android.util.Log;
import com.nxp.osu.util.OSULogger;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public abstract class OsuOmapiHelper {
    public static final byte[] RESET_APDU = {0, -42, -128, 19, 4, 74, 82, 69, 83, 0};
    public static final byte[] OSU_AID_SN3XX = {-46, 118, 0, 0, -123, 65, 0, 0, 0, 0, 32, 0};
    public static final byte[] OSUTrigger = {79, 112, -128, 19, 4, -34, -83, -66, -17};
    private static final byte[] UAITrigger = {79, 112, -128, 19, 4, 74, 85, 65, 73};
    public static final byte[] GP_UAITrigger = {0, -42, -128, 19, 4, 74, 85, 65, 73, 0};
    private static final byte[] CCI_CHECK = {-95, -86};
    private static final byte[] JCI_CHECK = {-95, -69};
    private static final byte[] DGI_HEADER = {-33, 92};
    private static boolean sIntegrityRequired = true;
    private static boolean sIsJcopNormalMode = false;
    private static boolean sIsSmb = false;
    private static boolean sIntegritySupported = true;
    private static boolean sWrapApduSupported = true;
    private static final boolean IS_DEBUG = OSULogger.isDebug();
    private static byte sOsVersionId = 0;

    enum APDU_TYPE {
        TRIGGER_UAI,
        JCI,
        CCI,
        DEADBEEF,
        DGI,
        NON_WRAPPER
    }

    public static void setTerminalSmb(boolean z) {
        sIsSmb = z;
    }

    public static boolean isJcopInNormalMode() {
        return sIsJcopNormalMode;
    }

    public static boolean isIntegrityCheckrequired() {
        if (IS_DEBUG) {
            Log.d("OSU-OMAPI-HELPER", "Integrity Feature " + sIntegritySupported + " isIntegrityCheckrequired " + sIntegrityRequired);
        }
        return sIntegritySupported && sIntegrityRequired;
    }

    public static void setIntegrityFeatureSupported(boolean z) {
        sIntegritySupported = z;
    }

    public static void setWrapApduSupported(boolean z) {
        sWrapApduSupported = z;
    }

    public static void parseATR(byte[] bArr) {
        sIntegrityRequired = true;
        sIsJcopNormalMode = false;
        if (bArr != null && bArr.length >= 5) {
            if (IS_DEBUG) {
                Log.d("OSU-OMAPI-HELPER", "ATR Version Number " + ((int) bArr[4]) + " Min Ver Req : 17");
            }
            byte b = bArr[4];
            if (b < 17) {
                sIntegrityRequired = false;
            }
            if (bArr.length > 20 && bArr[20] == 1) {
                sIsJcopNormalMode = true;
            }
            if (sIsSmb) {
                return;
            }
            sOsVersionId = b;
            return;
        }
        sIsJcopNormalMode = true;
    }

    public static byte getOsVersionId() {
        return sOsVersionId;
    }

    private static APDU_TYPE checkAPDU(byte[] bArr) {
        if (bArr == null) {
            return APDU_TYPE.NON_WRAPPER;
        }
        byte b = bArr[0];
        byte[] bArr2 = DGI_HEADER;
        if (b == bArr2[0] && bArr[1] == bArr2[1]) {
            return APDU_TYPE.DGI;
        }
        byte[] bArr3 = JCI_CHECK;
        if (b == bArr3[0] && bArr[1] == bArr3[1]) {
            return APDU_TYPE.JCI;
        }
        byte[] bArr4 = CCI_CHECK;
        if (b == bArr4[0] && bArr[1] == bArr4[1]) {
            return APDU_TYPE.CCI;
        }
        int length = bArr.length;
        byte[] bArr5 = OSUTrigger;
        if (length >= bArr5.length && Arrays.equals(Arrays.copyOfRange(bArr, 0, bArr5.length), bArr5)) {
            return APDU_TYPE.DEADBEEF;
        }
        int length2 = bArr.length;
        byte[] bArr6 = UAITrigger;
        if (length2 >= bArr6.length && Arrays.equals(Arrays.copyOfRange(bArr, 0, bArr6.length), bArr6)) {
            return APDU_TYPE.TRIGGER_UAI;
        }
        return APDU_TYPE.NON_WRAPPER;
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0048  */
    /* JADX WARN: Code duplicated, block: B:21:0x0051  */
    /* JADX WARN: Code duplicated, block: B:23:0x0055  */
    /* JADX WARN: Code duplicated, block: B:26:0x0079  */
    /* JADX WARN: Code duplicated, block: B:28:0x007d  */
    /* JADX WARN: Code duplicated, block: B:31:0x008e A[RETURN] */
    private static byte[] wrapToGPcmd(byte[] bArr, APDU_TYPE apdu_type) {
        boolean z;
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = {-128, -33, 0, 0, 0};
        short length = (short) bArr.length;
        int iOrdinal = apdu_type.ordinal();
        if (iOrdinal == 0) {
            bArr2[2] = 1;
        } else if (iOrdinal == 1) {
            bArr2[2] = 2;
        } else if (iOrdinal == 2) {
            bArr2[2] = 3;
        } else {
            if (iOrdinal != 3) {
                if (iOrdinal != 4) {
                    z = false;
                } else {
                    bArr2[1] = -54;
                    bArr2[2] = 0;
                    bArr2[3] = -2;
                    length = (short) (bArr.length - 1);
                }
                if (z) {
                    return bArr;
                }
                bArr2[4] = (byte) length;
                if (length > 255) {
                    if (IS_DEBUG) {
                        Log.d("OSU-OMAPI-HELPER", "Extention Byte Mode");
                    }
                    bArr2[4] = 0;
                    byte[] bArr3 = new byte[bArr.length + 7];
                    System.arraycopy(bArr2, 0, bArr3, 0, 5);
                    System.arraycopy(new byte[]{(byte) ((length >> 8) & 255), (byte) (length & 255)}, 0, bArr3, 5, 2);
                    System.arraycopy(bArr, 0, bArr3, 7, bArr.length);
                    return bArr3;
                }
                if (IS_DEBUG) {
                    Log.d("OSU-OMAPI-HELPER", "NON-Extention Byte Mode");
                }
                byte[] bArr4 = new byte[bArr.length + 5];
                System.arraycopy(bArr2, 0, bArr4, 0, 5);
                System.arraycopy(bArr, 0, bArr4, 5, bArr.length);
                return bArr4;
            }
            bArr2[2] = 4;
        }
        z = true;
        if (z) {
            return bArr;
        }
        bArr2[4] = (byte) length;
        if (length > 255) {
            if (IS_DEBUG) {
                Log.d("OSU-OMAPI-HELPER", "Extention Byte Mode");
            }
            bArr2[4] = 0;
            byte[] bArr5 = new byte[bArr.length + 7];
            System.arraycopy(bArr2, 0, bArr5, 0, 5);
            System.arraycopy(new byte[]{(byte) ((length >> 8) & 255), (byte) (length & 255)}, 0, bArr5, 5, 2);
            System.arraycopy(bArr, 0, bArr5, 7, bArr.length);
            return bArr5;
        }
        if (IS_DEBUG) {
            Log.d("OSU-OMAPI-HELPER", "NON-Extention Byte Mode");
        }
        byte[] bArr6 = new byte[bArr.length + 5];
        System.arraycopy(bArr2, 0, bArr6, 0, 5);
        System.arraycopy(bArr, 0, bArr6, 5, bArr.length);
        return bArr6;
    }

    public static byte[] checkAndwrapPacket(byte[] bArr) {
        if (!sWrapApduSupported) {
            if (IS_DEBUG) {
                Log.d("OSU-OMAPI-HELPER", "Wrap APDU not supported");
            }
            return bArr;
        }
        return checkAndwrapPacketInternal(bArr);
    }

    private static byte[] checkAndwrapPacketInternal(byte[] bArr) {
        APDU_TYPE apdu_typeCheckAPDU = checkAPDU(bArr);
        return apdu_typeCheckAPDU != APDU_TYPE.NON_WRAPPER ? wrapToGPcmd(bArr, apdu_typeCheckAPDU) : bArr;
    }

    public static byte[] createIntegrityCheckCmd(byte[] bArr, byte[] bArr2) {
        int i;
        if (bArr == null || bArr2 == null) {
            Log.e("OSU-OMAPI-HELPER", "Invalid Hash or Signature");
            return null;
        }
        byte length = sOsVersionId >= 48 ? (byte) bArr2.length : (byte) 64;
        int length2 = bArr.length + length + 4;
        int i2 = length2 > 127 ? 1 : 0;
        byte[] bArr3 = DGI_HEADER;
        byte[] bArr4 = new byte[bArr3.length + 1 + length2 + 1 + i2];
        System.arraycopy(bArr3, 0, bArr4, 0, bArr3.length);
        int length3 = bArr3.length;
        if (length2 > 127) {
            bArr4[length3] = -127;
            length3++;
        }
        bArr4[length3] = (byte) (bArr.length + length + 4);
        int i3 = length3 + 2;
        bArr4[length3 + 1] = -128;
        int i4 = length3 + 3;
        bArr4[i3] = (byte) bArr.length;
        System.arraycopy(bArr, 0, bArr4, i4, bArr.length);
        int length4 = i4 + bArr.length;
        bArr4[length4] = -127;
        int i5 = length4 + 2;
        bArr4[length4 + 1] = length;
        if (bArr2.length == length) {
            if (IS_DEBUG) {
                Log.d("OSU-OMAPI-HELPER", "Using Same Signature as JSBL");
            }
            System.arraycopy(bArr2, 0, bArr4, i5, length);
            i = i5 + length;
        } else {
            if (bArr2.length != length + 1) {
                Log.e("OSU-OMAPI-HELPER", "Invalid Signature Length in JSBL");
                return null;
            }
            if (IS_DEBUG) {
                Log.d("OSU-OMAPI-HELPER", "Remove Delimiter from Signature received from JSBL");
            }
            System.arraycopy(bArr2, 0, bArr4, i5, 32);
            System.arraycopy(bArr2, 33, bArr4, length4 + 34, 32);
            i = length4 + 66;
        }
        bArr4[i] = 0;
        return checkAndwrapPacketInternal(bArr4);
    }
}
