package com.android.services.telephony.relay.offlinecall.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.services.telephony.relay.offlinecall.OfflineCallsManager;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestWantRelay;
import com.android.telephony.Rlog;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class OfflineCallsTest {
    private static final String ACTION_XIAOMI_OFFLINE_CALL_DEBUG = "com.android.offlinecall.debug";
    private static final String LOG_TAG = "OfflineCallsTest";
    private static OfflineCallsTest sInstance;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(this) { // from class: com.android.services.telephony.relay.offlinecall.test.OfflineCallsTest.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Rlog.d(OfflineCallsTest.LOG_TAG, "mBroadcastReceiver action = " + action);
            if (OfflineCallsTest.ACTION_XIAOMI_OFFLINE_CALL_DEBUG.equals(action)) {
                String stringExtra = intent.getStringExtra("request_type");
                String stringExtra2 = intent.getStringExtra("sim1_number");
                String stringExtra3 = intent.getStringExtra("sim2_number");
                String stringExtra4 = intent.getStringExtra("ble_addr");
                Rlog.d(OfflineCallsTest.LOG_TAG, " request_type = " + stringExtra + "\n sim1_number = " + stringExtra2 + "\n sim2_number = " + stringExtra3 + "\n ble_addr" + stringExtra4);
                if ("dial".equals(stringExtra)) {
                    OfflineCallsManager.getInstance().dialOfflineCall(stringExtra2, stringExtra3, stringExtra4);
                    return;
                }
                if (RelayRequestWantRelay.WANTRELAY_ANSWER.equals(stringExtra)) {
                    OfflineCallsManager.getInstance().answerOfflineCall();
                    return;
                }
                if ("hangup".equals(stringExtra)) {
                    OfflineCallsManager.getInstance().hangupOfflineCall();
                    return;
                }
                if ("reject".equals(stringExtra)) {
                    OfflineCallsManager.getInstance().rejectOfflineCall(1);
                } else if ("send".equals(stringExtra)) {
                    OfflineCallsManager.getInstance().sendRecordData(ByteBuffer.allocate(4).putInt(888888).array());
                }
            }
        }
    };
    private Context mContext;

    public static synchronized OfflineCallsTest getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new OfflineCallsTest();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private OfflineCallsTest() {
    }

    public void init(Context context) {
        this.mContext = context;
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        if (this.mContext != null) {
            intentFilter.addAction(ACTION_XIAOMI_OFFLINE_CALL_DEBUG);
            this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        }
    }
}
