package com.mediatek.internal.telephony;

import android.os.SystemProperties;
import com.android.internal.telephony.HardwareConfig;

/* JADX INFO: loaded from: classes.dex */
public final class MtkHardwareConfig extends HardwareConfig {
    public MtkHardwareConfig(int type) {
        super(type);
    }

    public MtkHardwareConfig(String res) {
        super(res);
    }

    public boolean hasLimitDataProfilesCapability() {
        if (SystemProperties.get("ro.vendor.mtk_mipc_support").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return true;
        }
        return false;
    }

    public boolean hasModemMspmCapability() {
        if (SystemProperties.get("ro.vendor.mtk_mipc_support").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return true;
        }
        return false;
    }
}
