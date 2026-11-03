package com.android.internal.telephony.datasub;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public class SpeedRateEstimate {
    private static final int EVENT_UPDATE_NETWORK_SPEED = 0;
    static final String LOG_TAG = "SpeedRateEstimate";
    private static final int LOW_SPEED_THRESHOLD = 12800;
    private static final int MAX_CHECK_COUNT = 3;
    private static final int MSG_NETWORK_UPDATE_INTERVAL = 2000;
    private static SpeedRateEstimate sInstance;
    private Handler mBgHandler;
    private Context mContext;
    private HashSet<ISpeedRateEstimateListener> mNetworkSpeedListeners = new HashSet<>();
    private int mTotalCheckCount = 0;
    private SpeedStats mSpeedStats = new SpeedStats(this);

    public interface ISpeedRateEstimateListener {
        void onSpeedRateChanged(boolean z);
    }

    public static SpeedRateEstimate getInstance() {
        return sInstance;
    }

    public static synchronized SpeedRateEstimate init(Context context) {
        if (sInstance == null) {
            sInstance = new SpeedRateEstimate(context);
        }
        return sInstance;
    }

    private SpeedRateEstimate(Context context) {
        this.mContext = context;
    }

    public class SpeedStats {
        public long rxBytes;
        public long time;
        public long txBytes;

        public SpeedStats(SpeedRateEstimate this$0) {
            clear();
        }

        public void updateTxRx() {
            this.txBytes = TrafficStats.getMobileTxBytes();
            this.rxBytes = TrafficStats.getMobileRxBytes();
            this.time = System.currentTimeMillis();
        }

        public SpeedStats(SpeedRateEstimate this$0, SpeedStats ns) {
            this.txBytes = ns.txBytes;
            this.rxBytes = ns.rxBytes;
            this.time = ns.time;
        }

        public void clear() {
            this.txBytes = 0L;
            this.rxBytes = 0L;
            this.time = 0L;
        }

        public String toString() {
            return "txBytes=" + this.txBytes + " rxBytes=" + this.rxBytes + " time=" + this.time;
        }
    }

    public void startMonitor(Looper looper) {
        if (this.mBgHandler == null) {
            this.mBgHandler = new WorkHandler(looper);
            postUpdateNetworkSpeed();
        }
    }

    public void stopMonitor() {
        log("stop SpeedRateEstimate monitor.");
        if (this.mBgHandler != null) {
            this.mBgHandler.removeCallbacksAndMessages(null);
            this.mBgHandler = null;
        }
        this.mSpeedStats.clear();
        this.mTotalCheckCount = 0;
    }

    private void postUpdateNetworkSpeed() {
        postUpdateNetworkSpeedDelay(0L);
    }

    private void postUpdateNetworkSpeedDelay(long delay) {
        if (this.mBgHandler != null) {
            this.mBgHandler.removeMessages(0);
            this.mBgHandler.sendEmptyMessageDelayed(0, delay);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkSpeed() {
        boolean highSpeed = false;
        SpeedStats preSpeedStats = new SpeedStats(this, this.mSpeedStats);
        this.mSpeedStats.updateTxRx();
        long preTotalBytes = preSpeedStats.txBytes + preSpeedStats.rxBytes;
        long curTotalBytes = this.mSpeedStats.txBytes + this.mSpeedStats.rxBytes;
        if (preSpeedStats.time != 0 && this.mSpeedStats.time > preSpeedStats.time && preTotalBytes != 0 && curTotalBytes != 0 && curTotalBytes > preTotalBytes) {
            long currentSpeed = ((curTotalBytes - preTotalBytes) * 1000) / 2000;
            if (currentSpeed >= 12800) {
                highSpeed = true;
            }
        }
        this.mTotalCheckCount++;
        if (highSpeed || this.mTotalCheckCount >= 3) {
            notifySpeedRateEstimateListener(!highSpeed);
            stopMonitor();
        } else {
            postUpdateNetworkSpeedDelay(2000L);
        }
    }

    private final class WorkHandler extends Handler {
        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SpeedRateEstimate.this.updateNetworkSpeed();
                    break;
            }
        }
    }

    public void addSpeedRateEstimateListener(ISpeedRateEstimateListener listener) {
        this.mNetworkSpeedListeners.add(listener);
    }

    public void removeSpeedRateEstimateListener(ISpeedRateEstimateListener listener) {
        this.mNetworkSpeedListeners.remove(listener);
    }

    private void notifySpeedRateEstimateListener(boolean isLowSpeed) {
        log("notify notifyNetworkSpeedListener, isLowSpeed: " + isLowSpeed);
        for (ISpeedRateEstimateListener listener : this.mNetworkSpeedListeners) {
            listener.onSpeedRateChanged(isLowSpeed);
        }
    }

    private void log(String msg) {
        Log.d(LOG_TAG, msg);
    }
}
