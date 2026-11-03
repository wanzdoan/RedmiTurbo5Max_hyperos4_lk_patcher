package com.xiaomi.mirror.synergy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.ot.pubsub.a.a;
import com.ot.pubsub.g.f;

/* JADX INFO: loaded from: classes2.dex */
public class RemoteBroadcastManager {
    private static final String PERMISSION_RECEIVE_REMOTE_BROADCAST = "com.xiaomi.mirror.permission.RECEIVE_REMOTE_BROADCAST";
    private static final String PERMISSION_SEND_REMOTE_BROADCAST = "com.xiaomi.mirror.permission.SEND_REMOTE_BROADCAST";
    private static RemoteBroadcastManager mInstance;
    private static final Object mLock = new Object();
    private final Context mAppContext;

    private RemoteBroadcastManager(Context context) {
        this.mAppContext = context;
    }

    public static RemoteBroadcastManager getInstance(Context context) {
        RemoteBroadcastManager remoteBroadcastManager;
        synchronized (mLock) {
            try {
                if (mInstance == null) {
                    mInstance = new RemoteBroadcastManager(context.getApplicationContext());
                }
                remoteBroadcastManager = mInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return remoteBroadcastManager;
    }

    public int sendBroadcast(Intent intent) {
        return sendBroadcastAsDevice(intent, null);
    }

    public int sendBroadcastAsDevice(Intent intent, String str) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("intent", intent);
        bundle.putString(f.a.e, this.mAppContext.getPackageName());
        Bundle bundleDoCall = CallMethod.doCall(this.mAppContext.getContentResolver(), "sendRemoteBroadcast", str, bundle);
        if (bundleDoCall != null) {
            return bundleDoCall.getInt(a.L, -1);
        }
        return -1;
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        this.mAppContext.unregisterReceiver(broadcastReceiver);
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        this.mAppContext.registerReceiver(broadcastReceiver, intentFilter, PERMISSION_SEND_REMOTE_BROADCAST, null, 2);
    }
}
