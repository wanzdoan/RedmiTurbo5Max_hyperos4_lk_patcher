package com.android.phone.utils;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;

/* JADX INFO: loaded from: classes.dex */
public class NetworkSpeedMonitor {
    private static NetworkSpeedMonitor sInstance;
    private Handler mBgHandler;
    private Context mContext;
    private long mFirstTime;
    private long mLastTime;
    private long mTotalBytes;
    private int mTotalCheckCount;

    public static NetworkSpeedMonitor getInstance() {
        return sInstance;
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new NetworkSpeedMonitor(context);
        }
    }

    private NetworkSpeedMonitor(Context context) {
        this.mContext = context;
    }

    public void startMonitor(Looper looper) {
        if (this.mBgHandler == null) {
            this.mBgHandler = new WorkHandler(looper);
            postUpdateNetworkSpeed();
        }
    }

    public void stopMonitor() {
        log("stop speed monitor.");
        Handler handler = this.mBgHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.mBgHandler = null;
        }
        this.mFirstTime = 0L;
        this.mLastTime = 0L;
        this.mTotalBytes = 0L;
        this.mTotalCheckCount = 0;
    }

    private long getTotalByte() {
        return TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
    }

    private void postUpdateNetworkSpeed() {
        postUpdateNetworkSpeedDelay(0L);
    }

    private void postUpdateNetworkSpeedDelay(long j) {
        Handler handler = this.mBgHandler;
        if (handler != null) {
            handler.removeMessages(0);
            this.mBgHandler.sendEmptyMessageDelayed(0, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:16:0x0037  */
    public void updateNetworkSpeed() {
        long j;
        long jCurrentTimeMillis = System.currentTimeMillis();
        long totalByte = getTotalByte();
        if (totalByte == 0) {
            this.mLastTime = 0L;
            this.mTotalBytes = 0L;
            totalByte = getTotalByte();
        }
        long j2 = this.mLastTime;
        if (j2 == 0 || jCurrentTimeMillis <= j2) {
            j = 0;
        } else {
            long j3 = this.mTotalBytes;
            if (j3 == 0 || totalByte == 0 || totalByte <= j3) {
                j = 0;
            } else {
                j = ((totalByte - j3) * 1000) / 2000;
            }
        }
        this.mLastTime = jCurrentTimeMillis;
        this.mTotalBytes = totalByte;
        long j4 = this.mFirstTime;
        boolean z = false;
        if (j4 != 0) {
            z = j >= 1048576;
            this.mTotalCheckCount++;
        }
        if (j4 == 0) {
            this.mFirstTime = jCurrentTimeMillis;
        }
        log("update network speed, currentSpeed: " + j + ", totalCheckCount: " + this.mTotalCheckCount);
        if (z || this.mTotalCheckCount >= 5) {
            notifyNetworkSpeedListener(!z);
            stopMonitor();
        } else {
            postUpdateNetworkSpeedDelay(2000L);
        }
    }

    final class WorkHandler extends Handler {
        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 0) {
                return;
            }
            NetworkSpeedMonitor.this.updateNetworkSpeed();
        }
    }

    private void notifyNetworkSpeedListener(boolean z) {
        log("notify speed listener, isLowSpeed: " + z);
        Bundle bundle = new Bundle();
        bundle.putBoolean("low_speed", z);
        EventHubManager.getInstance().postEvent(new TelephonyEvent.DataEvent("continual_low_network_speed", bundle));
    }

    private void log(String str) {
        Log.d("NetworkSpeedMonitor", str);
    }
}
