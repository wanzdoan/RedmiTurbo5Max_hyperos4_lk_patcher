package com.android.phone;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;

/* JADX INFO: loaded from: classes.dex */
public class FakeCellController {
    private static FakeCellController mInstance;
    private Context mContext;
    private RilHook mRilHook;
    private RilHookListener mRilHookListener = null;

    public static synchronized FakeCellController init(Context context) {
        try {
            if (mInstance == null) {
                mInstance = new FakeCellController(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private FakeCellController(Context context) {
        this.mRilHook = null;
        this.mContext = context;
        this.mRilHook = RilHook.init(context);
        registerRilHookListener();
    }

    private void registerRilHookListener() {
        log("registerRilHookListener");
        this.mRilHookListener = new RilHookListener();
        EventHubManager.getInstance().registerListener("telephony", "ril_ready", this.mRilHookListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initFakecellSysProp() {
        try {
            String oemNvItem = this.mRilHook.readOemNvItem(6854);
            if (oemNvItem.contains(";")) {
                int i = Integer.parseInt(oemNvItem.substring(0, oemNvItem.indexOf(59)));
                if ((i & 1) == 0) {
                    log("initFakecellSysProp false");
                    return;
                }
                int i2 = (i >> 1) & 3;
                log("initFakecellSysProp true,status=" + i2);
                if (i2 != 1 && i2 != 2) {
                    return;
                }
                SystemProperties.set("persist.radio.support.fakecell", "true");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RilHookListener implements BaseEvent.IEventListener {
        RilHookListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof TelephonyEvent.RilEvent) {
                boolean zIsRilReady = ((TelephonyEvent.RilEvent) baseEvent).isRilReady();
                FakeCellController.this.log("onQcRilReady qcRilReady=" + zIsRilReady);
                if (zIsRilReady) {
                    new Thread(new Runnable() { // from class: com.android.phone.FakeCellController$RilHookListener$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onEvent$0();
                        }
                    }).start();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEvent$0() {
            FakeCellController.this.initFakecellSysProp();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("FakeCellController", str);
    }
}
