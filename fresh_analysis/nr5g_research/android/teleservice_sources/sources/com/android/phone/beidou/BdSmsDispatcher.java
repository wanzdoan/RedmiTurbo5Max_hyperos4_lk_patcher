package com.android.phone.beidou;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.SmsApplication;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;

/* JADX INFO: loaded from: classes.dex */
public class BdSmsDispatcher {
    private static final long SENT_BD_SMS_INTENT_DELAY_TIMER = 2000;
    protected Context mContext;
    protected BdSmsDispatcherHandler mHandler = new BdSmsDispatcherHandler(Looper.getMainLooper());

    protected BdSmsDispatcher(Context context) {
        this.mContext = context;
    }

    protected void dispatchBdSmsSendIntent(int[] iArr, int i, int i2, int i3, int i4, boolean z) {
        dispatchBDSmsIntentDelay(createSendIntent(iArr, i, i2, i3, i4, z));
    }

    protected void dispatchBdSmsReceiveIntent(boolean z, int i, String str, String str2, int i2) {
        dispatchBDSmsIntentDelay(createReceiveIntent(z, i, str, str2, i2));
    }

    protected void dispatchFailBdSmsSendIntent(int[] iArr, int i, int i2, int i3, boolean z) {
        BdSmsDispatcher bdSmsDispatcher;
        Intent intentCreateSendIntent;
        if (i3 <= 4) {
            intentCreateSendIntent = createSendIntent(iArr, i, 64, -1, i2, z);
            bdSmsDispatcher = this;
        } else {
            bdSmsDispatcher = this;
            intentCreateSendIntent = bdSmsDispatcher.createSendIntent(iArr, i, 32, -1, i2, z);
        }
        bdSmsDispatcher.dispatchBDSmsIntent(intentCreateSendIntent);
    }

    protected void dispatchFailBdSmsReceiveIntent(int i) {
        dispatchBDSmsIntent(createReceiveIntent(false, -1, "", "", i));
    }

    protected Intent createSendIntent(int[] iArr, int i, int i2, int i3, int i4, boolean z) {
        Intent intent = new Intent("miui.intent.action.ACTION_BD_SMS_SENT");
        intent.putExtra("uri", iArr);
        intent.putExtra("idx", i);
        intent.putExtra(a.J, i2);
        intent.putExtra("cause", i3);
        intent.putExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, i4);
        intent.putExtra("is_mms", z);
        return intent;
    }

    protected Intent createReceiveIntent(boolean z, int i, String str, String str2, int i2) {
        Intent intent = new Intent("miui.intent.action.ACTION_BD_SMS_RECEIVED");
        intent.putExtra(a.J, z);
        intent.putExtra("cause", i);
        intent.putExtra(a.m, str);
        intent.putExtra("send_phone_number", str2);
        intent.putExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, i2);
        return intent;
    }

    protected void dispatchBDSmsIntentDelay(Intent intent) {
        Message messageObtain = Message.obtain(this.mHandler, 0);
        messageObtain.obj = intent;
        this.mHandler.sendMessageDelayed(messageObtain, SENT_BD_SMS_INTENT_DELAY_TIMER);
    }

    protected void dispatchBDSmsIntent(Intent intent) {
        ComponentName defaultSmsApplication = SmsApplication.getDefaultSmsApplication(this.mContext, true);
        log("dispatchBDSmsIntent: Delivering BD SMS to:" + defaultSmsApplication.getPackageName() + " " + defaultSmsApplication.getClassName());
        intent.setComponent(defaultSmsApplication);
        intent.addFlags(268435456);
        this.mContext.sendBroadcast(intent, "android.permission.RECEIVE_SMS");
    }

    public class BdSmsDispatcherHandler extends Handler {
        public BdSmsDispatcherHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            BdSmsDispatcher.logi("BdSmsDispatcherHandler handleMessage what = " + message.what);
            if (message.what != 0) {
                return;
            }
            BdSmsDispatcher.this.dispatchBDSmsIntent((Intent) message.obj);
        }
    }

    protected static void log(String str) {
        Log.d("BdSmsDispatcher", str);
    }

    protected static void logi(String str) {
        Log.i("BdSmsDispatcher", str);
    }
}
