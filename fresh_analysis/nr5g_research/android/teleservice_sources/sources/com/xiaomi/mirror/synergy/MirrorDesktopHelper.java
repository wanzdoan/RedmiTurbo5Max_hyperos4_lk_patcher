package com.xiaomi.mirror.synergy;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import com.xiaomi.mirror.SystemSettingsUtils;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class MirrorDesktopHelper implements IBinder.DeathRecipient {
    public static final long HANDLER_TIMEOUT_MILLIS = 1000;
    public static final int MSG_MIRROR_DESKTOP_CONFIG_CHANGED = 905;
    public static final int MSG_MIRROR_DESKTOP_ICON_DISABLE = 907;
    public static final int MSG_MIRROR_DESKTOP_OPEN_OR_CLOSE = 906;
    public static final int MSG_MIRROR_DESKTOP_SHOW_OR_HIDE = 903;
    public static final int MSG_MIRROR_DEVICE_LIST_CLEAR = 902;
    public static final int MSG_MIRROR_DEVICE_LIST_UPDATE = 901;
    public static final int MSG_MIRROR_SEAT_CHANGED = 904;
    private static final String TAG = "MirrorDesktopHelper";
    public static final long TIMEOUT_MILLIS = 5000;
    private IBinder mAliveBinder;
    private ContentObserver mConfigurationObserver;
    private SoftReference mContextRef;
    private ContentObserver mDesktopIconObserver;
    private ContentObserver mDesktopOpenObserver;
    private ContentObserver mDesktopVisibilityObserver;
    private ContentObserver mDeviceUpdateObserver;
    private Handler mHandler;
    private MirrorDesktopCallback mMirrorDesktopCallback;
    private ContentObserver mSeatObserver;
    private static final Uri URI_MIRROR_DESKTOP_VISIBILITY = MiuiSynergySdk.getUriFor("mirror_desktop_device_visibility");
    private static final Uri URI_MIRROR_OPEN_OR_CLOSE = MiuiSynergySdk.getUriFor("mirror_open_or_close");
    private static final Uri URI_MIRROR_DEVICE_UPDATE = MiuiSynergySdk.getUriFor("mirror_device_update");
    private static final Uri URI_MIRROR_CONFIGURATION = MiuiSynergySdk.getUriFor("mirror_configuration");
    private static final Uri URI_MIRROR_SETTINGS_SEAT = Settings.Secure.getUriFor(SystemSettingsUtils.KEY_MIRROR_SEAT);
    private static final Uri URI_MIRROR_DESKTOP_ICON = MiuiSynergySdk.getUriFor("mirror_desktop_icon");
    private final LinkedList mMessageQueue = new LinkedList();
    private volatile boolean mIsRegister = false;
    private Runnable mRunnable = new Runnable() { // from class: com.xiaomi.mirror.synergy.MirrorDesktopHelper.1
        @Override // java.lang.Runnable
        public void run() {
            MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_DEVICE_LIST_UPDATE);
            MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_DESKTOP_SHOW_OR_HIDE);
            MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_SEAT_CHANGED);
            MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_DESKTOP_CONFIG_CHANGED);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRemoteProcess() {
        SoftReference softReference;
        if (this.mAliveBinder != null || (softReference = this.mContextRef) == null || softReference.get() == null) {
            return;
        }
        IBinder aliveBinder = MiuiSynergySdk.getInstance().getAliveBinder((Context) this.mContextRef.get());
        this.mAliveBinder = aliveBinder;
        if (aliveBinder != null) {
            try {
                aliveBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void enqueueMessage(int i) {
        Iterator it = this.mMessageQueue.iterator();
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (((Long) pair.second).longValue() + TIMEOUT_MILLIS < jCurrentTimeMillis || i == ((Integer) pair.first).intValue()) {
                it.remove();
            }
            if (i == 901 || i == 902) {
                if (((Long) pair.second).longValue() == 901 || ((Long) pair.second).longValue() == 902) {
                    it.remove();
                }
            }
        }
        this.mMessageQueue.addLast(new Pair(Integer.valueOf(i), Long.valueOf(jCurrentTimeMillis)));
    }

    private void handleMessage(int i) {
        if (isContextValid()) {
            switch (i) {
                case MSG_MIRROR_DEVICE_LIST_UPDATE /* 901 */:
                    mirrorDeviceListUpdate();
                    break;
                case MSG_MIRROR_DEVICE_LIST_CLEAR /* 902 */:
                    mirrorDeviceListClear();
                    break;
                case MSG_MIRROR_DESKTOP_SHOW_OR_HIDE /* 903 */:
                    mirrorDesktopShowOrHide();
                    break;
                case MSG_MIRROR_SEAT_CHANGED /* 904 */:
                    mirrorSeatChanged();
                    break;
                case MSG_MIRROR_DESKTOP_CONFIG_CHANGED /* 905 */:
                    mirrorDesktopConfigChanged();
                    break;
                case MSG_MIRROR_DESKTOP_OPEN_OR_CLOSE /* 906 */:
                    mirrorDesktopOpenOrClose();
                    break;
                case MSG_MIRROR_DESKTOP_ICON_DISABLE /* 907 */:
                    mirrorDesktopIconDisable();
                    break;
            }
        }
    }

    private void handleQueue() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (!this.mMessageQueue.isEmpty()) {
            Pair pair = (Pair) this.mMessageQueue.poll();
            if (((Long) pair.second).longValue() + TIMEOUT_MILLIS >= jCurrentTimeMillis) {
                handleMessage(((Integer) pair.first).intValue());
            }
        }
    }

    private boolean isContextValid() {
        SoftReference softReference = this.mContextRef;
        return (softReference == null || softReference.get() == null) ? false : true;
    }

    private boolean isDesktopCallbackValid() {
        boolean z = this.mIsRegister && this.mMirrorDesktopCallback != null;
        Log.i(TAG, "isDesktopCallbackValid result = " + z);
        return z;
    }

    private void mirrorDesktopConfigChanged() {
        Log.i(TAG, "mirrorDesktopConfigChanged()  ");
        if (this.mMirrorDesktopCallback == null) {
            Log.i(TAG, "mirrorDesktopConfigChanged()  mMirrorDesktopCallback is null");
        } else {
            this.mMirrorDesktopCallback.onMirrorDesktopConfigChanged(MiuiSynergySdk.getInstance().queryMirrorDesktopInfo((Context) this.mContextRef.get()));
        }
    }

    private void mirrorDesktopIconDisable() {
        Log.i(TAG, "mirrorDesktopIconDisable");
        if (this.mMirrorDesktopCallback == null) {
            Log.i(TAG, "mirrorDesktopIconDisable mMirrorDesktopCallback is null");
        } else {
            this.mMirrorDesktopCallback.onMirrorDesktopIconChanged(MiuiSynergySdk.getInstance().getInt((Context) this.mContextRef.get(), URI_MIRROR_DESKTOP_ICON, 0) == 1);
        }
    }

    private void mirrorDesktopOpenOrClose() {
        Log.i(TAG, "mirrorDesktopOpenOrClose");
        if (this.mMirrorDesktopCallback == null) {
            Log.i(TAG, "mirrorDesktopOpenOrClose mMirrorDesktopCallback is null");
        } else {
            this.mMirrorDesktopCallback.onMirrorDeskOpenOrClose(MiuiSynergySdk.getInstance().getInt((Context) this.mContextRef.get(), URI_MIRROR_OPEN_OR_CLOSE, 0) == 1);
        }
    }

    private void mirrorDesktopShowOrHide() {
        Log.i(TAG, "mirrorDesktopShowOrHide()  ");
        if (this.mMirrorDesktopCallback == null) {
            Log.i(TAG, "mirrorDesktopShowOrHide()  mMirrorDesktopCallback is null");
        } else if (MiuiSynergySdk.getInstance().getInt((Context) this.mContextRef.get(), URI_MIRROR_DESKTOP_VISIBILITY, 0) == 0) {
            this.mMirrorDesktopCallback.onMirrorDesktopHide();
        } else {
            this.mMirrorDesktopCallback.onMirrorDesktopShow(MiuiSynergySdk.getInstance().queryMirrorDesktopInfo((Context) this.mContextRef.get()));
        }
    }

    private void mirrorDeviceListClear() {
        Log.i(TAG, "mirrorDeviceListClear");
        MirrorDesktopCallback mirrorDesktopCallback = this.mMirrorDesktopCallback;
        if (mirrorDesktopCallback != null) {
            mirrorDesktopCallback.onDeviceListUpdate(null);
        } else {
            Log.i(TAG, "mirrorDeviceListClear mMirrorDesktopCallback is null");
        }
    }

    private void mirrorDeviceListUpdate() {
        String str;
        Log.i(TAG, "mirrorDeviceListUpdate");
        List listQueryMirrorDesktopDevices = MiuiSynergySdk.getInstance().queryMirrorDesktopDevices((Context) this.mContextRef.get());
        if (listQueryMirrorDesktopDevices == null) {
            str = "null";
        } else {
            str = "size= " + listQueryMirrorDesktopDevices.size();
        }
        Log.i(TAG, "mirrorDeviceListUpdate deviceInfoList: ".concat(str));
        MirrorDesktopCallback mirrorDesktopCallback = this.mMirrorDesktopCallback;
        if (mirrorDesktopCallback != null) {
            mirrorDesktopCallback.onDeviceListUpdate(listQueryMirrorDesktopDevices);
        } else {
            Log.i(TAG, "mirrorDeviceListUpdate mMirrorDesktopCallback is null");
        }
    }

    private void mirrorSeatChanged() {
        Log.i(TAG, "mirrorSeatChanged()  ");
        if (this.mMirrorDesktopCallback == null) {
            Log.i(TAG, "mirrorSeatChanged()  mMirrorDesktopCallback is null");
        } else {
            this.mMirrorDesktopCallback.onMirrorSeatChanged(SystemSettingsUtils.getInt((Context) this.mContextRef.get(), SystemSettingsUtils.KEY_MIRROR_SEAT, 0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessageToCallback(int i) {
        synchronized (this.mMessageQueue) {
            try {
                if (isDesktopCallbackValid()) {
                    handleQueue();
                    handleMessage(i);
                } else {
                    enqueueMessage(i);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void unlinkToDeath() {
        IBinder iBinder = this.mAliveBinder;
        if (iBinder != null) {
            iBinder.unlinkToDeath(this, 0);
            this.mAliveBinder = null;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        sendMessageToCallback(MSG_MIRROR_DEVICE_LIST_CLEAR);
        unlinkToDeath();
    }

    public void performExitMirrorClick(Context context) {
        try {
            CallMethod.doCall(context.getContentResolver(), "performExitMirrorClick", null, new Bundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performMirrorDeviceIconClick(Context context, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("extra", str);
        try {
            CallMethod.doCall(context.getContentResolver(), "performMirrorDeviceIconClick", null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerMirrorDesktopCallback(Context context, MirrorDesktopCallback mirrorDesktopCallback, Handler handler) {
        Log.i(TAG, "registerMirrorDesktopCallback() ");
        Handler handler2 = this.mHandler;
        if (handler2 != null) {
            handler2.removeCallbacks(this.mRunnable);
        }
        this.mHandler = handler;
        handler.postDelayed(this.mRunnable, 1000L);
        if (mirrorDesktopCallback == null) {
            this.mMirrorDesktopCallback = null;
            this.mContextRef = null;
            return;
        }
        synchronized (this.mMessageQueue) {
            this.mContextRef = new SoftReference(context);
            this.mMirrorDesktopCallback = mirrorDesktopCallback;
            this.mIsRegister = true;
            this.mDeviceUpdateObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.MirrorDesktopHelper.2
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_DEVICE_LIST_UPDATE);
                    MirrorDesktopHelper.this.checkRemoteProcess();
                }
            };
            context.getContentResolver().registerContentObserver(URI_MIRROR_DEVICE_UPDATE, false, this.mDeviceUpdateObserver);
            this.mDesktopVisibilityObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.MirrorDesktopHelper.3
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    Log.i(MirrorDesktopHelper.TAG, "registerMirrorDesktopCallback() mDesktopVisibilityObserver onChange ");
                    MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_DESKTOP_SHOW_OR_HIDE);
                }
            };
            context.getContentResolver().registerContentObserver(URI_MIRROR_DESKTOP_VISIBILITY, false, this.mDesktopVisibilityObserver);
            this.mDesktopOpenObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.MirrorDesktopHelper.4
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_DESKTOP_OPEN_OR_CLOSE);
                }
            };
            context.getContentResolver().registerContentObserver(URI_MIRROR_OPEN_OR_CLOSE, false, this.mDesktopOpenObserver);
            this.mConfigurationObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.MirrorDesktopHelper.5
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_DESKTOP_CONFIG_CHANGED);
                }
            };
            context.getContentResolver().registerContentObserver(URI_MIRROR_CONFIGURATION, false, this.mConfigurationObserver);
            this.mSeatObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.MirrorDesktopHelper.6
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_SEAT_CHANGED);
                }
            };
            context.getContentResolver().registerContentObserver(URI_MIRROR_SETTINGS_SEAT, false, this.mSeatObserver);
            this.mDesktopIconObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.MirrorDesktopHelper.7
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    super.onChange(z);
                    MirrorDesktopHelper.this.sendMessageToCallback(MirrorDesktopHelper.MSG_MIRROR_DESKTOP_ICON_DISABLE);
                }
            };
            context.getContentResolver().registerContentObserver(URI_MIRROR_DESKTOP_ICON, false, this.mDesktopIconObserver);
        }
        checkRemoteProcess();
    }

    public void unRegisterMirrorDesktopCallback(Context context) {
        synchronized (this.mMessageQueue) {
            try {
                this.mIsRegister = false;
                this.mMirrorDesktopCallback = null;
                this.mContextRef = null;
                if (this.mDeviceUpdateObserver != null) {
                    context.getContentResolver().unregisterContentObserver(this.mDeviceUpdateObserver);
                    this.mDeviceUpdateObserver = null;
                }
                if (this.mDesktopVisibilityObserver != null) {
                    context.getContentResolver().unregisterContentObserver(this.mDesktopVisibilityObserver);
                    this.mDesktopVisibilityObserver = null;
                }
                if (this.mConfigurationObserver != null) {
                    context.getContentResolver().unregisterContentObserver(this.mConfigurationObserver);
                    this.mConfigurationObserver = null;
                }
                if (this.mDesktopOpenObserver != null) {
                    context.getContentResolver().unregisterContentObserver(this.mDesktopOpenObserver);
                    this.mDesktopOpenObserver = null;
                }
                if (this.mSeatObserver != null) {
                    context.getContentResolver().unregisterContentObserver(this.mSeatObserver);
                    this.mSeatObserver = null;
                }
                if (this.mDesktopIconObserver != null) {
                    context.getContentResolver().unregisterContentObserver(this.mDesktopIconObserver);
                    this.mDesktopIconObserver = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mRunnable);
        }
        unlinkToDeath();
    }
}
