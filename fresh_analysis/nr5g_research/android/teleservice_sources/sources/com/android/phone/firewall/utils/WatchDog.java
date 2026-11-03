package com.android.phone.firewall.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.android.services.telephony.relay.phonecall.RelayUtils;

/* JADX INFO: loaded from: classes.dex */
public class WatchDog {
    private final Handler mHandler;
    private final HandlerThread mThread;

    public interface Action {
        void doAction();
    }

    abstract class Holder {
        private static final WatchDog sInstance = new WatchDog();
    }

    private WatchDog() {
        HandlerThread handlerThread = new HandlerThread("PhoneWatchDogX");
        this.mThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.firewall.utils.WatchDog.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                try {
                    if (message.what != 1) {
                        return;
                    }
                    Log.d("PhoneWatchDogX", "handleMessage EVENT_NETD_TIMEOUT obj=" + message.obj);
                    Object obj = message.obj;
                    if (!(obj instanceof Action)) {
                        Log.w("PhoneWatchDogX", "handleMessage wrong argument");
                    } else {
                        ((Action) obj).doAction();
                    }
                } catch (Exception e) {
                    Log.w("PhoneWatchDogX", "handleMessage exception", e);
                }
            }
        };
    }

    public static WatchDog get() {
        return Holder.sInstance;
    }

    public void start(int i, Action action) {
        Log.d("PhoneWatchDogX", "startWatch what=" + i + " token=" + action);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(i, action), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
    }

    public void cancel(int i, Object obj) {
        Log.d("PhoneWatchDogX", "cancelWatch what=" + i + " token=" + obj);
        this.mHandler.removeMessages(i, obj);
    }
}
