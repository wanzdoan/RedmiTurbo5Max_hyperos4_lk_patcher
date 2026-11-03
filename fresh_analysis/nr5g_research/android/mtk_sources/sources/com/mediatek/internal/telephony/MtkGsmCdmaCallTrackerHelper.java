package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.util.CallLogUtils;
import java.util.Date;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class MtkGsmCdmaCallTrackerHelper {
    protected static final int EVENT_CALL_ADDITIONAL_INFO = 1006;
    protected static final int EVENT_CALL_STATE_CHANGE = 2;
    protected static final int EVENT_CONFERENCE_RESULT = 11;
    protected static final int EVENT_ECT_RESULT = 13;
    protected static final int EVENT_GET_LAST_CALL_FAIL_CAUSE = 5;
    protected static final int EVENT_MTK_BASE = 1000;
    protected static final int EVENT_OPERATION_COMPLETE = 4;
    protected static final int EVENT_POLL_CALLS_RESULT = 1;
    protected static final int EVENT_RADIO_AVAILABLE = 9;
    protected static final int EVENT_RADIO_NOT_AVAILABLE = 10;
    protected static final int EVENT_REPOLL_AFTER_DELAY = 3;
    protected static final int EVENT_SEPARATE_RESULT = 12;
    protected static final int EVENT_SWITCH_RESULT = 8;
    static final String LOG_TAG = "GsmCallTkrHlpr";
    private static final int MT_CALL_GWSD = 10;
    private static final int MT_CALL_MISSED = 2;
    private static final int MT_CALL_REJECTED = 1;
    private Context mContext;
    private boolean mIsGwsdCall = false;
    private MtkGsmCdmaCallTracker mMtkTracker;

    public MtkGsmCdmaCallTrackerHelper(Context context, MtkGsmCdmaCallTracker tracker) {
        this.mContext = context;
        this.mMtkTracker = tracker;
    }

    void logD(String msg) {
        Rlog.d(LOG_TAG, msg + " (slot " + this.mMtkTracker.mPhone.getPhoneId() + ")");
    }

    void logI(String msg) {
        Rlog.i(LOG_TAG, msg + " (slot " + this.mMtkTracker.mPhone.getPhoneId() + ")");
    }

    void logW(String msg) {
        Rlog.w(LOG_TAG, msg + " (slot " + this.mMtkTracker.mPhone.getPhoneId() + ")");
    }

    void logE(String msg) {
        Rlog.e(LOG_TAG, msg + " (slot " + this.mMtkTracker.mPhone.getPhoneId() + ")");
    }

    public void LogerMessage(int msgType) {
        switch (msgType) {
            case 1:
                logD("handle EVENT_POLL_CALLS_RESULT");
                break;
            case 2:
                logD("handle EVENT_CALL_STATE_CHANGE");
                break;
            case 3:
                logD("handle EVENT_REPOLL_AFTER_DELAY");
                break;
            case 4:
                logD("handle EVENT_OPERATION_COMPLETE");
                break;
            case 5:
                logD("handle EVENT_GET_LAST_CALL_FAIL_CAUSE");
                break;
            case 6:
            case 7:
            default:
                logD("handle XXXXX");
                break;
            case 8:
                logD("handle EVENT_SWITCH_RESULT");
                break;
            case 9:
                logD("handle EVENT_RADIO_AVAILABLE");
                break;
            case 10:
                logD("handle EVENT_RADIO_NOT_AVAILABLE");
                break;
            case 11:
                logD("handle EVENT_CONFERENCE_RESULT");
                break;
            case 12:
                logD("handle EVENT_SEPARATE_RESULT");
                break;
            case 13:
                logD("handle EVENT_ECT_RESULT");
                break;
        }
    }

    private void addCallLog(Context context, String iccId, String number, int type, long missedReason) {
        PhoneAccountHandle phoneAccountHandle;
        String number2;
        int presentationMode;
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(TelecomManager.class);
        Iterator<PhoneAccountHandle> phoneAccounts = telecomManager.getCallCapablePhoneAccounts().listIterator();
        while (true) {
            if (!phoneAccounts.hasNext()) {
                phoneAccountHandle = null;
                break;
            }
            PhoneAccountHandle handle = phoneAccounts.next();
            String id = handle.getId();
            if (id != null && id.equals(iccId)) {
                phoneAccountHandle = handle;
                break;
            }
        }
        if (number != null) {
            number2 = number;
        } else {
            number2 = "";
        }
        if (number2 == null || number2.equals("")) {
            presentationMode = 2;
        } else {
            presentationMode = 1;
        }
        CallLogUtils.addCall(null, context, number2, presentationMode, type, 0, phoneAccountHandle, new Date().getTime(), 0, new Long(0L), missedReason, 0);
    }

    public void handleCallAdditionalInfo(AsyncResult ar) {
        String[] callAdditionalInfo = (String[]) ar.result;
        int type = Integer.parseInt(callAdditionalInfo[0]);
        if (type != 2 && type != 1 && type != 10) {
            logD("handleCallAdditionalInfo not handle event");
        }
        String number = callAdditionalInfo[1];
        int callMode = Integer.parseInt(callAdditionalInfo[2]);
        logD("handleCallAdditionalInfo type:" + type + " mode:" + callMode);
        switch (type) {
            case 1:
                addCallLog(this.mContext, this.mMtkTracker.mPhone.getIccSerialNumber(), number, 5, 0L);
                break;
            case 2:
                addCallLog(this.mContext, this.mMtkTracker.mPhone.getIccSerialNumber(), number, 3, 65536L);
                break;
            case 10:
                this.mIsGwsdCall = true;
                break;
        }
    }

    public boolean isGwsdCall() {
        return this.mIsGwsdCall;
    }

    public void setGwsdCall(boolean isGwsd) {
        this.mIsGwsdCall = isGwsd;
    }
}
