package com.android.internal.telephony.metrics;

import android.content.Context;
import android.hardware.devicestate.DeviceState;
import android.hardware.devicestate.DeviceStateManager;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStateHelper {
    private int mFoldState = 0;

    public DeviceStateHelper(Context context) {
        ((DeviceStateManager) context.getSystemService(DeviceStateManager.class)).registerCallback(BackgroundThread.getExecutor(), new DeviceStateManager.DeviceStateCallback() { // from class: com.android.internal.telephony.metrics.DeviceStateHelper$$ExternalSyntheticLambda0
            public final void onDeviceStateChanged(DeviceState deviceState) {
                this.f$0.lambda$new$0(deviceState);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceState deviceState) {
        updateFoldState(deviceState.getIdentifier());
    }

    private void updateFoldState(int i) {
        if (i == 0) {
            this.mFoldState = 1;
        } else if (i == 1) {
            this.mFoldState = 2;
        } else if (i == 2) {
            this.mFoldState = 3;
        } else if (i == 4) {
            this.mFoldState = 4;
        } else {
            this.mFoldState = 0;
        }
        updateServiceStateStats();
    }

    private void updateServiceStateStats() {
        for (Phone phone : MetricsCollector.getPhonesIfAny()) {
            phone.getServiceStateTracker().getServiceStateStats().onFoldStateChanged(this.mFoldState);
        }
    }

    public int getFoldState() {
        return this.mFoldState;
    }
}
