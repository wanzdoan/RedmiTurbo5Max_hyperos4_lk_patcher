package com.android.services.telephony.relay.phonecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telecom.Connection;
import android.text.TextUtils;
import com.android.services.telephony.relay.phonecall.message.MessageCodecUtils;
import com.android.services.telephony.relay.phonecall.message.MessagesManager;
import com.android.services.telephony.relay.phonecall.message.RelayMessageHandler;
import com.android.services.telephony.relay.phonecall.message.api.RelayApi;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestCallInfo;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestCallState;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestCallStateIdle;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestDial;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestOperate;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestRelease;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestRing;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestTransfer;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestWantRelay;
import gov.nist.javax.sip.address.GenericURI;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class RelayManager {
    private static final String TAG = "RelayManager";
    private final MessagesManager mMessagesManager;
    private final RelayConnectionsManager mRelayConnectionsManager;
    private final RelayMessageHandler mRelayMessageHandler;
    private String mNumber = "";
    private String mName = "";
    private final RelayPhoneAccountRegistry mRelayPhoneAccountRegistry = new RelayPhoneAccountRegistry();

    public RelayManager() {
        RelayConnectionsManager relayConnectionsManager = new RelayConnectionsManager();
        this.mRelayConnectionsManager = relayConnectionsManager;
        MessagesManager messagesManager = new MessagesManager();
        this.mMessagesManager = messagesManager;
        this.mRelayMessageHandler = new RelayMessageHandler(relayConnectionsManager, messagesManager);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.RELAY_UPDATE");
        RelayUtils.System.getContext().registerReceiver(new BroadcastReceiver() { // from class: com.android.services.telephony.relay.phonecall.RelayManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                RelayLog.logi(RelayManager.TAG, "onReceive action:" + action);
                if ("miui.intent.action.RELAY_UPDATE".equals(action)) {
                    RelayManager.this.handleRelayUpdateAction(intent);
                }
            }
        }, intentFilter, "com.android.phone.permission.ACCESS_RELAY_SERVICE", null, 2);
    }

    public RelayConnectionsManager getRelayConnectionsManager() {
        return this.mRelayConnectionsManager;
    }

    public RelayMessageHandler getRelayMessageHandler() {
        return this.mRelayMessageHandler;
    }

    public MessagesManager getMessagesManager() {
        return this.mMessagesManager;
    }

    public void enableRelayPhoneAccount(boolean z) {
        if (!z) {
            this.mRelayConnectionsManager.tearDown();
        }
        this.mRelayPhoneAccountRegistry.enablePhoneAccount(z);
    }

    public void createRelayPhoneAccount() {
        this.mRelayPhoneAccountRegistry.createRelayPhoneAccount();
    }

    public void onRelayMessageReceived(String str) {
        this.mRelayMessageHandler.onRelayMessageReceived(str);
    }

    public void onRelayCallStateChanged(Connection connection) {
        if (connection == null) {
            RelayUtils.Relay.clearCurrentConnection();
            RelayUtils.Relay.clearWantRelayMap();
            return;
        }
        int state = connection.getState();
        if (state == 6) {
            RelayUtils.Relay.clearWantRelayMap();
            RelayLog.logi(TAG, "call disconnected clear the currentConnection");
            RelayUtils.Relay.clearCurrentConnection();
        }
        RelayLog.logi(TAG, "onRelayCallStateChanged state:" + Connection.stateToString(state));
        if ((!RelayUtils.Relay.isCallRelayed(connection) && !RelayUtils.Relay.isCallRelaying(connection)) || connection.getState() == 2) {
            RelayLog.logi(TAG, "onRelayCallStateChanged not update");
            return;
        }
        RelayRequestCallState relayRequestCallState = new RelayRequestCallState();
        relayRequestCallState.callState = state;
        relayRequestCallState.address = RelayUtils.Telephony.getNumber(connection);
        if (connection.getDisconnectCause() != null) {
            relayRequestCallState.disconnectCode = connection.getDisconnectCause().getTelephonyDisconnectCause();
        } else {
            relayRequestCallState.disconnectCode = -1;
        }
        if (TextUtils.isEmpty(relayRequestCallState.address)) {
            RelayLog.logi(TAG, "onRelayCallStateChanged address is null.");
            return;
        }
        boolean zIsConnectionInRelay = RelayUtils.Relay.isConnectionInRelay(connection);
        if (zIsConnectionInRelay && PhoneContinuityController.getInstance().isDeviceInRelay()) {
            sendUpdateCallStateRequest(PhoneContinuityController.getInstance().getDeviceInRelay(), relayRequestCallState);
        } else {
            List<String> connectedServiceDevices = PhoneContinuityController.getInstance().getConnectedServiceDevices();
            if (connectedServiceDevices != null && connectedServiceDevices.size() > 0) {
                for (String str : connectedServiceDevices) {
                    if (!str.equals(PhoneContinuityController.getInstance().getDeviceInRelay())) {
                        sendUpdateCallStateRequest(str, relayRequestCallState);
                    }
                }
            }
        }
        if (connection.getState() == 6 && zIsConnectionInRelay) {
            PhoneContinuityController.getInstance().setDeviceInRelay(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRelayUpdateAction(Intent intent) {
        String stringExtra = intent.getStringExtra("extra_number");
        String stringExtra2 = intent.getStringExtra("extra_contact");
        boolean booleanExtra = intent.getBooleanExtra("extra_release", false);
        int intExtra = intent.getIntExtra("extra_release_reason_code", -1);
        boolean booleanExtra2 = intent.getBooleanExtra("extra_place_relay_call", false);
        String stringExtra3 = intent.getStringExtra("extra_transfer_relay_call_device_id");
        long longExtra = intent.getLongExtra("extra_transfer_relay_call_connect_time_millis", 0L);
        if (TextUtils.isEmpty(stringExtra)) {
            RelayLog.logi(TAG, "handleRelayUpdateAction number is empty!");
            return;
        }
        String strReplaceAll = stringExtra.replaceAll(" ", "");
        if (!TextUtils.isEmpty(stringExtra2)) {
            updateContact(strReplaceAll, stringExtra2);
        }
        if (booleanExtra) {
            relayRelease(strReplaceAll, intExtra);
        }
        if (booleanExtra2) {
            placeCall(strReplaceAll);
        }
        if (TextUtils.isEmpty(stringExtra3)) {
            return;
        }
        transferCall(strReplaceAll, stringExtra3, longExtra);
    }

    public void relayRelease(String str, int i) {
        RelayLog.logi(TAG, "relayRelease, release relay call, releaseType:" + i);
        if (i == 7) {
            RelayRequestRelease relayRequestRelease = new RelayRequestRelease();
            RelayConnection relayConnectionWithNumber = this.mRelayConnectionsManager.getRelayConnectionWithNumber(str);
            if (relayConnectionWithNumber != null) {
                relayRequestRelease.address = RelayUtils.Telephony.getNumber(relayConnectionWithNumber);
                relayRequestRelease.releaseType = i;
                this.mMessagesManager.padSendRequestMessage(MessageCodecUtils.getRequestObject(RelayApi.RELEASE, relayRequestRelease.toJson()));
                relayConnectionWithNumber.forceDisconnected(relayConnectionWithNumber.getDisconnectCause(RelayRequestRelease.getDisconnectCause(i)));
                RelayUtils.Relay.setDeviceInRelay(null);
                return;
            }
            return;
        }
        Connection localConnectionWithNumber = this.mRelayConnectionsManager.getLocalConnectionWithNumber(str);
        if (localConnectionWithNumber != null) {
            forceReleaseRelayCall(localConnectionWithNumber, i);
        }
    }

    public void placeCall(String str) {
        boolean zIsRelayDialEnable = RelayUtils.Relay.isRelayDialEnable(RelayUtils.System.getContext());
        RelayLog.logi(TAG, "placeCall, place relay call, enable:" + zIsRelayDialEnable);
        if (zIsRelayDialEnable) {
            RelayRequestDial relayRequestDial = new RelayRequestDial();
            relayRequestDial.address = str;
            sendDialRequest(relayRequestDial);
        }
    }

    public boolean transferCall(String str, String str2, long j) {
        RelayLog.logi(TAG, "transferCall, transfer call, transferRelayCallDeviceId:" + str2);
        if (TextUtils.isEmpty(str2) || PhoneContinuityController.getInstance().getRelayServiceInfo(str2) == null || str2.equals(PhoneContinuityController.getInstance().getDeviceInRelay())) {
            return false;
        }
        return sendRelayTransferRequest(str2, j, this.mRelayConnectionsManager.getLocalConnectionWithNumber(str));
    }

    public void updateContact(String str, String str2) {
        Connection localConnectionWithNumber = this.mRelayConnectionsManager.getLocalConnectionWithNumber(str);
        if (localConnectionWithNumber != null) {
            if (RelayUtils.Relay.isCallRelayAnswered(localConnectionWithNumber) && PhoneContinuityController.getInstance().isDeviceInRelay()) {
                RelayRequestCallInfo relayRequestCallInfo = new RelayRequestCallInfo();
                relayRequestCallInfo.address = str;
                relayRequestCallInfo.contactName = str2;
                sendUpdateCallInfoRequest(PhoneContinuityController.getInstance().getDeviceInRelay(), relayRequestCallInfo);
            } else {
                List<String> connectedServiceDevices = PhoneContinuityController.getInstance().getConnectedServiceDevices();
                if (connectedServiceDevices != null && connectedServiceDevices.size() > 0) {
                    RelayRequestCallInfo relayRequestCallInfo2 = new RelayRequestCallInfo();
                    relayRequestCallInfo2.address = str;
                    relayRequestCallInfo2.contactName = str2;
                    for (String str3 : connectedServiceDevices) {
                        if (!str3.equals(PhoneContinuityController.getInstance().getDeviceInRelay())) {
                            sendUpdateCallInfoRequest(str3, relayRequestCallInfo2);
                        }
                    }
                }
            }
            RelayLog.logi(TAG, "updateContact");
        }
    }

    public void forceReleaseRelayCall(Connection connection, int i) {
        if (RelayUtils.Relay.isCallRelayed(connection)) {
            Iterator it = PhoneContinuityController.getInstance().getConnectedServiceDevices().iterator();
            while (it.hasNext()) {
                sendReleaseCallRequest((String) it.next(), i, connection);
            }
            releaseRelayExtra(connection);
        }
    }

    public void releaseRelayExtra(Connection connection) {
        if (connection == null) {
            return;
        }
        Bundle extras = connection.getExtras();
        Bundle bundle = new Bundle();
        if (extras != null && extras.containsKey("telecomm.EXTRA_CALL_RELAYED")) {
            bundle.putBoolean("telecomm.EXTRA_CALL_RELAYED", false);
        }
        if (extras != null && extras.containsKey("telecomm.EXTRA_RELAY_ANSWERED")) {
            bundle.putBoolean("telecomm.EXTRA_RELAY_ANSWERED", false);
        }
        if (extras != null && extras.containsKey("telecomm.EXTRA_RELAY_DEVICE_ID")) {
            bundle.putString("telecomm.EXTRA_RELAY_DEVICE_ID", "");
        }
        connection.putExtras(bundle);
        RelayLog.logi(TAG, "releaseRelayExtra");
    }

    public void addLocalConnection(Connection connection) {
        this.mRelayConnectionsManager.addLocalConnection(connection);
    }

    public void removeLocalConnection(Connection connection) {
        this.mRelayConnectionsManager.removeLocalConnection(connection);
        this.mRelayConnectionsManager.removeRelayedLocalConnection(connection);
    }

    public void sendReleaseCallRequest(String str, int i, Connection connection) {
        RelayRequestRelease relayRequestRelease = new RelayRequestRelease();
        relayRequestRelease.address = RelayUtils.Telephony.getNumber(connection);
        relayRequestRelease.releaseType = i;
        this.mMessagesManager.phoneSendRequestMessage(str, MessageCodecUtils.getRequestObject(RelayApi.RELEASE, relayRequestRelease.toJson()));
        PhoneContinuityController.getInstance().setDeviceInRelay(null);
    }

    public void sendRelayRingCallRequest(Connection connection) {
        RelayRequestRing relayRequestRing = new RelayRequestRing();
        relayRequestRing.address = RelayUtils.Telephony.getNumber(connection);
        relayRequestRing.hostDeviceName = RelayUtils.System.getDeviceName();
        relayRequestRing.telecomCallId = connection.getTelecomCallId();
        for (String str : PhoneContinuityController.getInstance().getConnectedServiceDevices()) {
            if (!str.equals(PhoneContinuityController.getInstance().getDeviceInRelay())) {
                RelayServiceInfo relayServiceInfo = PhoneContinuityController.getInstance().getRelayServiceInfo(str);
                if (relayServiceInfo != null && relayServiceInfo.mDeviceType == 11 && !RelayUtils.Relay.getWantRelayForDeviceId(relayServiceInfo.mDeviceId)) {
                    RelayLog.logi(TAG, "ApplePhone device But no wantRelay");
                } else if (relayServiceInfo != null && relayServiceInfo.mDeviceType == 1 && !RelayUtils.Relay.getWantRelayForDeviceId(relayServiceInfo.mDeviceId)) {
                    RelayLog.logi(TAG, "MiPhone device But no wantRelay");
                } else {
                    this.mMessagesManager.phoneSendRequestMessage(str, MessageCodecUtils.getRequestObject(RelayApi.RING, relayRequestRing.toJson()));
                }
            }
        }
    }

    public void sendRelayRingCallRequestToDeviceId(Connection connection, String str) {
        int i;
        RelayRequestRing relayRequestRing = new RelayRequestRing();
        relayRequestRing.address = RelayUtils.Telephony.getNumber(connection);
        relayRequestRing.hostDeviceName = RelayUtils.System.getDeviceName();
        relayRequestRing.telecomCallId = connection.getTelecomCallId();
        for (String str2 : PhoneContinuityController.getInstance().getConnectedServiceDevices()) {
            if (str2.equals(str)) {
                RelayLog.logi(TAG, "send ring message to device " + str);
                RelayServiceInfo relayServiceInfo = PhoneContinuityController.getInstance().getRelayServiceInfo(str2);
                if (relayServiceInfo != null && RelayUtils.Relay.getWantRelayForDeviceId(str2) && ((i = relayServiceInfo.mDeviceType) == 1 || i == 11)) {
                    this.mMessagesManager.phoneSendRequestMessage(str2, MessageCodecUtils.getRequestObject(RelayApi.RING, relayRequestRing.toJson()));
                }
            }
        }
    }

    public boolean sendRelayTransferRequest(String str, long j, Connection connection) {
        if (connection == null) {
            return false;
        }
        RelayRequestTransfer relayRequestTransfer = new RelayRequestTransfer();
        relayRequestTransfer.address = RelayUtils.Telephony.getNumber(connection);
        relayRequestTransfer.hostDeviceName = RelayUtils.System.getDeviceName();
        relayRequestTransfer.connectTimeMillis = j;
        relayRequestTransfer.connectDuration = System.currentTimeMillis() - j;
        this.mMessagesManager.phoneSendRequestMessage(str, MessageCodecUtils.getRequestObject(RelayApi.TRANSFER, relayRequestTransfer.toJson()));
        Bundle bundle = new Bundle();
        bundle.putBoolean("telecomm.EXTRA_CALL_RELAYED", true);
        connection.putExtras(bundle);
        RelayLog.logi(TAG, "sendRelayTransferRequest, " + j);
        return true;
    }

    public void sendOperateRequest(Connection connection, int i) {
        RelayChannelInfo channelInRelay;
        if (this.mRelayConnectionsManager.getRelayConnections().contains(connection)) {
            RelayRequestOperate relayRequestOperate = new RelayRequestOperate();
            relayRequestOperate.operateType = i;
            if (i == 0 && (channelInRelay = RelayUtils.Relay.getChannelInRelay()) != null) {
                RelayUtils.Relay.setDeviceInRelay(channelInRelay.mDeviceId);
            }
            relayRequestOperate.address = RelayUtils.Telephony.getNumber(connection);
            this.mMessagesManager.padSendRequestMessage(MessageCodecUtils.getRequestObject(RelayApi.OPERATE, relayRequestOperate.toJson()));
            return;
        }
        RelayLog.logi(TAG, "sendOperateRequest relay connection not added!");
    }

    public void sendWantRelayRequest(String str, String str2) {
        RelayRequestWantRelay relayRequestWantRelay = new RelayRequestWantRelay();
        relayRequestWantRelay.action = RelayRequestWantRelay.WANTRELAY_ANSWER;
        relayRequestWantRelay.address = str;
        this.mMessagesManager.sendWantRelayRequestMessage(str2, MessageCodecUtils.getRequestObject(RelayApi.WANT_RELAY, relayRequestWantRelay.toJson()));
    }

    public void sendUpdateCallStateRequest(String str, RelayRequestCallState relayRequestCallState) {
        this.mMessagesManager.phoneSendRequestMessage(str, MessageCodecUtils.getRequestObject(RelayApi.UPDATE_CALL_STATE, relayRequestCallState.toJson()));
    }

    public void sendUpdateCallInfoRequest(String str, RelayRequestCallInfo relayRequestCallInfo) {
        this.mMessagesManager.phoneSendRequestMessage(str, MessageCodecUtils.getRequestObject(RelayApi.UPDATE_CALL_INFO, relayRequestCallInfo.toJson()));
    }

    public void sendDialRequest(RelayRequestDial relayRequestDial) {
        this.mMessagesManager.padSendRequestMessage(MessageCodecUtils.getRequestObject(RelayApi.DIAL, relayRequestDial.toJson()));
    }

    public void sendCallStateIdle(String str) {
        RelayRequestCallStateIdle relayRequestCallStateIdle = new RelayRequestCallStateIdle();
        relayRequestCallStateIdle.address = GenericURI.TEL;
        this.mMessagesManager.phoneSendRequestMessage(str, MessageCodecUtils.getRequestObject(RelayApi.CALL_STATE_IDLE, relayRequestCallStateIdle.toJson()));
    }

    public synchronized void recordIncomingInfo(String str, String str2) {
        this.mNumber = str;
        this.mName = str2;
    }

    public synchronized String getRecordNumber() {
        return this.mNumber;
    }

    public synchronized String getRecordName() {
        return this.mName;
    }
}
