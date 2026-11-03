package com.android.phone.eventhub;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.internal.telephony.uicc.UiccController;
import com.android.phone.eventhub.events.EventDispatcher;
import com.android.phone.eventhub.events.TelephonyEvent;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyObserver extends EventObserver {
    private final Handler mHandler;
    private final UiccController mUiccController;

    public TelephonyObserver(Context context, EventDispatcher eventDispatcher) {
        super(context, eventDispatcher);
        this.mUiccController = UiccController.getInstance();
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.phone.eventhub.TelephonyObserver.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 0) {
                    TelephonyObserver.this.logd("EVENT_ICC_CHANGED received");
                    TelephonyObserver.this.notifyEvent(new TelephonyEvent.SimEvent("icc_changed", null));
                }
            }
        };
    }

    @Override // com.android.phone.eventhub.EventObserver
    public void startTracking() {
        this.mUiccController.registerForIccChanged(this.mHandler, 0, (Object) null);
    }
}
