package com.android.phone.beidou;

import android.os.Binder;
import android.os.SystemClock;
import android.util.Log;
import com.android.phone.FiveGCloudController;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class BdSmsStatistics {
    public static final String TAG = "BdSmsStatistics";
    private static BdSmsStatistics mBdSmsStatistics;
    private Map mBdSmsStatisticsMap;
    private BdStatusListener mBdStatusListener;
    private Map mBdUpdateAuthKeyStatisticsMap;
    private Map mBdUpdateEphDataStatisticsMap;
    private long mStartTime = 0;
    private long mEndTime = 0;

    private BdSmsStatistics() {
        this.mBdStatusListener = null;
        this.mBdSmsStatisticsMap = null;
        this.mBdUpdateAuthKeyStatisticsMap = null;
        this.mBdUpdateEphDataStatisticsMap = null;
        this.mBdSmsStatisticsMap = new HashMap();
        this.mBdUpdateAuthKeyStatisticsMap = new HashMap();
        this.mBdUpdateEphDataStatisticsMap = new HashMap();
        BdStatusListener bdStatusListener = new BdStatusListener();
        this.mBdStatusListener = bdStatusListener;
        BdSmsController.registerBdStatusListener(bdStatusListener);
    }

    public static void init() {
        if (mBdSmsStatistics == null) {
            mBdSmsStatistics = new BdSmsStatistics();
        }
        Log.d(TAG, "init");
    }

    public void reportBdSmsStatistics() {
        Log.d(TAG, "reportBdSmsStatistics");
        this.mBdSmsStatisticsMap.put("city", FiveGCloudController.getInstance().getCity());
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "bd_sms_statistics", this.mBdSmsStatisticsMap);
        this.mBdSmsStatisticsMap.clear();
    }

    class BdStatusListener implements BdSmsController.IBdStatusListener {
        BdStatusListener() {
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSysStatus(int i, int i2) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            if (i == 1) {
                BdSmsStatistics.this.mStartTime = SystemClock.elapsedRealtime();
            } else if (i == 2) {
                BdSmsStatistics.this.mBdSmsStatisticsMap.clear();
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSatelliteStatus(int i, int i2, int i3, int i4, int i5) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            Log.d(BdSmsStatistics.TAG, "notifyBdSatelliteStatus");
            BdSmsStatistics.this.mBdSmsStatisticsMap.put("azimuth", String.valueOf(i));
            BdSmsStatistics.this.mBdSmsStatisticsMap.put("pitch_angle", String.valueOf(i2));
            BdSmsStatistics.this.mBdSmsStatisticsMap.put("cnr", String.valueOf(i3));
            BdSmsStatistics.this.mBdSmsStatisticsMap.put("sateIdx", String.valueOf(i4));
            BdSmsStatistics.this.mBdSmsStatisticsMap.put("cnr0", String.valueOf(i5));
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSendSmsStatus(int i, int i2, int i3, int i4) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            int bdSmsMode = BdSmsController.getInstance().getBdSmsMode();
            Log.d(BdSmsStatistics.TAG, "notifyBdSendSmsStatus : bdSmsMode = " + bdSmsMode);
            if (bdSmsMode == 0 && i4 != 6) {
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("type", String.valueOf(0));
                BdSmsStatistics.this.mBdSmsStatisticsMap.put(a.J, String.valueOf(i3));
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("cause", String.valueOf(i4));
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("user_azimuth", String.valueOf(BDSatelliteMgr.getInstance().getPhoneAzimuth()));
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("user_pitch_angle", String.valueOf(BDSatelliteMgr.getInstance().getPhoneElevation()));
                BdSmsStatistics.this.mEndTime = SystemClock.elapsedRealtime();
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("process_time", String.valueOf((BdSmsStatistics.this.mEndTime - BdSmsStatistics.this.mStartTime) / 1000));
                BdSmsStatistics.this.reportBdSmsStatistics();
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdReceiveSmsStatus(boolean z, int i, String str, String str2) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            int bdSmsMode = BdSmsController.getInstance().getBdSmsMode();
            Log.d(BdSmsStatistics.TAG, "notifyBdReceiveSmsStatus : bdSmsMode = " + bdSmsMode);
            if (bdSmsMode == 1) {
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("type", String.valueOf(1));
                BdSmsStatistics.this.mBdSmsStatisticsMap.put(a.J, String.valueOf(z));
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("cause", String.valueOf(i));
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("user_azimuth", String.valueOf(BDSatelliteMgr.getInstance().getPhoneAzimuth()));
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("user_pitch_angle", String.valueOf(BDSatelliteMgr.getInstance().getPhoneElevation()));
                BdSmsStatistics.this.mEndTime = SystemClock.elapsedRealtime();
                BdSmsStatistics.this.mBdSmsStatisticsMap.put("process_time", String.valueOf((BdSmsStatistics.this.mEndTime - BdSmsStatistics.this.mStartTime) / 1000));
                BdSmsStatistics.this.reportBdSmsStatistics();
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdServiceStatus(boolean z, int i) {
            Binder.restoreCallingIdentity(Binder.clearCallingIdentity());
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyUpdateAuthKey(boolean z, int i) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            Log.d(BdSmsStatistics.TAG, "notifyUpdateAuthKey: result = " + z + ", cause = " + i);
            BdSmsStatistics.this.mBdUpdateAuthKeyStatisticsMap.put(a.J, String.valueOf(z));
            BdSmsStatistics.this.mBdUpdateAuthKeyStatisticsMap.put("cause", String.valueOf(i));
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "bd_update_auth_key_statistics", BdSmsStatistics.this.mBdUpdateAuthKeyStatisticsMap);
            BdSmsStatistics.this.mBdUpdateAuthKeyStatisticsMap.clear();
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyUpdateEphData(boolean z, int i) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            Log.d(BdSmsStatistics.TAG, "notifyUpdateEphData: result = " + z + ", cause = " + i);
            BdSmsStatistics.this.mBdUpdateEphDataStatisticsMap.put(a.J, String.valueOf(z));
            BdSmsStatistics.this.mBdUpdateEphDataStatisticsMap.put("cause", String.valueOf(i));
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "bd_update_eph_statistics", BdSmsStatistics.this.mBdUpdateEphDataStatisticsMap);
            BdSmsStatistics.this.mBdUpdateEphDataStatisticsMap.clear();
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }
}
