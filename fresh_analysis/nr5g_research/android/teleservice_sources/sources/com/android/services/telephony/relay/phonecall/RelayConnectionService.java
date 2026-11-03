package com.android.services.telephony.relay.phonecall;

import android.net.Uri;
import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccountHandle;
import android.text.TextUtils;
import com.android.services.telephony.DisconnectCauseUtil;
import com.xiaomi.internal.telephony.DynamicSarService;

/* JADX INFO: loaded from: classes.dex */
public class RelayConnectionService extends ConnectionService {
    private static final String TAG = "RelayConnectionService";

    @Override // android.telecom.ConnectionService
    public Connection onCreateIncomingConnection(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        RelayLog.logi(TAG, "onCreateIncomingConnection, request: " + connectionRequest);
        RelayConnection relayConnectionCreateConnectionFor = createConnectionFor(false, connectionRequest.getTelecomCallId());
        Bundle extras = connectionRequest.getExtras();
        Uri uri = (Uri) extras.getParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS");
        if (uri != null) {
            RelayLog.logi(TAG, "onCreateIncomingConnection. Set caller id info.");
            relayConnectionCreateConnectionFor.setAddress(uri, 1);
        }
        RelayLog.logi(TAG, "onCreateIncomingConnection. " + extras.getBoolean("telecomm.TRANSFER_CALL"));
        relayConnectionCreateConnectionFor.setRelayConnectionService(this);
        String string = extras.getString("telecomm.EXTRA_RELAY_DEVICE_NAME");
        if (string == null) {
            string = "";
        }
        relayConnectionCreateConnectionFor.setHostName(string);
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            if (!TextUtils.isEmpty(relayManager.getRecordName())) {
                RelayLog.logi(TAG, "onCreateIncomingConnection. Set relay caller name.");
                Bundle bundle = new Bundle();
                bundle.putString("telecomm.EXTRA_CONTACT", relayManager.getRecordName());
                relayConnectionCreateConnectionFor.putExtras(bundle);
            }
            RelayConnectionsManager relayConnectionsManager = relayManager.getRelayConnectionsManager();
            if (relayConnectionsManager != null && uri != null && relayConnectionsManager.isDisconnectedConnection(uri.getSchemeSpecificPart())) {
                RelayLog.logi(TAG, "onCreateIncomingConnection. Connection is disconnected.");
                relayConnectionCreateConnectionFor.forceDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(3));
                return null;
            }
        }
        return relayConnectionCreateConnectionFor;
    }

    @Override // android.telecom.ConnectionService
    public Connection onCreateOutgoingConnection(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        RelayLog.logi(TAG, "onCreateOutgoingConnection, request: " + connectionRequest);
        RelayConnection relayConnectionCreateConnectionFor = createConnectionFor(true, connectionRequest.getTelecomCallId());
        Uri uri = (Uri) connectionRequest.getExtras().getParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS");
        if (uri != null) {
            RelayLog.logi(TAG, "onCreateOutgoingConnection. Set caller id info.");
            relayConnectionCreateConnectionFor.setAddress(uri, 1);
        }
        relayConnectionCreateConnectionFor.setRelayConnectionService(this);
        return relayConnectionCreateConnectionFor;
    }

    private RelayConnection createConnectionFor(boolean z, String str) {
        RelayConnection relayConnection = new RelayConnection(z, str);
        relayConnection.setConnectionProperties(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        relayConnection.setConnectionCapabilities(3);
        return relayConnection;
    }
}
