package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class MtkSuppServManager {
    private static final boolean DBG = true;
    public static final String LOG_TAG = "SuppServManager";
    private Context mContext;
    private HandlerThread mHandlerThread;
    private Phone[] mPhones;
    private static MtkSuppServManager instance = null;
    private static MtkSuppServQueueHelper mSuppServQueueHelper = null;
    private static MtkSuppServHelper[] mSuppServHelper = null;

    private MtkSuppServManager(Context context, Phone[] phones) {
        this.mContext = context;
        this.mPhones = phones;
    }

    public static MtkSuppServManager getInstance() {
        return instance;
    }

    public static MtkSuppServManager makeSuppServManager(Context context, Phone[] phones) {
        if (context == null || phones == null) {
            return null;
        }
        if (instance == null) {
            Rlog.d(LOG_TAG, "Create MtkSuppServManager singleton instance, phones.length = " + phones.length);
            instance = new MtkSuppServManager(context, phones);
        } else {
            instance.mContext = context;
            instance.mPhones = phones;
        }
        return instance;
    }

    public static MtkSuppServQueueHelper getSuppServQueueHelper() {
        if (mSuppServQueueHelper != null) {
            return mSuppServQueueHelper;
        }
        Rlog.e(LOG_TAG, "Get MtkSuppServQueueHelper instance failed!");
        return null;
    }

    public static MtkSuppServHelper getSuppServHelper(int phoneId) {
        if (mSuppServHelper != null && mSuppServHelper.length > phoneId && phoneId >= 0) {
            return mSuppServHelper[phoneId];
        }
        Rlog.e(LOG_TAG, "Get MtkSuppServHelper instance failed!");
        return null;
    }

    public void init() {
        Rlog.d(LOG_TAG, "Initialize MtkSuppServManager!");
        this.mHandlerThread = new HandlerThread("MtkSuppServManager");
        this.mHandlerThread.start();
        Looper looper = this.mHandlerThread.getLooper();
        if (!supportMdAutoSetupIms()) {
            mSuppServQueueHelper = MtkSuppServQueueHelper.makeSuppServQueueHelper(this.mContext, this.mPhones);
            mSuppServQueueHelper.init(looper);
        }
        int numPhones = TelephonyManager.getDefault().getPhoneCount();
        mSuppServHelper = new MtkSuppServHelper[numPhones];
        for (int i = 0; i < numPhones; i++) {
            mSuppServHelper[i] = new MtkSuppServHelper(this.mContext, this.mPhones[i], looper);
            mSuppServHelper[i].init(looper);
        }
    }

    public void dispose() {
        Rlog.d(LOG_TAG, "dispose.");
        Looper looper = this.mHandlerThread.getLooper();
        looper.quit();
    }

    private boolean supportMdAutoSetupIms() {
        if (!SystemProperties.get("ro.vendor.md_auto_setup_ims").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return false;
        }
        return DBG;
    }
}
