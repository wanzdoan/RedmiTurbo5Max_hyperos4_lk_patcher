package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import androidx.preference.PreferenceManager;
import com.android.internal.os.BackgroundThread;
import com.android.services.telephony.ims.ImsAdapterBase;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class VolteEnableManager implements DefaultSimManager.DataSlotListener {
    private static String LOG_TAG = "VolteEnableManager";
    private static volatile VolteEnableManager sInstance;
    private Context mContext;
    private VolteEnableHandler mHandler = new VolteEnableHandler(BackgroundThread.get().getLooper());

    public static void init() {
        if (sInstance == null) {
            synchronized (VolteEnableManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new VolteEnableManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static VolteEnableManager getInstance() {
        return sInstance;
    }

    private VolteEnableManager() {
        DefaultSimManager.getInstance().addDataSlotListener(this);
        this.mContext = PhoneGlobals.getInstance();
    }

    public class VolteEnableHandler extends Handler {
        public VolteEnableHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                Rlog.d(VolteEnableManager.LOG_TAG, "onDataSlotReady dataSlot=" + defaultDataSlotId);
                return;
            }
            if (i != 2) {
                return;
            }
            Rlog.d(VolteEnableManager.LOG_TAG, "EVENT_DATA_SLOT_CHANGED");
            if (MiuiPhoneUtils.isCTDualVolteSupported() && MiuiPhoneUtils.isDualCTSim()) {
                int defaultDataSlotId2 = DefaultSimManager.getDefaultDataSlotId();
                int userSelectedVolteState = VolteEnableManager.this.getUserSelectedVolteState(SubscriptionManager.getDefault().getSubscriptionIdForSlot(defaultDataSlotId2));
                if (userSelectedVolteState == -1) {
                    if (!MiuiPhoneUtils.shouldOpenVolteForCt()) {
                        ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(VolteEnableManager.this.mContext, false, defaultDataSlotId2);
                    }
                } else {
                    ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(VolteEnableManager.this.mContext, userSelectedVolteState == 1, defaultDataSlotId2);
                }
                CarrierConfigLoaderInjector.enableViceSlotVolteForDualCtSim();
            }
        }
    }

    public void onDataSlotReady(boolean z) {
        VolteEnableHandler volteEnableHandler = this.mHandler;
        if (z) {
            volteEnableHandler.sendMessage(volteEnableHandler.obtainMessage(2));
        } else {
            volteEnableHandler.sendMessage(volteEnableHandler.obtainMessage(1));
        }
    }

    public int getUserSelectedVolteState(int i) {
        int i2 = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getInt("user_selected_volte_state" + i, -1);
        Rlog.d(LOG_TAG, "getUserSelectedVolteState subId=" + i + ", result=" + i2);
        return i2;
    }

    public void saveUserSelectedVolteState(int i, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putInt("user_selected_volte_state" + i, z ? 1 : 0);
        editorEdit.apply();
    }

    public int getForceSelectedVolteState(int i) {
        int i2 = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getInt("force_enable_volte_state" + i, -1);
        Rlog.d(LOG_TAG, "getUserSelectedVolteState subId=" + i + ", result=" + i2);
        return i2;
    }

    public void saveForceSelectedVolteState(int i, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putInt("force_enable_volte_state" + i, z ? 1 : 0);
        editorEdit.apply();
    }

    public void clearUserSelectedVolteState(int i) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putInt("user_selected_volte_state" + i, -1);
        editorEdit.apply();
    }
}
