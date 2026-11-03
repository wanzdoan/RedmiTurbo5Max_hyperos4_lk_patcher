package com.mediatek.internal.telephony;

import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.DisplayInfoController;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public final class MtkDisplayInfoController extends DisplayInfoController {
    private static final String TAG = "MtkDisplayInfoController";

    public MtkDisplayInfoController(Phone phone, FeatureFlags featureFlags) {
        super(phone, featureFlags);
        if (this.mNetworkTypeController != null) {
            this.mNetworkTypeController.quitNow();
        }
        this.mNetworkTypeController = new MtkNetworkTypeController(phone, this, featureFlags);
        this.mNetworkTypeController.sendMessage(0);
    }

    public void updateTelephonyDisplayInfo() {
        MtkServiceStateTracker sst = (MtkServiceStateTracker) this.mPhone.getServiceStateTracker();
        if (sst.isTurboSSAlive()) {
            logl("updateTelephonyDisplayInfo, turboSS alive, skip notify");
        } else {
            super.updateTelephonyDisplayInfo();
        }
    }
}
