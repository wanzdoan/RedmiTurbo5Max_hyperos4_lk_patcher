package com.android.internal.telephony.metrics;

import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.nano.PersistAtomsProto$CarrierIdMismatch;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class CarrierIdMatchStats {
    private static final String TAG = "CarrierIdMatchStats";

    public static void onCarrierIdMismatch(int i, String str, String str2, String str3, String str4) {
        PersistAtomsStorage atomsStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
        PersistAtomsProto$CarrierIdMismatch persistAtomsProto$CarrierIdMismatch = new PersistAtomsProto$CarrierIdMismatch();
        persistAtomsProto$CarrierIdMismatch.mccMnc = nullToEmpty(str);
        persistAtomsProto$CarrierIdMismatch.gid1 = nullToEmpty(str2);
        String strNullToEmpty = nullToEmpty(str3);
        persistAtomsProto$CarrierIdMismatch.spn = strNullToEmpty;
        persistAtomsProto$CarrierIdMismatch.pnn = strNullToEmpty.isEmpty() ? nullToEmpty(str4) : "";
        if (atomsStorage.addCarrierIdMismatch(persistAtomsProto$CarrierIdMismatch)) {
            Rlog.d(TAG, "New carrier ID mismatch event: " + persistAtomsProto$CarrierIdMismatch.toString());
            TelephonyStatsLog.write(313, i, str, str2, str3, str4);
        }
    }

    public static void sendCarrierIdTableVersion(int i) {
        if (PhoneFactory.getMetricsCollector().getAtomsStorage().setCarrierIdTableVersion(i)) {
            Rlog.d(TAG, "New carrier ID table version: " + i);
            TelephonyStatsLog.write(314, i);
        }
    }

    private static String nullToEmpty(String str) {
        return str != null ? str : "";
    }
}
