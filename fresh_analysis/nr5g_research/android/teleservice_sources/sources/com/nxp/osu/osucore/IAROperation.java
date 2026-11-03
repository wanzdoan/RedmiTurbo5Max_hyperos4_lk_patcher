package com.nxp.osu.osucore;

import android.util.Log;
import com.nxp.osu.IOsuAuthCallback;
import com.nxp.osu.channel.OsuOmapiApduChannel;

/* JADX INFO: loaded from: classes.dex */
public class IAROperation {
    private static final byte[] SUCCESS_RESP = {-112, 0};
    private static IAROperation sIAROperation = null;

    public enum IAR_SPECIFIC_OPERATION {
        PRECONDITION,
        POSTCONDITION,
        ISJCOPMODE
    }

    private boolean isAuthRequired() {
        return false;
    }

    public boolean doPostCondition() {
        return true;
    }

    public boolean isAuthCallBackSet() {
        return false;
    }

    public void setOsuAuthCallback(IOsuAuthCallback iOsuAuthCallback) {
    }

    public static IAROperation getInstance() {
        if (sIAROperation == null) {
            sIAROperation = new IAROperation();
        }
        return sIAROperation;
    }

    private boolean validateResponse(byte[] bArr, byte[] bArr2) {
        if (bArr != null && bArr.length >= 2 && bArr2 != null && bArr2.length == 2) {
            return bArr2[1] == bArr[bArr.length - 1] && bArr2[0] == bArr[bArr.length - 2];
        }
        Log.e("OSU-IAROperation", "Invalid Response to Check");
        return false;
    }

    public boolean doPreCondition() {
        if (!isAuthRequired()) {
            Log.d("OSU-IAROperation", "Authentication Not Required.");
            return true;
        }
        try {
            if (!validateResponse(OsuOmapiApduChannel.doIARSpecific(IAR_SPECIFIC_OPERATION.PRECONDITION), SUCCESS_RESP)) {
                Log.e("OSU-IAROperation", "IAR specific precondition failed...");
            }
        } catch (Exception e) {
            Log.e("OSU-IAROperation", "Caught Exception in doPreCondition");
            e.printStackTrace();
        }
        throw null;
    }
}
