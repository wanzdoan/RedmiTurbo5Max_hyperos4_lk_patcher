package com.android.services.telephony;

import android.os.Handler;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.util.ArraySet;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
final class CdmaConferenceController {
    private CdmaConference mConference;
    private final TelephonyConnection.TelephonyConnectionListener mTelephonyConnectionListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.CdmaConferenceController.1
        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onStateChanged(Connection connection, int i) {
            CdmaConferenceController.this.recalculateConference();
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onDisconnected(Connection connection, DisconnectCause disconnectCause) {
            CdmaConferenceController.this.recalculateConference();
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onDestroyed(Connection connection) {
            CdmaConferenceController cdmaConferenceController = CdmaConferenceController.this;
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(connection);
            cdmaConferenceController.remove(null);
        }
    };
    private final List mCdmaConnections = new ArrayList();
    private final List mPendingOutgoingConnections = new ArrayList();
    private final Handler mHandler = new Handler();
    private final Set mPendingAddConnections = Collections.synchronizedSet(new ArraySet());

    public CdmaConferenceController(TelephonyConnectionService telephonyConnectionService) {
    }

    void remove(CdmaConnection cdmaConnection) {
        if (!this.mCdmaConnections.contains(cdmaConnection)) {
            Log.d(this, "remove - connection not tracked; connection=%s", cdmaConnection);
            return;
        }
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recalculateConference() {
        ArrayList arrayList = new ArrayList(this.mCdmaConnections.size());
        Iterator it = this.mCdmaConnections.iterator();
        if (it.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
        Log.d(this, "recalculating conference calls %d", Integer.valueOf(arrayList.size()));
        if (arrayList.size() >= 2) {
            List list = this.mCdmaConnections;
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(list.get(list.size() - 1));
            Log.i(this, "Creating new Cdma conference call", new Object[0]);
            throw null;
        }
        arrayList.isEmpty();
    }
}
