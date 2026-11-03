package com.android.phone;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.android.internal.os.BackgroundThread;
import com.android.phone.networkmode.NetworkMode;
import com.android.phone.utils.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miui.telephony.DefaultSimManager;
import miui.telephony.VirtualSimUtils;

/* JADX INFO: loaded from: classes.dex */
public class Dual4GManager {
    private static String LOG_TAG = "Dual4GManager";
    private static Dual4GManager sInstance;
    private List mDual4GListener;
    private Context mContext = PhoneGlobals.getInstance();
    private Dual4GHandler mHandler = new Dual4GHandler(BackgroundThread.get().getLooper());

    private static int getDual4GModeDefaultValue() {
        return 1;
    }

    class EnableDual4GParams {
        Utils.Dual4GCallBack callback;
        boolean enabled;

        EnableDual4GParams(Dual4GManager dual4GManager, boolean z, Utils.Dual4GCallBack dual4GCallBack) {
            this.enabled = z;
            this.callback = dual4GCallBack;
        }

        public String toString() {
            return "{enabled=" + this.enabled + ", callback=" + this.callback + "}";
        }
    }

    public static void init() {
        if (sInstance == null) {
            sInstance = new Dual4GManager();
        }
    }

    public static Dual4GManager getInstance() {
        return sInstance;
    }

    private Dual4GManager() {
        this.mDual4GListener = null;
        this.mDual4GListener = new ArrayList(1);
    }

    public class Dual4GHandler extends Handler {
        public Dual4GHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            Object obj = message.obj;
            if (obj instanceof AsyncResult) {
                AsyncResult asyncResult = (AsyncResult) obj;
                Rlog.d(Dual4GManager.LOG_TAG, "EVENT_SET_VICE_NETWORK_TYPE_DONE exception=" + asyncResult.exception + ", slot = " + message.arg1 + ", viceNetworkMode = " + message.arg2);
                int i = asyncResult.exception != null ? -1 : 0;
                Object obj2 = asyncResult.userObj;
                if (obj2 instanceof EnableDual4GParams) {
                    EnableDual4GParams enableDual4GParams = (EnableDual4GParams) obj2;
                    if (i == 0) {
                        ModemProvider.getInstance().setNetworkModeInDb(message.arg1, message.arg2);
                        Dual4GManager.this.notifyDual4GModeChanged(enableDual4GParams.enabled);
                    } else {
                        Settings.Global.putInt(Dual4GManager.this.mContext.getContentResolver(), "dual_4g_mode_enabled", !enableDual4GParams.enabled ? 1 : 0);
                    }
                    Utils.Dual4GCallBack dual4GCallBack = enableDual4GParams.callback;
                    if (dual4GCallBack != null) {
                        dual4GCallBack.onSetDual4GResult(enableDual4GParams.enabled, i);
                    }
                }
            }
        }
    }

    public void setDual4GMode(boolean z, Utils.Dual4GCallBack dual4GCallBack) {
        Rlog.d(LOG_TAG, "setDual4GMode enabled=" + z);
        Settings.Global.putInt(this.mContext.getContentResolver(), "dual_4g_mode_enabled", z ? 1 : 0);
        int[][] supportModes = NetworkMode.getInstance().getSupportModes();
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (i != defaultDataSlotId) {
                int[] iArr = supportModes[i];
                if (iArr.length == 0) {
                    Rlog.d(LOG_TAG, "No valid networkMode, skip update modem rat");
                } else {
                    int i2 = iArr[0];
                    Rlog.d(LOG_TAG, "viceNetworkMode=" + i2 + ", slotId=" + i);
                    MiuiPhoneUtils.getPhone(i).setAllowedNetworkTypes(0, (long) RadioAccessFamily.getRafFromNetworkType(i2), this.mHandler.obtainMessage(1, i, i2, new EnableDual4GParams(this, z, dual4GCallBack)));
                }
            }
        }
    }

    public static boolean isDual4GEnabled() {
        if (MiuiPhoneUtils.isRemoveDual4GSwitch()) {
            return true;
        }
        if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            return VirtualSimUtils.isCloudSimEnabled() || Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "dual_4g_mode_enabled", getDual4GModeDefaultValue()) == 1;
        }
        return false;
    }

    public void notifyDual4GModeChanged(boolean z) {
        synchronized (this.mDual4GListener) {
            try {
                Iterator it = this.mDual4GListener.iterator();
                if (it.hasNext()) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                    throw null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
