package com.android.services.telephony;

import android.telecom.Connection;
import android.telecom.PhoneAccountHandle;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Phone;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyConference extends TelephonyConferenceBase implements Holdable {
    private boolean mIsHoldable;

    @Override // com.android.services.telephony.Holdable
    public boolean isChildHoldable() {
        return false;
    }

    public TelephonyConference(PhoneAccountHandle phoneAccountHandle) {
        super(phoneAccountHandle);
        setConnectionCapabilities(195);
        setActive();
        this.mIsHoldable = true;
    }

    @Override // android.telecom.Conference
    public void onDisconnect() {
        Iterator<Connection> it = getConnections().iterator();
        while (it.hasNext() && !disconnectCall(it.next())) {
        }
    }

    private boolean disconnectCall(Connection connection) {
        Call multipartyCallForConnection = getMultipartyCallForConnection(connection, "onDisconnect");
        if (multipartyCallForConnection != null) {
            Log.d(this, "Found multiparty call to hangup for conference.", new Object[0]);
            try {
                multipartyCallForConnection.hangup();
                return true;
            } catch (CallStateException e) {
                Log.e((Object) this, (Throwable) e, "Exception thrown trying to hangup conference", new Object[0]);
            }
        }
        return false;
    }

    @Override // android.telecom.Conference
    public void onSeparate(Connection connection) {
        try {
            getOriginalConnection(connection).separate();
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "Exception thrown trying to separate a conference call", new Object[0]);
        }
    }

    @Override // android.telecom.Conference
    public void onAnswer(int i) {
        Log.e(this, new Exception(), "Answer not supported for GSM conference call.", new Object[0]);
    }

    @Override // android.telecom.Conference
    public void onReject() {
        Log.e(this, new Exception(), "Reject not supported for GSM conference call.", new Object[0]);
    }

    @Override // android.telecom.Conference
    public void onAddConferenceParticipants(List list) {
        Log.e(this, new Exception(), "Adding Conference Participants not supported  for GSM conference call.", new Object[0]);
    }

    @Override // android.telecom.Conference
    public void onMerge(Connection connection) {
        try {
            Phone phone = ((TelephonyConnection) connection).getPhone();
            if (phone != null) {
                phone.conference();
            }
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "Exception thrown trying to merge call into a conference", new Object[0]);
        }
    }

    @Override // android.telecom.Conference
    public void onHold() {
        TelephonyConnection firstConnection = getFirstConnection();
        if (firstConnection != null) {
            firstConnection.onHold();
        }
    }

    @Override // android.telecom.Conference
    public void onUnhold() {
        TelephonyConnection firstConnection = getFirstConnection();
        if (firstConnection != null) {
            firstConnection.onUnhold();
        }
    }

    @Override // android.telecom.Conference
    public void onPlayDtmfTone(char c) {
        TelephonyConnection firstConnection = getFirstConnection();
        if (firstConnection != null) {
            firstConnection.onPlayDtmfTone(c);
        }
    }

    @Override // android.telecom.Conference
    public void onStopDtmfTone() {
        TelephonyConnection firstConnection = getFirstConnection();
        if (firstConnection != null) {
            firstConnection.onStopDtmfTone();
        }
    }

    @Override // android.telecom.Conference
    public void onConnectionAdded(Connection connection) {
        if (connection instanceof TelephonyConnection) {
            TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
            if ((telephonyConnection.wasImsConnection() || MiTelephonyServiceStub.get().isAfterSrvccConnection(telephonyConnection)) && MiTelephonyServiceStub.get().disableManageConference(telephonyConnection.getPhone())) {
                removeCapability(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            }
        }
    }

    public Connection getPrimaryConnection() {
        List<Connection> connections = getConnections();
        if (connections == null || connections.isEmpty()) {
            return null;
        }
        Connection connection = connections.get(0);
        for (Connection connection2 : connections) {
            com.android.internal.telephony.Connection originalConnection = getOriginalConnection(connection2);
            if (originalConnection != null && originalConnection.isMultiparty()) {
                return connection2;
            }
        }
        return connection;
    }

    @Override // com.android.services.telephony.Holdable
    public void setHoldable(boolean z) {
        this.mIsHoldable = z;
        if (!z) {
            removeCapability(1);
        } else {
            addCapability(1);
        }
    }

    private Call getMultipartyCallForConnection(Connection connection, String str) {
        Call call;
        com.android.internal.telephony.Connection originalConnection = getOriginalConnection(connection);
        if (originalConnection == null || (call = originalConnection.getCall()) == null || !call.isMultiparty()) {
            return null;
        }
        return call;
    }

    protected com.android.internal.telephony.Connection getOriginalConnection(Connection connection) {
        if (connection instanceof TelephonyConnection) {
            return ((TelephonyConnection) connection).getOriginalConnection();
        }
        return null;
    }

    private TelephonyConnection getFirstConnection() {
        List<Connection> connections = getConnections();
        if (connections.isEmpty()) {
            return null;
        }
        return (TelephonyConnection) connections.get(0);
    }
}
