package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.uicc.UiccSlot;

/* JADX INFO: loaded from: classes.dex */
public class MtkUiccSlot extends UiccSlot {
    static final String LOG_TAG = "MtkUiccSlot";

    public MtkUiccSlot(Context c, boolean isActive, FeatureFlags flags) {
        super(c, isActive, flags);
        Rlog.d(LOG_TAG, "Creating");
    }

    public boolean isEuicc() {
        return super.isEuicc() && !TextUtils.isEmpty(getEid());
    }

    public void resetPortAndState() {
        int[] portList = getPortList();
        int activePort = 0;
        if (portList == null) {
            return;
        }
        for (int i : portList) {
            if (isPortActive(i)) {
                activePort++;
            }
        }
        Rlog.d(LOG_TAG, "resetPortAndState, activePort = " + activePort);
        if (activePort > 1) {
            if (this.mUiccCard != null) {
                for (int i2 : portList) {
                    this.mUiccCard.disposePort(i2);
                }
            }
            this.mCardState.clear();
        }
    }
}
