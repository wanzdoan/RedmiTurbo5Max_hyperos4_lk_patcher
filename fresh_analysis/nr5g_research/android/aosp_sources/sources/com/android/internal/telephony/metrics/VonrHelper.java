package com.android.internal.telephony.metrics;

import android.os.Handler;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.Phone;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class VonrHelper {
    private final FeatureFlags mFlags;
    private Map mPhoneVonrState = new ConcurrentHashMap();
    protected Runnable mVonrRunnable = new Runnable() { // from class: com.android.internal.telephony.metrics.VonrHelper.1
        @Override // java.lang.Runnable
        public void run() {
            VonrHelper.this.mPhoneVonrState.clear();
            for (Phone phone : MetricsCollector.getPhonesIfAny()) {
                VonrHelper.this.mPhoneVonrState.put(Integer.valueOf(phone.getSubId()), Boolean.valueOf(PerSimStatus.isVonrEnabled(phone)));
            }
        }
    };
    private Handler mHandler = new Handler(BackgroundThread.get().getLooper());

    public VonrHelper(FeatureFlags featureFlags) {
        this.mFlags = featureFlags;
    }

    public void updateVonrEnabledState() {
        this.mHandler.post(this.mVonrRunnable);
    }

    public boolean getVonrEnabled(int i) {
        return ((Boolean) this.mPhoneVonrState.getOrDefault(Integer.valueOf(i), Boolean.FALSE)).booleanValue();
    }
}
