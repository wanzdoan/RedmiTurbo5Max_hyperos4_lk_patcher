package com.android.internal.telephony.metrics;

import android.net.NetworkRequest;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.nano.PersistAtomsProto$NetworkRequestsV2;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRequestsStats {
    public static void addNetworkRequest(NetworkRequest networkRequest, int i) {
        PersistAtomsStorage atomsStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
        PersistAtomsProto$NetworkRequestsV2 persistAtomsProto$NetworkRequestsV2 = new PersistAtomsProto$NetworkRequestsV2();
        persistAtomsProto$NetworkRequestsV2.carrierId = getCarrierId(i);
        persistAtomsProto$NetworkRequestsV2.requestCount = 1;
        if (networkRequest.hasCapability(34)) {
            persistAtomsProto$NetworkRequestsV2.capability = 1;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(35)) {
            persistAtomsProto$NetworkRequestsV2.capability = 2;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(38)) {
            persistAtomsProto$NetworkRequestsV2.capability = 11;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(5)) {
            persistAtomsProto$NetworkRequestsV2.capability = 3;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(29)) {
            persistAtomsProto$NetworkRequestsV2.capability = 4;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (!networkRequest.hasTransport(10) || networkRequest.hasCapability(13)) {
            return;
        }
        if (networkRequest.hasCapability(12)) {
            persistAtomsProto$NetworkRequestsV2.capability = 5;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(0)) {
            persistAtomsProto$NetworkRequestsV2.capability = 6;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(4)) {
            persistAtomsProto$NetworkRequestsV2.capability = 7;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(9)) {
            persistAtomsProto$NetworkRequestsV2.capability = 8;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(10)) {
            persistAtomsProto$NetworkRequestsV2.capability = 9;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
        if (networkRequest.hasCapability(1)) {
            persistAtomsProto$NetworkRequestsV2.capability = 10;
            atomsStorage.addNetworkRequestsV2(persistAtomsProto$NetworkRequestsV2);
        }
    }

    private static int getCarrierId(int i) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        if (phone != null) {
            return phone.getCarrierId();
        }
        return -1;
    }
}
