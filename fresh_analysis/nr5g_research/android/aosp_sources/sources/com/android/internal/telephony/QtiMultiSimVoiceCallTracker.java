package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class QtiMultiSimVoiceCallTracker {
    public static final String ACTION_MSIM_VOICE_CAPABILITY = "org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY";
    public static final String EXTRAS_DSDS_TRANSITION_SUPPORTED = "DsdsTransitionSupported";
    public static final String EXTRAS_MSIM_VOICE_CAPABILITY = "MsimVoiceCapability";
    public static final String PERMISSION_MSIM_VOICE_CAPABILITY = "com.qti.permission.RECEIVE_MSIM_VOICE_CAPABILITY";
    private BroadcastReceiver ConcurrentCallsReceiver;
    private final Context mContext;
    private final Handler mHandler;

    public QtiMultiSimVoiceCallTracker(Context context, Handler handler) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.QtiMultiSimVoiceCallTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                int intExtra = intent.getIntExtra(QtiMultiSimVoiceCallTracker.EXTRAS_MSIM_VOICE_CAPABILITY, 4);
                boolean booleanExtra = intent.getBooleanExtra(QtiMultiSimVoiceCallTracker.EXTRAS_DSDS_TRANSITION_SUPPORTED, false);
                Log.d("QtiMultiSimVoiceCallTracker", "ConcurrentCallsReceiver: voiceCapability : " + intExtra + " + isDsdsTransitionSupported : " + booleanExtra);
                TelephonyProperties.multi_sim_voice_capability(Integer.valueOf(intExtra));
                TelephonyProperties.dsds_transition_supported(Boolean.valueOf(booleanExtra));
                if (intExtra == 4 || QtiMultiSimVoiceCallTracker.this.mHandler == null) {
                    return;
                }
                QtiMultiSimVoiceCallTracker.this.mHandler.sendMessage(QtiMultiSimVoiceCallTracker.this.mHandler.obtainMessage(106));
            }
        };
        this.ConcurrentCallsReceiver = broadcastReceiver;
        this.mContext = context;
        this.mHandler = handler;
        if (context != null) {
            context.registerReceiver(broadcastReceiver, new IntentFilter(ACTION_MSIM_VOICE_CAPABILITY), PERMISSION_MSIM_VOICE_CAPABILITY, null, 2);
        }
    }

    public List<Integer> generateSimultaneousCallingSupport() {
        Log.d("QtiMultiSimVoiceCallTracker", "generateSimultaneousCallingSupport");
        ArrayList arrayList = new ArrayList();
        if (TelephonyProperties.multi_sim_voice_capability().orElse(0).intValue() == 3) {
            arrayList.addAll(Arrays.asList(0, 1));
        }
        return arrayList;
    }
}
