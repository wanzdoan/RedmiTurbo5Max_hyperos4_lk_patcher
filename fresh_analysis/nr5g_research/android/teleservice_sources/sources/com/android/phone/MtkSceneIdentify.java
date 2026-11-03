package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.utils.MiuiThreadUtil;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import miui.process.ForegroundInfo;
import miuix.animation.utils.EaseManager;

/* JADX INFO: loaded from: classes.dex */
public class MtkSceneIdentify {
    private static MtkSceneIdentify mInstance;
    private BroadcastReceiver mMtkSelfIdentifyReceiver;
    private int mSceneType = 0;
    private boolean mIsSingleHandApp = false;
    private boolean mIsDoubleHandGame = false;
    private final Handler mWorkHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.MtkSceneIdentify.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MtkSceneIdentify.log("handleMessage: " + message.what);
            int i = message.what;
            if (i == 200) {
                MtkSceneIdentify.this.processForgroundChange((ForegroundInfo) message.obj);
                return;
            }
            if (i != 201) {
                return;
            }
            int iIntValue = ((Integer) message.obj).intValue();
            MtkSceneIdentify.this.mSceneType = iIntValue;
            MtkSceneIdentify.this.mIsSingleHandApp = iIntValue == 1;
            MtkSceneIdentify.this.mIsDoubleHandGame = iIntValue == 2;
            MtkSceneIdentify.this.notifySceneTypeChanged(iIntValue);
        }
    };
    private final BaseEvent.IEventListener mForegroundInfoListener = new BaseEvent.IEventListener() { // from class: com.android.phone.MtkSceneIdentify.2
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            ForegroundInfo foregroundInfo;
            if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                return;
            }
            MtkSceneIdentify.this.mWorkHandler.sendMessageDelayed(MtkSceneIdentify.this.mWorkHandler.obtainMessage(200, foregroundInfo), 500L);
        }
    };
    private final Context mContext = PhoneGlobals.getInstance();

    public static MtkSceneIdentify make() {
        if (mInstance == null) {
            mInstance = new MtkSceneIdentify();
        }
        return mInstance;
    }

    private MtkSceneIdentify() {
        registerForegroundInfoListener();
        registerReceiver();
    }

    private void registerReceiver() {
        this.mMtkSelfIdentifyReceiver = new MtkSceneIdentifyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(this.mMtkSelfIdentifyReceiver, intentFilter, 2);
    }

    private void registerForegroundInfoListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mForegroundInfoListener);
    }

    class MtkSceneIdentifyReceiver extends BroadcastReceiver {
        MtkSceneIdentifyReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            if (intent == null) {
                return;
            }
            MtkSceneIdentify.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSceneIdentify$MtkSceneIdentifyReceiver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0(intent);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                boolean zIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
                MtkSceneIdentify mtkSceneIdentify = MtkSceneIdentify.this;
                if (zIsWifiConnected) {
                    if (mtkSceneIdentify.mSceneType != 0) {
                        MtkSceneIdentify.this.notifySceneTypeChanged(0);
                    }
                    MtkSceneIdentify.this.mSceneType = 0;
                } else if (mtkSceneIdentify.mIsDoubleHandGame || MtkSceneIdentify.this.mIsSingleHandApp) {
                    if (MtkSceneIdentify.this.mSceneType != (MtkSceneIdentify.this.mIsDoubleHandGame ? 2 : 1)) {
                        MtkSceneIdentify mtkSceneIdentify2 = MtkSceneIdentify.this;
                        mtkSceneIdentify2.notifySceneTypeChanged(mtkSceneIdentify2.mIsDoubleHandGame ? 2 : 1);
                        MtkSceneIdentify mtkSceneIdentify3 = MtkSceneIdentify.this;
                        mtkSceneIdentify3.mSceneType = mtkSceneIdentify3.mIsDoubleHandGame ? 2 : 1;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processForgroundChange(ForegroundInfo foregroundInfo) {
        int i;
        if (foregroundInfo == null || MiuiPhoneUtils.isWifiConnected()) {
            return;
        }
        String str = foregroundInfo.mForegroundPackageName;
        if (MiuiPhoneUtils.isContainsPackage("config_single_hand_app", str)) {
            i = 1;
        } else {
            i = MiuiPhoneUtils.isContainsPackage("config_double_hand_app", str) ? 2 : 0;
        }
        if (this.mSceneType != i) {
            if (this.mWorkHandler.hasMessages(EaseManager.EaseStyleDef.PERLIN)) {
                this.mWorkHandler.removeMessages(EaseManager.EaseStyleDef.PERLIN);
            }
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(EaseManager.EaseStyleDef.PERLIN, Integer.valueOf(i)), MirrorDesktopHelper.TIMEOUT_MILLIS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySceneTypeChanged(int i) {
        String[] strArr = {"AT+EDMFAPP=20102,3," + i, "+EDMFAPP"};
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return;
        }
        FiveGManagerBase.getInstance().atCmdRunSync(activeDataPhone.getPhoneId(), strArr, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("MtkSceneIdentify", str);
    }
}
