package com.android.services.telephony;

import android.os.PersistableBundle;
import android.telecom.Conference;
import android.telecom.Conferenceable;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;
import android.util.Patterns;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.phone.PhoneUtils;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
final class TelephonyConferenceController {
    private final TelephonyConnectionServiceProxy mConnectionService;
    private TelephonyConference mTelephonyConference;
    private final TelephonyConnection.TelephonyConnectionListener mTelephonyConnectionListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.TelephonyConferenceController.1
        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onStateChanged(Connection connection, int i) {
            Log.v(this, "onStateChange triggered in Conf Controller : connection = " + connection + " state = " + i, new Object[0]);
            TelephonyConferenceController.this.recalculate();
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onDisconnected(Connection connection, DisconnectCause disconnectCause) {
            TelephonyConferenceController.this.recalculate();
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onDestroyed(Connection connection) {
            TelephonyConferenceController.this.remove((TelephonyConnection) connection);
        }
    };
    private final List mTelephonyConnections = new ArrayList();
    private boolean mTriggerRecalculate = false;

    public TelephonyConferenceController(TelephonyConnectionServiceProxy telephonyConnectionServiceProxy) {
        this.mConnectionService = telephonyConnectionServiceProxy;
    }

    boolean shouldRecalculate() {
        Log.d(this, "shouldRecalculate is " + this.mTriggerRecalculate, new Object[0]);
        return this.mTriggerRecalculate;
    }

    void add(TelephonyConnection telephonyConnection) {
        if (this.mTelephonyConnections.contains(telephonyConnection)) {
            Log.w(this, "add - connection already tracked; connection=%s", telephonyConnection);
            return;
        }
        this.mTelephonyConnections.add(telephonyConnection);
        telephonyConnection.addTelephonyConnectionListener(this.mTelephonyConnectionListener);
        recalculate();
    }

    void remove(TelephonyConnection telephonyConnection) {
        if (!this.mTelephonyConnections.contains(telephonyConnection)) {
            Log.d(this, "remove - connection not tracked; connection=%s", telephonyConnection);
            return;
        }
        telephonyConnection.removeTelephonyConnectionListener(this.mTelephonyConnectionListener);
        this.mTelephonyConnections.remove(telephonyConnection);
        recalculate();
    }

    void recalculate() {
        recalculateConference();
        recalculateConferenceable();
    }

    private boolean isFullConference(Conference conference) {
        return conference.getConnections().size() >= 5;
    }

    private boolean participatesInFullConference(Connection connection) {
        return connection.getConference() != null && isFullConference(connection.getConference());
    }

    private void recalculateConferenceable() {
        int state;
        Log.v(this, "recalculateConferenceable : %d", Integer.valueOf(this.mTelephonyConnections.size()));
        HashSet<Connection> hashSet = new HashSet(this.mTelephonyConnections.size());
        for (TelephonyConnection telephonyConnection : this.mTelephonyConnections) {
            Log.d(this, "recalc - %s %s supportsConf? %s", Integer.valueOf(telephonyConnection.getState()), telephonyConnection, Boolean.valueOf(telephonyConnection.isConferenceSupported()));
            if (telephonyConnection.isConferenceSupported() && !participatesInFullConference(telephonyConnection) && ((state = telephonyConnection.getState()) == 4 || state == 5)) {
                hashSet.add(telephonyConnection);
            } else if (!getConferenceableConnections(telephonyConnection).isEmpty()) {
                telephonyConnection.setConferenceableConnections(Collections.EMPTY_LIST);
            }
        }
        Log.v(this, "conferenceable: " + hashSet.size(), new Object[0]);
        for (final Connection connection : hashSet) {
            List<Connection> list = (List) hashSet.stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConferenceController$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return TelephonyConferenceController.m4343$r8$lambda$DCqsXrPWag5hyTEEFbx8HwCjIQ(connection, (Connection) obj);
                }
            }).collect(Collectors.toList());
            if (!getConferenceableConnections(connection).equals(list)) {
                connection.setConferenceableConnections(list);
            }
        }
        Conference conference = this.mTelephonyConference;
        if (conference != null) {
            if (!isFullConference(conference)) {
                List<Connection> list2 = (List) this.mTelephonyConnections.stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConferenceController$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return TelephonyConferenceController.$r8$lambda$902V95o5uIijcLoKRIOX_KhBOfc((TelephonyConnection) obj);
                    }
                }).collect(Collectors.toList());
                if (this.mTelephonyConference.getConferenceableConnections().equals(list2)) {
                    return;
                }
                this.mTelephonyConference.setConferenceableConnections(list2);
                return;
            }
            Log.d(this, "cannot merge anymore due it is full", new Object[0]);
            if (this.mTelephonyConference.getConferenceableConnections().isEmpty()) {
                return;
            }
            this.mTelephonyConference.setConferenceableConnections(Collections.EMPTY_LIST);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$DCqsXrPW-ag5hyTEEFbx8HwCjIQ, reason: not valid java name */
    public static /* synthetic */ boolean m4343$r8$lambda$DCqsXrPWag5hyTEEFbx8HwCjIQ(Connection connection, Connection connection2) {
        return connection != connection2;
    }

    public static /* synthetic */ boolean $r8$lambda$902V95o5uIijcLoKRIOX_KhBOfc(TelephonyConnection telephonyConnection) {
        if (telephonyConnection.isConferenceSupported() && telephonyConnection.getConference() == null) {
            return telephonyConnection.getState() == 4 || telephonyConnection.getState() == 5;
        }
        return false;
    }

    private void recalculateConference() {
        boolean z;
        Phone phone;
        HashSet<Connection> hashSet = new HashSet();
        int i = 0;
        for (TelephonyConnection telephonyConnection : this.mTelephonyConnections) {
            com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
            if (originalConnection != null) {
                Call.State state = originalConnection.getState();
                Call call = originalConnection.getCall();
                if (state == Call.State.ACTIVE || state == Call.State.HOLDING) {
                    if (call != null && call.isMultiparty()) {
                        i++;
                        hashSet.add(telephonyConnection);
                    }
                }
            }
        }
        Log.d(this, "Recalculate conference calls %s %s.", this.mTelephonyConference, hashSet);
        Collection allConnections = this.mConnectionService.getAllConnections();
        Iterator it = hashSet.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = true;
                break;
            }
            Connection connection = (Connection) it.next();
            Log.v(this, "Finding connection in Connection Service for " + connection, new Object[0]);
            if (!allConnections.contains(connection)) {
                Log.v(this, "Finding connection in Connection Service Failed", new Object[0]);
                z = false;
                break;
            }
        }
        Log.d(this, "Is there a match for all connections in connection service " + z, new Object[0]);
        PhoneAccountHandle phoneAccountHandleMakePstnPhoneAccountHandle = null;
        if (i < 2) {
            Log.d(this, "not enough connections to be a conference!", new Object[0]);
            if (this.mTelephonyConference != null) {
                Log.d(this, "with a conference to destroy!", new Object[0]);
                this.mTelephonyConference.destroyTelephonyConference();
                this.mTelephonyConference = null;
                return;
            }
            return;
        }
        TelephonyConference telephonyConference = this.mTelephonyConference;
        if (telephonyConference != null) {
            List<Connection> connections = telephonyConference.getConnections();
            for (Connection connection2 : connections) {
                if ((connection2 instanceof TelephonyConnection) && !hashSet.contains(connection2)) {
                    this.mTelephonyConference.removeTelephonyConnection(connection2);
                }
            }
            if (z) {
                this.mTriggerRecalculate = false;
                for (Connection connection3 : hashSet) {
                    if (!connections.contains(connection3)) {
                        this.mTelephonyConference.addTelephonyConnection(connection3);
                    }
                }
            } else {
                Log.d(this, "Trigger recalculate later", new Object[0]);
                this.mTriggerRecalculate = true;
            }
        } else if (z) {
            this.mTriggerRecalculate = false;
            if (hashSet.isEmpty()) {
                phone = null;
            } else {
                TelephonyConnection telephonyConnection2 = (TelephonyConnection) hashSet.iterator().next();
                phoneAccountHandleMakePstnPhoneAccountHandle = PhoneUtils.makePstnPhoneAccountHandle(telephonyConnection2.getPhone());
                phone = telephonyConnection2.getPhone();
            }
            this.mTelephonyConference = new TelephonyConference(phoneAccountHandleMakePstnPhoneAccountHandle);
            Log.i(this, "Creating new TelephonyConference to hold conferenced connections. conference=" + this.mTelephonyConference, new Object[0]);
            Iterator it2 = hashSet.iterator();
            boolean z2 = false;
            while (it2.hasNext()) {
                TelephonyConnection telephonyConnection3 = (TelephonyConnection) it2.next();
                Log.d(this, "Adding a connection to a conference call: %s %s", this.mTelephonyConference, telephonyConnection3);
                if ((telephonyConnection3.getConnectionProperties() & 64) != 0) {
                    if (telephonyConnection3.getAddress() == null || isPhoneNumber(telephonyConnection3.getAddress().getSchemeSpecificPart())) {
                        Log.d(this, "Removing PROPERTY_IS_DOWNGRADED_CONFERENCE from connection %s", telephonyConnection3);
                        telephonyConnection3.setTelephonyConnectionProperties(telephonyConnection3.getConnectionProperties() & (-65));
                    }
                    z2 = true;
                }
                this.mTelephonyConference.addTelephonyConnection(telephonyConnection3);
            }
            if (z2) {
                TelephonyConference telephonyConference2 = this.mTelephonyConference;
                telephonyConference2.setConnectionProperties(telephonyConference2.getConnectionProperties() | 64);
                if (shouldRemoveManageConferenceForDowngradeConf(phone)) {
                    this.mTelephonyConference.removeCapability(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                }
            }
            this.mTelephonyConference.updateCallRadioTechAfterCreation();
            this.mConnectionService.addConference(this.mTelephonyConference);
        } else {
            Log.d(this, "Trigger recalculate later", new Object[0]);
            this.mTriggerRecalculate = true;
        }
        TelephonyConference telephonyConference3 = this.mTelephonyConference;
        if (telephonyConference3 != null) {
            Connection primaryConnection = telephonyConference3.getPrimaryConnection();
            Log.v(this, "Primary Conferenced connection is " + primaryConnection, new Object[0]);
            if (primaryConnection != null) {
                int state2 = primaryConnection.getState();
                if (state2 == 4) {
                    Log.v(this, "Setting conference to active", new Object[0]);
                    this.mTelephonyConference.setActive();
                } else {
                    if (state2 != 5) {
                        return;
                    }
                    Log.v(this, "Setting conference to hold", new Object[0]);
                    this.mTelephonyConference.setOnHold();
                }
            }
        }
    }

    private boolean isPhoneNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return Patterns.PHONE.matcher(str).matches();
    }

    private List getConferenceableConnections(Connection connection) {
        List<Conferenceable> conferenceables = connection.getConferenceables();
        ArrayList arrayList = new ArrayList();
        for (Conferenceable conferenceable : conferenceables) {
            if (conferenceable instanceof Connection) {
                arrayList.add((Connection) conferenceable);
            }
        }
        return arrayList;
    }

    private boolean shouldRemoveManageConferenceForDowngradeConf(Phone phone) {
        if (phone == null) {
            Log.v(this, "shouldRemoveManageConferenceForDowngradeConf: cannot get phone", new Object[0]);
            return false;
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            Log.v(this, "shouldRemoveManageConferenceForDowngradeConf: cannot get cfgM", new Object[0]);
            return false;
        }
        PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId());
        if (configForSubId == null) {
            Log.v(this, "shouldRemoveManageConferenceForDowngradeConf: cannot get Bundle", new Object[0]);
            return false;
        }
        boolean z = configForSubId.getBoolean("mtk_support_mng_conf_for_srvcc_conf_bool", true);
        StringBuilder sb = new StringBuilder("shouldRemoveManageConferenceForDowngradeConf = ");
        sb.append(!z);
        Log.d(this, sb.toString(), new Object[0]);
        return !z;
    }
}
