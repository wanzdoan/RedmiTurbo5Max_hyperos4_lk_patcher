package com.android.services.telephony.relay.phonecall;

import android.net.Uri;
import android.os.Bundle;
import android.telecom.Connection;
import android.text.TextUtils;
import com.android.phone.R;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class RelayConnectionsManager {
    private static final String TAG = "RelayConnectionsManager";
    private final Set mRelayConnections = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));
    private final Set mLocalConnections = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));
    private final Set mDisconnectedConnections = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));
    private final Set mRelayedLocalConnections = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));

    public void addLocalConnection(Connection connection) {
        if (connection == null || this.mLocalConnections.contains(connection)) {
            return;
        }
        this.mLocalConnections.add(connection);
        RelayLog.logi(TAG, "addLocalConnection");
    }

    public void removeLocalConnection(Connection connection) {
        if (this.mLocalConnections.contains(connection)) {
            this.mLocalConnections.remove(connection);
            RelayLog.logi(TAG, "removeLocalConnection");
        }
    }

    public Connection getLocalConnectionWithNumber(String str) {
        for (Connection connection : this.mLocalConnections) {
            Uri address = connection.getAddress();
            if (address != null) {
                String schemeSpecificPart = address.getSchemeSpecificPart();
                if (str != null && str.equals(schemeSpecificPart)) {
                    return connection;
                }
            }
        }
        RelayLog.logi(TAG, "getLocalConnectionWithNumber, no Connection was found!");
        return null;
    }

    public Connection getLocalConnectionCallRelayed() {
        for (Connection connection : this.mLocalConnections) {
            if (RelayUtils.Relay.isCallRelayed(connection)) {
                return connection;
            }
        }
        RelayLog.logi(TAG, "getLocalConnectionCallRelayed, no Connection was found!");
        return null;
    }

    public Connection getLocalConnectionCallRelayAnswered() {
        for (Connection connection : this.mLocalConnections) {
            if (RelayUtils.Relay.isCallRelayAnswered(connection)) {
                return connection;
            }
        }
        RelayLog.logi(TAG, "getLocalConnectionCallRelayAnswered, no Connection was found!");
        return null;
    }

    public Connection getLocalConnectionCallRelaying() {
        for (Connection connection : this.mLocalConnections) {
            if (RelayUtils.Relay.isCallRelaying(connection)) {
                return connection;
            }
        }
        RelayLog.logi(TAG, "getLocalConnectionCallRelaying, no Connection was found!");
        return null;
    }

    public Set getLocalConnections() {
        return this.mLocalConnections;
    }

    public void clearRelayConnections() {
        if (this.mRelayConnections.isEmpty()) {
            return;
        }
        RelayLog.logi(TAG, "clearRelayConnections");
        for (RelayConnection relayConnection : this.mRelayConnections) {
            RelayLog.logi(TAG, "clearRelayConnections connection:" + relayConnection);
            relayConnection.tearDown();
        }
        RelayUtils.System.showToast(R.string.relay_call_released_due_to_connect_lost);
        this.mRelayConnections.clear();
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_relay_call_lost");
    }

    public void clearLocalConnections() {
        if (this.mLocalConnections.isEmpty()) {
            return;
        }
        for (Connection connection : this.mLocalConnections) {
            Bundle bundle = new Bundle();
            Bundle extras = connection.getExtras();
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
            RelayLog.logi(TAG, "clearLocalConnections connection:" + connection);
        }
        this.mLocalConnections.clear();
    }

    public void tearDown() {
        clearRelayConnections();
        clearLocalConnections();
    }

    public void addRelayConnection(RelayConnection relayConnection) {
        if (relayConnection == null || this.mRelayConnections.contains(relayConnection)) {
            return;
        }
        RelayLog.logi(TAG, "addRelayConnection, relayConnection: " + relayConnection);
        this.mRelayConnections.add(relayConnection);
    }

    public void removeRelayConnection(RelayConnection relayConnection) {
        if (this.mRelayConnections.contains(relayConnection)) {
            RelayLog.logi(TAG, "removeRelayConnection, relayConnection: " + relayConnection);
            this.mRelayConnections.remove(relayConnection);
        }
    }

    public Set getRelayConnections() {
        return this.mRelayConnections;
    }

    public RelayConnection getRelayConnectionWithNumber(String str) {
        for (RelayConnection relayConnection : this.mRelayConnections) {
            Uri address = relayConnection.getAddress();
            if (address != null && !TextUtils.isEmpty(str) && str.equals(address.getSchemeSpecificPart())) {
                RelayLog.logi(TAG, "getRelayConnectionWithNumber, relayConnection:" + relayConnection);
                return relayConnection;
            }
        }
        RelayLog.logi(TAG, "getRelayConnectionWithNumber, no RelayConnection was found!");
        return null;
    }

    public boolean addDisconnectedConnection(String str) {
        return this.mDisconnectedConnections.add(str);
    }

    public boolean isDisconnectedConnection(String str) {
        return this.mDisconnectedConnections.contains(str);
    }

    public void clearDisconnectedConnection() {
        this.mDisconnectedConnections.clear();
    }

    public void addRelayedLocalConnection(Connection connection) {
        if (connection == null || this.mRelayedLocalConnections.contains(connection)) {
            return;
        }
        this.mRelayedLocalConnections.add(connection);
        RelayLog.logi(TAG, "addRelayedLocalConnection");
    }

    public void removeRelayedLocalConnection(Connection connection) {
        if (connection == null || !this.mRelayedLocalConnections.contains(connection)) {
            return;
        }
        this.mRelayedLocalConnections.remove(connection);
        RelayLog.logi(TAG, "removeRelayedLocalConnection");
    }

    public boolean isRelayedLocalConnection(Connection connection) {
        return connection != null && this.mRelayedLocalConnections.contains(connection);
    }
}
