package com.android.services.telephony;

import com.android.internal.telephony.Connection;
import mediatek.telecom.MtkConnection;

/* JADX INFO: loaded from: classes.dex */
final class GsmConnection extends TelephonyConnection {
    GsmConnection(Connection connection, String str, int i) {
        super(connection, str, i);
    }

    @Override // com.android.services.telephony.TelephonyConnection
    public TelephonyConnection cloneConnection() {
        return new GsmConnection(getOriginalConnection(), getTelecomCallId(), getCallDirection());
    }

    @Override // android.telecom.Connection
    public void onPlayDtmfTone(char c) {
        if (getPhone() != null) {
            getPhone().startDtmf(c);
        }
    }

    @Override // android.telecom.Connection
    public void onStopDtmfTone() {
        if (getPhone() != null) {
            getPhone().stopDtmf();
        }
    }

    @Override // com.android.services.telephony.TelephonyConnection
    protected int buildConnectionProperties() {
        int iBuildConnectionProperties = super.buildConnectionProperties();
        return (getConnectionProperties() & 64) != 0 ? iBuildConnectionProperties | 64 : iBuildConnectionProperties;
    }

    @Override // com.android.services.telephony.TelephonyConnection
    protected int buildConnectionCapabilities() {
        int iBuildConnectionCapabilities = super.buildConnectionCapabilities();
        int i = iBuildConnectionCapabilities | 64;
        if (!shouldTreatAsEmergencyCall() && !isImsConnection()) {
            i = iBuildConnectionCapabilities | 66;
            if (isHoldable() && (getState() == 4 || getState() == 5)) {
                i = iBuildConnectionCapabilities | 67;
            }
        }
        Log.d(this, "buildConnectionCapabilities: " + MtkConnection.capabilitiesToString(i), new Object[0]);
        return i;
    }
}
