package com.nxp.osu.modem;

import android.util.Log;
import com.nxp.osu.IModem;
import com.nxp.osu.util.OSULogger;

/* JADX INFO: loaded from: classes.dex */
public class ModemImp implements IModem {
    private static final boolean IS_DEBUG = OSULogger.isDebug();

    @Override // com.nxp.osu.IModem
    public boolean setModemDirectMode(boolean z) {
        if (!IS_DEBUG) {
            return true;
        }
        Log.d("ModemImp", "setModemDirectMode " + z);
        return true;
    }
}
