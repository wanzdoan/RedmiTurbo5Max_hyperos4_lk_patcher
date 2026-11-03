package com.android.services.telephony;

import android.os.Bundle;
import android.telecom.Conference;
import android.telecom.Connection;
import android.telecom.PhoneAccountHandle;
import android.telephony.ServiceState;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelephonyConferenceBase extends Conference {
    private final Set mListeners;

    public abstract class TelephonyConferenceListener {
        public void onConferenceCapacityChanged() {
        }

        public abstract void onConferenceMembershipChanged(Connection connection);

        public void onDestroyed(Conference conference) {
        }

        public void onStateChanged(Conference conference, int i, int i2) {
        }
    }

    public void addTelephonyConferenceListener(TelephonyConferenceListener telephonyConferenceListener) {
        this.mListeners.add(telephonyConferenceListener);
    }

    public TelephonyConferenceBase(PhoneAccountHandle phoneAccountHandle) {
        super(phoneAccountHandle);
        this.mListeners = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));
    }

    public void addTelephonyConnection(Connection connection) {
        addConnection(connection);
        notifyConferenceMembershipChanged(connection);
    }

    public void removeTelephonyConnection(Connection connection) {
        removeConnection(connection);
        notifyConferenceMembershipChanged(connection);
    }

    public void destroyTelephonyConference() {
        Iterator<Connection> it = getConnections().iterator();
        while (it.hasNext()) {
            removeTelephonyConnection(it.next());
        }
        destroy();
        notifyDestroyed();
    }

    public final void setConferenceOnHold() {
        int state = getState();
        if (state == 5) {
            return;
        }
        setOnHold();
        notifyStateChanged(state, getState());
    }

    public final void setConferenceOnDialing() {
        int state = getState();
        if (state == 3) {
            return;
        }
        setDialing();
        notifyStateChanged(state, getState());
    }

    public final void setConferenceOnRinging() {
        int state = getState();
        if (state == 2) {
            return;
        }
        setRinging();
        notifyStateChanged(state, getState());
    }

    public final void setConferenceOnActive() {
        int state = getState();
        if (state == 4) {
            return;
        }
        setActive();
        notifyStateChanged(state, getState());
    }

    public void updateCallRadioTechAfterCreation() {
        Connection primaryConnection = getPrimaryConnection();
        if (primaryConnection != null && (primaryConnection instanceof TelephonyConnection)) {
            Bundle bundle = new Bundle();
            bundle.putInt("android.telecom.extra.CALL_NETWORK_TYPE", ServiceState.rilRadioTechnologyToNetworkType(((TelephonyConnection) primaryConnection).getCallRadioTech()));
            putExtras(bundle);
            return;
        }
        Log.w("TelephonyConferenceBase", "No primary connection found while updateCallRadioTechAfterCreation", new Object[0]);
    }

    public void removeCapability(int i) {
        setConnectionCapabilities((~i) & getConnectionCapabilities());
    }

    public void addCapability(int i) {
        setConnectionCapabilities(i | getConnectionCapabilities());
    }

    private void notifyConferenceMembershipChanged(Connection connection) {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConferenceListener) it.next()).onConferenceMembershipChanged(connection);
        }
    }

    public void notifyConferenceCapacityChanged() {
        this.mListeners.forEach(new Consumer() { // from class: com.android.services.telephony.TelephonyConferenceBase$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((TelephonyConferenceBase.TelephonyConferenceListener) obj).onConferenceCapacityChanged();
            }
        });
    }

    private void notifyDestroyed() {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConferenceListener) it.next()).onDestroyed(this);
        }
    }

    private void notifyStateChanged(int i, int i2) {
        if (i != i2) {
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((TelephonyConferenceListener) it.next()).onStateChanged(this, i, i2);
            }
        }
    }
}
