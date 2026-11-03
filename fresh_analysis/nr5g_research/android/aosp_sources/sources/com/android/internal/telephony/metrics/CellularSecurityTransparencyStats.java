package com.android.internal.telephony.metrics;

import android.telephony.CellularIdentifierDisclosure;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class CellularSecurityTransparencyStats {
    /* JADX WARN: Code duplicated, block: B:7:0x0031  */
    public void logIdentifierDisclosure(CellularIdentifierDisclosure cellularIdentifierDisclosure, String str, String str2, boolean z) {
        int i;
        int plmnPartOrDefault;
        int plmnPartOrDefault2 = parsePlmnPartOrDefault(str, "SIM MCC");
        int plmnPartOrDefault3 = parsePlmnPartOrDefault(str2, "SIM MNC");
        String plmn = cellularIdentifierDisclosure.getPlmn();
        if (plmn != null) {
            String[] strArrSplit = plmn.split("-");
            if (strArrSplit.length == 2) {
                int plmnPartOrDefault4 = parsePlmnPartOrDefault(strArrSplit[0], "disclosure MCC");
                plmnPartOrDefault = parsePlmnPartOrDefault(strArrSplit[1], "disclosure MNC");
                i = plmnPartOrDefault4;
            } else {
                i = -1;
                plmnPartOrDefault = -1;
            }
        } else {
            i = -1;
            plmnPartOrDefault = -1;
        }
        writeIdentifierDisclosure(plmnPartOrDefault2, plmnPartOrDefault3, i, plmnPartOrDefault, cellularIdentifierDisclosure.getCellularIdentifier(), cellularIdentifierDisclosure.getNasProtocolMessage(), cellularIdentifierDisclosure.isEmergency(), z);
    }

    private int parsePlmnPartOrDefault(String str, String str2) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            Rlog.d("CellularSecurityTransparencyStats", "Failed to parse " + str2 + ": " + str);
            return -1;
        }
    }

    public void writeIdentifierDisclosure(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2) {
        TelephonyStatsLog.write(NtnRILConstants.MI_NTN_RIL_RESPONSE_ACKNOWLEDGEMENT, i, i2, i3, i4, i5, i6, z, z2);
    }
}
