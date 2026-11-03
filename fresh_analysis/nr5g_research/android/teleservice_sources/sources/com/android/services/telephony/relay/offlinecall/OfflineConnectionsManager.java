package com.android.services.telephony.relay.offlinecall;

import android.text.TextUtils;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class OfflineConnectionsManager {
    private static final String TAG = "OfflineConnectionsManager";
    private final Set mOfflineConnections = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));

    public void clearOfflineConnections() {
        if (this.mOfflineConnections.isEmpty()) {
            return;
        }
        OfflineCallLog.logi(TAG, "clearOfflineConnections");
        for (OfflineConnection offlineConnection : this.mOfflineConnections) {
            OfflineCallLog.logi(TAG, "clearOfflineConnections connection:" + offlineConnection);
            offlineConnection.tearDown();
        }
        this.mOfflineConnections.clear();
    }

    public void tearDown() {
        clearOfflineConnections();
    }

    public void addOfflineConnection(OfflineConnection offlineConnection) {
        if (offlineConnection == null || this.mOfflineConnections.contains(offlineConnection)) {
            return;
        }
        OfflineCallLog.logi(TAG, "addOfflineConnection, offlineConnection: " + offlineConnection);
        this.mOfflineConnections.add(offlineConnection);
    }

    public void removeOfflineConnection(OfflineConnection offlineConnection) {
        if (this.mOfflineConnections.contains(offlineConnection)) {
            OfflineCallLog.logi(TAG, "removeOfflineConnection, offlineConnection: " + offlineConnection);
            this.mOfflineConnections.remove(offlineConnection);
        }
    }

    public Set getOfflineConnections() {
        return this.mOfflineConnections;
    }

    public OfflineConnection getOfflineConnectionWithNumber(String str) {
        for (OfflineConnection offlineConnection : this.mOfflineConnections) {
            if (!TextUtils.isEmpty(str) && str.equals(offlineConnection.getAddress().getSchemeSpecificPart())) {
                OfflineCallLog.logi(TAG, "getOfflineConnectionWithNumber, offlineConnection:" + offlineConnection);
                return offlineConnection;
            }
        }
        OfflineCallLog.logi(TAG, "getOfflineConnectionWithNumber, no offlineConnection was found!");
        return null;
    }

    public OfflineConnection getConnectionOfflineCall() {
        for (OfflineConnection offlineConnection : this.mOfflineConnections) {
            if (offlineConnection != null) {
                return offlineConnection;
            }
        }
        OfflineCallLog.logi(TAG, "getConnectionOfflineCall, no Connection was found!");
        return null;
    }
}
