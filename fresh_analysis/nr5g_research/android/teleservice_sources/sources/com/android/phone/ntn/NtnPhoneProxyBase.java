package com.android.phone.ntn;

import android.os.Handler;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhoneProxyBase {
    protected static volatile NtnPhoneProxyBase mInstance;

    public boolean destroyNtnPhone() {
        return false;
    }

    public Phone getNtnPhone() {
        return null;
    }

    public int getRadioState() {
        return 2;
    }

    public boolean isNtnInService() {
        return false;
    }

    public void registerForRadioStateChanged(Handler handler, int i, Object obj) {
    }

    public void setRadioPower(boolean z, boolean z2, boolean z3, boolean z4) {
    }

    public boolean startNtnPhone() {
        return false;
    }

    public void unregisterForRadioStateChanged(Handler handler) {
    }

    public static NtnPhoneProxyBase getInstance() {
        if (mInstance == null) {
            synchronized (NtnPhoneProxyBase.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new NtnPhoneProxyBase();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }
}
