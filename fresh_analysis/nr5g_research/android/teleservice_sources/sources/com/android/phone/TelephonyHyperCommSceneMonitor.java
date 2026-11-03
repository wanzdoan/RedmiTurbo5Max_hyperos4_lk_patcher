package com.android.phone;

import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.SystemProperties;
import android.telephony.Rlog;
import java.util.concurrent.Executor;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyHyperCommSceneMonitor {
    private static volatile TelephonyHyperCommSceneMonitor sInstance;
    private volatile int mActiveSceneBitmask;
    private SceneCB mSceneCB;

    private int getSceneBit(int i) {
        switch (i) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                return 16;
            case 5:
                return 32;
            case 6:
                return 64;
            default:
                return 0;
        }
    }

    public static TelephonyHyperCommSceneMonitor getInstance() {
        if (sInstance == null) {
            synchronized (TelephonyHyperCommSceneMonitor.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new TelephonyHyperCommSceneMonitor();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private TelephonyHyperCommSceneMonitor() {
    }

    public synchronized void register(Handler handler) {
        if (handler == null) {
            Rlog.w("TelephonyHyperCommSceneMonitor", "register: handler is null, skip");
            return;
        }
        if (this.mSceneCB != null) {
            Rlog.w("TelephonyHyperCommSceneMonitor", "register: already registered, skip");
            return;
        }
        try {
            this.mSceneCB = new SceneCB(new HandlerExecutor(handler));
            HyperCommManager.getDefault().registerSceneAwareCallback(this.mSceneCB);
            Rlog.d("TelephonyHyperCommSceneMonitor", "register: success");
        } catch (Exception e) {
            this.mSceneCB = null;
            Rlog.w("TelephonyHyperCommSceneMonitor", "register failed: " + e);
        }
    }

    public synchronized int getActiveSceneBitmask() {
        updateScene(6, SystemProperties.getBoolean("gsm.game.scene", false));
        return this.mActiveSceneBitmask;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateScene(int i, boolean z) {
        try {
            int sceneBit = getSceneBit(i);
            if (sceneBit == 0) {
                return;
            }
            int i2 = this.mActiveSceneBitmask;
            if (z) {
                this.mActiveSceneBitmask = sceneBit | i2;
            } else {
                this.mActiveSceneBitmask = (~sceneBit) & i2;
            }
            Rlog.d("TelephonyHyperCommSceneMonitor", "updateScene scene=" + i + " isEnter=" + z + " activeSceneBitmask=" + this.mActiveSceneBitmask);
        } catch (Throwable th) {
            throw th;
        }
    }

    class SceneCB extends SceneAwareCallback implements SceneAwareCallback.HighSpeedTrainStateListener, SceneAwareCallback.SubwayStateListener, SceneAwareCallback.AirportStateListener, SceneAwareCallback.SchoolStateListener, SceneAwareCallback.VenueStateListener, SceneAwareCallback.PortStateListener, SceneAwareCallback.HighSpeedTrainStationListener {
        SceneCB(Executor executor) {
            super(executor);
        }

        public void onHighSpeedTrainStateChanged(boolean z) {
            Rlog.d("TelephonyHyperCommSceneMonitor", "onHighSpeedTrainStateChanged state=" + z);
            TelephonyHyperCommSceneMonitor.this.updateScene(2, z);
        }

        public void onSubwayStateChanged(boolean z, boolean z2) {
            Rlog.d("TelephonyHyperCommSceneMonitor", "onSubwayStateChanged state=" + z + " isDomestic=" + z2);
            TelephonyHyperCommSceneMonitor.this.updateScene(0, z);
        }

        public void onAirportStateChanged(String str, boolean z, boolean z2) {
            Rlog.d("TelephonyHyperCommSceneMonitor", "onAirportStateChanged name=" + str + " state=" + z + " isDomestic=" + z2);
            TelephonyHyperCommSceneMonitor.this.updateScene(1, z);
        }

        public void onSchoolStateChanged(String str, boolean z) {
            Rlog.d("TelephonyHyperCommSceneMonitor", "onSchoolStateChanged name=" + str + " state=" + z);
            TelephonyHyperCommSceneMonitor.this.updateScene(3, z);
        }

        public void onVenueStateChanged(String str, boolean z, boolean z2) {
            Rlog.d("TelephonyHyperCommSceneMonitor", "onVenueStateChanged name=" + str + " state=" + z + " isDomestic=" + z2);
            TelephonyHyperCommSceneMonitor.this.updateScene(4, z);
        }

        public void onPortPoiChanged(String str, boolean z, boolean z2) {
            Rlog.d("TelephonyHyperCommSceneMonitor", "onPortPoiChanged name=" + str + " state=" + z + " isDomestic=" + z2);
            TelephonyHyperCommSceneMonitor.this.updateScene(5, z);
        }

        public void onHighSpeedTrainStationChanged(String str, boolean z, boolean z2) {
            Rlog.d("TelephonyHyperCommSceneMonitor", "onHighSpeedTrainStationChanged name=" + str + " isEnter=" + z + " isDomestic=" + z2);
            TelephonyHyperCommSceneMonitor.this.updateScene(2, z);
        }
    }
}
