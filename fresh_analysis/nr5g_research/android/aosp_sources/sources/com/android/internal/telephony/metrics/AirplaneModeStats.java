package com.android.internal.telephony.metrics;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class AirplaneModeStats extends ContentObserver {
    private static final String TAG = AirplaneModeStats.class.getSimpleName();
    private final Uri mAirplaneModeSettingUri;
    private final Context mContext;
    private long mLastActivationTime;

    public AirplaneModeStats(Context context) {
        super(new Handler(Looper.getMainLooper()));
        this.mLastActivationTime = 0L;
        this.mContext = context;
        Uri uriFor = Settings.Global.getUriFor("airplane_mode_on");
        this.mAirplaneModeSettingUri = uriFor;
        context.getContentResolver().registerContentObserver(uriFor, false, this);
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z, Uri uri) {
        if (uri.equals(this.mAirplaneModeSettingUri)) {
            onAirplaneModeChanged(isAirplaneModeOn());
        }
    }

    private boolean isAirplaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    private void onAirplaneModeChanged(boolean z) {
        String str = TAG;
        Rlog.d(str, "Airplane mode change. Value: " + z);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime < SatelliteSessionController.DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS) {
            return;
        }
        boolean zCalculateShortToggle = calculateShortToggle(jElapsedRealtime, z);
        int carrierId = getCarrierId();
        Rlog.d(str, "Airplane mode: " + z + ", short=" + zCalculateShortToggle + ", carrierId=" + carrierId);
        TelephonyStatsLog.write(311, z, zCalculateShortToggle, carrierId);
    }

    private boolean calculateShortToggle(long j, boolean z) {
        long j2 = this.mLastActivationTime;
        if (z) {
            if (j2 == 0) {
                this.mLastActivationTime = j;
            }
            return false;
        }
        long j3 = j - j2;
        this.mLastActivationTime = 0L;
        return j3 > 0 && j3 < 10000;
    }

    private static int getCarrierId() {
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        Phone phone = PhoneFactory.getPhone(activeDataSubscriptionId != -1 ? SubscriptionManager.getPhoneId(activeDataSubscriptionId) : 0);
        if (phone != null) {
            return phone.getCarrierId();
        }
        return -1;
    }
}
