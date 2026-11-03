package com.mediatek.internal.telephony;

import com.android.internal.telephony.SmsHeader;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsHeader extends SmsHeader {
    public static final int CONCATENATED_8_BIT_REFERENCE_LENGTH = 5;
    public static final int NATIONAL_LANGUAGE_LOCKING_SHIFT_LENGTH = 3;
    public static final int NATIONAL_LANGUAGE_SINGLE_SHIFT_LENGTH = 3;
    private static final String TAG = "SmsHeader";

    public static byte[] getSubmitPduHeader(int destPort, int originalPort) {
        return getSubmitPduHeaderWithLang(destPort, originalPort, 0, 0, 0, -1, -1);
    }

    public static byte[] getSubmitPduHeader(int destPort, int refNumber, int seqNumber, int msgCount) {
        return getSubmitPduHeaderWithLang(destPort, refNumber, seqNumber, msgCount, -1, -1);
    }

    public static byte[] getSubmitPduHeaderWithLang(int destPort, int singleShiftId, int lockingShiftId) {
        return getSubmitPduHeaderWithLang(destPort, 0, 0, 0, singleShiftId, lockingShiftId);
    }

    public static byte[] getSubmitPduHeaderWithLang(int destPort, int refNumber, int seqNumber, int msgCount, int singleShiftId, int lockingShiftId) {
        return getSubmitPduHeaderWithLang(destPort, 0, refNumber, seqNumber, msgCount, singleShiftId, lockingShiftId);
    }

    public static byte[] getSubmitPduHeaderWithLang(int destPort, int originalPort, int refNumber, int seqNumber, int msgCount, int singleShiftId, int lockingShiftId) {
        MtkSmsHeader smsHeader = new MtkSmsHeader();
        if (destPort >= 0) {
            SmsHeader.PortAddrs portAddrs = new SmsHeader.PortAddrs();
            portAddrs.destPort = destPort;
            portAddrs.origPort = originalPort;
            portAddrs.areEightBits = false;
            smsHeader.portAddrs = portAddrs;
        }
        if (msgCount > 0) {
            SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
            concatRef.refNumber = refNumber;
            concatRef.seqNumber = seqNumber;
            concatRef.msgCount = msgCount;
            concatRef.isEightBits = true;
            smsHeader.concatRef = concatRef;
        }
        if (singleShiftId > 0 || lockingShiftId > 0) {
            smsHeader.languageShiftTable = singleShiftId;
            smsHeader.languageTable = lockingShiftId;
        }
        return SmsHeader.toByteArray(smsHeader);
    }
}
