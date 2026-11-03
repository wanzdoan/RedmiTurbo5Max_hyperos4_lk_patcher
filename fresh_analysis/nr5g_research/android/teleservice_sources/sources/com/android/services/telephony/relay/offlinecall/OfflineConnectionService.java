package com.android.services.telephony.relay.offlinecall;

import android.net.Uri;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccountHandle;
import com.xiaomi.internal.telephony.DynamicSarService;

/* JADX INFO: loaded from: classes.dex */
public class OfflineConnectionService extends ConnectionService {
    private static final String TAG = "RelayConnectionService";

    @Override // android.telecom.ConnectionService
    public Connection onCreateIncomingConnection(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        OfflineCallLog.logi(TAG, "onCreateIncomingConnection, request: " + connectionRequest);
        OfflineConnection offlineConnectionCreateConnectionFor = createConnectionFor(false, connectionRequest.getTelecomCallId());
        Uri uri = (Uri) connectionRequest.getExtras().getParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS");
        if (uri != null) {
            OfflineCallLog.logi(TAG, "onCreateIncomingConnection. Set caller id info.");
            offlineConnectionCreateConnectionFor.setAddress(uri, 1);
        }
        offlineConnectionCreateConnectionFor.setOfflineConnectionService(this);
        return offlineConnectionCreateConnectionFor;
    }

    @Override // android.telecom.ConnectionService
    public Connection onCreateOutgoingConnection(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        OfflineCallLog.logi(TAG, "onCreateOutgoingConnection, request: " + connectionRequest);
        OfflineConnection offlineConnectionCreateConnectionFor = createConnectionFor(true, connectionRequest.getTelecomCallId());
        Uri uri = (Uri) connectionRequest.getExtras().getParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS");
        if (uri != null) {
            OfflineCallLog.logi(TAG, "onCreateOutgoingConnection. Set caller id info.");
            offlineConnectionCreateConnectionFor.setAddress(uri, 1);
        }
        offlineConnectionCreateConnectionFor.setOfflineConnectionService(this);
        return offlineConnectionCreateConnectionFor;
    }

    private OfflineConnection createConnectionFor(boolean z, String str) {
        OfflineConnection offlineConnection = new OfflineConnection(z, str);
        offlineConnection.setConnectionProperties(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        offlineConnection.setConnectionCapabilities(3);
        return offlineConnection;
    }
}
