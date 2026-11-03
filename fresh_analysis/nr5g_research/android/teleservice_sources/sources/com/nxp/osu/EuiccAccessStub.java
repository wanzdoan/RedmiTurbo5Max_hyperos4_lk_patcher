package com.nxp.osu;

import android.util.Log;
import com.xiaomi.telephony.EuiccOsuManagerBase;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class EuiccAccessStub implements ISecOsuAccess, IModem {
    static final byte[] RQST_SELECT_ISDR = {0, -92, 4, 0, 16, -96, 0, 0, 5, 89, 16, 16, -1, -1, -1, -1, -119, 0, 0, 1, 0};
    private static byte[] UAI_QUERY = {-128, -54, 0, -2, 2, -33, 67};
    private static final byte[] RESP_SUCCESS = {-112, 0};
    private static final byte[] OSUTrigger = {79, 112, -128, 19, 4, -34, -83, -66, -17};
    private static EuiccAccessStub sEuiccAccessStub = null;
    private static final byte[] RESP_ATR = {0, 0, 0, 0, 33, 1, 0, 1, 3, -24, 97, -88, 0, -2, 3, 2, 0, 0, 28, 0, 1};
    private static final byte[] DGI_HEADER = {-33, 92};
    private EuiccCmdParserMockRsp mMockTransport = new EuiccCmdParserMockRsp();
    byte[] GP_HEADER_INTEGRITY_CHECK = {-128, -54, 0, -2, 0};

    private EuiccAccessStub() {
    }

    public static EuiccAccessStub getInstance() {
        if (sEuiccAccessStub == null) {
            sEuiccAccessStub = new EuiccAccessStub();
        }
        return sEuiccAccessStub;
    }

    private static String toString(byte[] bArr) {
        if (bArr == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            sb.append(String.format("%02x", Byte.valueOf(b)));
        }
        return sb.toString();
    }

    @Override // com.nxp.osu.channel.IAccess
    public void reset() {
        Log.d("OSU-EuiccAccessStub", "reset enter");
        NfcNciHandler.getInstance().performNciColdReset();
        if (EuiccOsuManagerBase.getInstance() != null) {
            EuiccOsuManagerBase.getInstance().doWarmReset();
        }
        Log.d("OSU-EuiccAccessStub", "reset exit");
    }

    @Override // com.nxp.osu.channel.IAccess
    public byte[] open() {
        Log.d("OSU-EuiccAccessStub", "open enter");
        Log.d("OSU-EuiccAccessStub", "Initializing Transport");
        send(RQST_SELECT_ISDR);
        Log.d("OSU-EuiccAccessStub", "open exit");
        return RESP_SUCCESS;
    }

    @Override // com.nxp.osu.channel.IAccess
    public byte[] openChannel(byte[] bArr) {
        byte[] bArrOsuOpenChannel;
        Log.d("OSU-EuiccAccessStub", "openChannel(" + toString(bArr) + ") enter");
        if (!Arrays.equals(bArr, OSUTrigger) && EuiccOsuManagerBase.getInstance() != null) {
            bArrOsuOpenChannel = EuiccOsuManagerBase.getInstance().osuOpenChannel(bArr);
        } else {
            Log.d("OSU-EuiccAccessStub", "skip OSUTrigger");
            bArrOsuOpenChannel = RESP_SUCCESS;
        }
        Log.d("OSU-EuiccAccessStub", "openChannel(" + toString(bArrOsuOpenChannel) + ")");
        return bArrOsuOpenChannel;
    }

    @Override // com.nxp.osu.channel.IAccess
    public byte[] getAtr() {
        Log.d("OSU-EuiccAccessStub", "getAtr enter");
        Log.d("OSU-EuiccAccessStub", "getAtr exit");
        return RESP_ATR;
    }

    @Override // com.nxp.osu.channel.IAccess
    public void close() {
        Log.d("OSU-EuiccAccessStub", "close enter");
        Log.d("OSU-EuiccAccessStub", "Deinitializing transport");
        Log.d("OSU-EuiccAccessStub", "close exit");
    }

    @Override // com.nxp.osu.channel.IAccess
    public byte[] send(byte[] bArr) {
        Log.d("OSU-EuiccAccessStub", "send(" + toString(bArr) + ")");
        if (Arrays.equals(bArr, UAI_QUERY) || isIntegrityCommand(bArr)) {
            return handleCommand(bArr);
        }
        if (EuiccOsuManagerBase.getInstance() != null) {
            return EuiccOsuManagerBase.getInstance().osuSend(bArr);
        }
        return null;
    }

    private boolean isIntegrityCommand(byte[] bArr) {
        if (bArr != null) {
            try {
                int length = bArr.length;
                byte[] bArr2 = this.GP_HEADER_INTEGRITY_CHECK;
                int length2 = bArr2.length;
                byte[] bArr3 = DGI_HEADER;
                if (length >= length2 + bArr3.length && bArr[1] == bArr2[1] && bArr[2] == bArr2[2] && bArr[3] == bArr2[3]) {
                    if (bArr[4] == 0 && bArr[bArr2.length + 2] == bArr3[0] && bArr[bArr2.length + 3] == bArr3[1]) {
                        Log.d("OSU-EuiccAccessStub", "extended length check integrity");
                        return true;
                    }
                    if (bArr[bArr2.length] == bArr3[0] && bArr[bArr2.length + 1] == bArr3[1]) {
                        Log.d("OSU-EuiccAccessStub", "non-extended length check integrity");
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private byte[] handleCommand(byte[] bArr) {
        if (EuiccOsuManagerBase.getInstance() == null) {
            return null;
        }
        byte[] bArrOsuSend = EuiccOsuManagerBase.getInstance().osuSend(bArr);
        byte[] bArr2 = RESP_SUCCESS;
        if (validateResponse(bArrOsuSend, bArr2)) {
            return bArrOsuSend;
        }
        byte[] bArrOsuSend2 = EuiccOsuManagerBase.getInstance().osuSend(RQST_SELECT_ISDR);
        return validateResponse(bArrOsuSend2, bArr2) ? EuiccOsuManagerBase.getInstance().osuSend(bArr) : bArrOsuSend2;
    }

    private static boolean validateResponse(byte[] bArr, byte[] bArr2) {
        if (bArr != null && bArr.length >= 2 && bArr2 != null && bArr2.length == 2) {
            return bArr2[1] == bArr[bArr.length - 1] && bArr2[0] == bArr[bArr.length - 2];
        }
        Log.e("OSU-EuiccAccessStub", "Invalid Response to Check");
        return false;
    }

    @Override // com.nxp.osu.IModem
    public boolean setModemDirectMode(boolean z) {
        if (EuiccOsuManagerBase.getInstance() == null) {
            return false;
        }
        EuiccOsuManagerBase.getInstance().setModemDirectMode(z);
        if (z) {
            Log.d("OSU-EuiccAccessStub", "Enabling passthrough mode");
            return true;
        }
        Log.d("OSU-EuiccAccessStub", "Disabling passthrough mode");
        return true;
    }
}
