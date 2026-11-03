package com.android.phone.roaming.recovery;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.roaming.RoamingManager;
import com.android.phone.roaming.utils.RoamingUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.phone.utils.ModemUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRecovery {
    private static NetworkRecovery mInstance;
    private Listener mListener = new Listener();
    private Context mContext = PhoneGlobals.getInstance();
    private OOSHandler mHandler = new OOSHandler(MiuiThreadUtil.getWorkThreadLooper());

    private NetworkRecovery() {
        EventHubManager.getInstance().registerListener("telephony", "roaming_mode_changed", this.mListener, this.mHandler);
    }

    public static void init() {
        if (mInstance == null) {
            mInstance = new NetworkRecovery();
        }
    }

    class Listener implements BaseEvent.IEventListener {
        Listener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            Bundle bundle;
            if (!(baseEvent instanceof TelephonyEvent.NetworkEvent) || (bundle = ((TelephonyEvent.NetworkEvent) baseEvent).getBundle()) == null) {
                return;
            }
            NetworkRecovery.this.processRoamingModeChanged(bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1), bundle.getInt("roaming_mode", -1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRoamingModeChanged(int i, int i2) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || phone.getServiceState() == null) {
            Log.i("NetworkRecovery", "invalid mode changed");
            return;
        }
        ServiceState serviceState = phone.getServiceState();
        Log.d("NetworkRecovery", "processRoamingModeChanged roamingMode:" + i2 + "serviceState:" + serviceState);
        int i3 = i == 0 ? 1 : 2;
        if (isRoamingNetwork(i) && serviceState.getVoiceRegState() == 1 && serviceState.getDataRegState() == 1) {
            if (this.mHandler.hasMessages(i3)) {
                return;
            }
            Log.d("NetworkRecovery", "sendMessage event:" + i3);
            OOSHandler oOSHandler = this.mHandler;
            oOSHandler.sendMessageDelayed(oOSHandler.obtainMessage(i3, i, -1, phone), 300000L);
            return;
        }
        if (this.mHandler.hasMessages(i3)) {
            this.mHandler.removeMessages(i3);
        }
    }

    class OOSHandler extends Handler {
        public OOSHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1 || i == 2) {
                NetworkRecovery.this.refreshMbn(message.arg1, (Phone) message.obj);
            } else {
                if (i != 3) {
                    return;
                }
                NetworkRecovery.this.confirmRefreshResult((Phone) message.obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmRefreshResult(Phone phone) {
        if (phone == null || phone.getServiceState() == null) {
            Log.i("NetworkRecovery", "invalid parameter");
            return;
        }
        ServiceState serviceState = phone.getServiceState();
        String networkOperatorNumeric = RoamingUtils.getNetworkOperatorNumeric(serviceState);
        String operatorNumeric = phone.getOperatorNumeric();
        boolean z = serviceState.getVoiceRegState() == 0 || serviceState.getDataRegState() == 0;
        HashMap map = new HashMap();
        map.put(a.L, Boolean.valueOf(z));
        map.put("simNumeric", operatorNumeric);
        map.put("networkNumeric", networkOperatorNumeric);
        MiStatInterfaceUtil.recordObjectEvent("roaming_optimization", "roaming_oos_optimization", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshMbn(int i, Phone phone) {
        ServiceState serviceState = phone.getServiceState();
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit == null || serviceState == null) {
            Log.i("NetworkRecovery", "modemUtils or serviceState is null");
            return;
        }
        if (serviceState.getVoiceRegState() == 1 && serviceState.getDataRegState() == 1 && isRoamingNetwork(i)) {
            if (phone.getState() != PhoneConstants.State.IDLE) {
                Log.i("NetworkRecovery", "in call don't refreshMbn");
                OOSHandler oOSHandler = this.mHandler;
                oOSHandler.sendMessageDelayed(oOSHandler.obtainMessage(i != 0 ? 2 : 1, i, -1, phone), 300000L);
            } else {
                boolean zOnHookMbnRefresh = modemUtilsInit.onHookMbnRefresh(i + 1);
                OOSHandler oOSHandler2 = this.mHandler;
                oOSHandler2.sendMessageDelayed(oOSHandler2.obtainMessage(3, phone), 120000L);
                Log.i("NetworkRecovery", "refreshMbn result = " + zOnHookMbnRefresh);
            }
        }
    }

    private boolean isRoamingNetwork(int i) {
        int roamingModeForPhoneId = RoamingManager.getInstance().getRoamingModeForPhoneId(i);
        return (roamingModeForPhoneId == 0 || roamingModeForPhoneId == 2 || roamingModeForPhoneId == -1) ? false : true;
    }
}
