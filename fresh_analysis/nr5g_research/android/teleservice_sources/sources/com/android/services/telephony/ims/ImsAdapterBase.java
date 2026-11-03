package com.android.services.telephony.ims;

import android.content.Context;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class ImsAdapterBase {
    protected static volatile ImsAdapterBase sInstance;

    public int booleanToPropertyInt(boolean z) {
        return z ? 1 : 0;
    }

    public boolean getEnhanced4gLteModeSetting(Context context, int i) {
        return false;
    }

    public int getWfcMode(Context context, int i, boolean z) {
        return -1;
    }

    public boolean hideEnhanded4GLte(int i) {
        return false;
    }

    public boolean hideWfc(Context context, int i) {
        return false;
    }

    public boolean isCrossSimCallingEnabled(Context context, int i) {
        return false;
    }

    public boolean isCrossSimCallingEnabledByUser(Context context, int i) {
        return false;
    }

    public boolean isImsPreferredSupported(int i) {
        return false;
    }

    public boolean isUseWfcHomeNetworkinRoaming(Context context, int i) {
        return false;
    }

    public boolean isVideoEnabled(Phone phone) {
        return false;
    }

    public boolean isVolteSupportedByDevice(Context context) {
        return false;
    }

    public boolean isWfcEnabledByPlatform(Context context, int i) {
        return false;
    }

    public boolean isWfcEnabledByUser(Context context, int i) {
        return false;
    }

    public boolean isWfcModeEditable(Context context, int i) {
        return true;
    }

    public boolean isWfcRoamingModeEditable(Context context, int i) {
        return false;
    }

    public boolean isWfcSupportWifiOnly(Context context, int i) {
        return false;
    }

    public void setCrossSimCallingDisabledByCloud(Context context) {
    }

    public void setCrossSimCallingEnabled(Context context, int i, boolean z) {
    }

    public void setEnhanced4gLteModeSetting(Context context, boolean z) {
    }

    public void setEnhanced4gLteModeSetting(Context context, boolean z, int i) {
    }

    public void setEnhanced4gLteModeSettingDB(Context context, int i) {
    }

    public void setEnhanced4gLteModeSettingDB(Context context, int i, int i2) {
    }

    public void setWfcMode(Context context, int i, int i2, boolean z) {
    }

    public void setWfcSetting(Context context, int i, boolean z) {
    }

    public static ImsAdapterBase getInstance() {
        if (sInstance == null) {
            synchronized (ImsAdapterBase.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new ImsAdapterBase();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }
}
