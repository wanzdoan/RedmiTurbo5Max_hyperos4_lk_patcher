package com.android.services.telephony;

import android.telecom.Conference;
import android.telecom.Connection;
import android.telecom.PhoneAccountHandle;
import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
public interface TelephonyConnectionServiceProxy {
    void addConference(ImsConference imsConference);

    void addConference(TelephonyConference telephonyConference);

    void addConferenceFromConnection(ImsConference imsConference, TelephonyConnection telephonyConnection);

    void addConnectionToConferenceController(TelephonyConnection telephonyConnection);

    void addExistingConnection(PhoneAccountHandle phoneAccountHandle, Connection connection);

    void addExistingConnection(PhoneAccountHandle phoneAccountHandle, Connection connection, Conference conference);

    Collection getAllConnections();
}
