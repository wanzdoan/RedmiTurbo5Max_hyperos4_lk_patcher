package com.android.internal.telephony.utils;

import android.telephony.TelephonyManager;
import com.miui.misight.MiEvent;
import com.miui.misight.MiEventStub;
import com.miui.misight.MiSight;
import com.miui.misight.MiSightStub;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class MiEventUtils {
    public static final int APN_EXCEPTION_EVENT_CODE = 915000003;
    public static final String CAUSE_COMMAND_SEND_EXCEPTION = "CommandSendException";
    public static final String CAUSE_IN_INTERNATIONAL = "InInternational";
    public static final String CAUSE_IN_PSEUDODSDA = "InPseudoDsda";
    public static final String CAUSE_IN_SWITCH_DSDS = "InSwitchDsds";
    public static final String CAUSE_IN_WEAK_NR_RSRP = "InWeakNrRsrp";
    public static final String CAUSE_STATUS_REPORT_EXCEPTION = "StatusReportException";
    public static final String EVENT_REASON = "EventReason";
    public static final int HANG_UP_CALL_FOR_DSDA_EVENT_CODE = 915200003;
    public static final int INVALID_CARD_EVENT_CODE = 915300006;
    public static final int NO_RINGTONE_FOR_INCOMING_CALLS_EVENT_CODE = 915200006;
    public static final String PARAM_COLUME_OVER_ZERO = "ColumeOverZero";
    public static final String PARAM_CRS_CALL = "CRSCall";
    public static final String PARAM_DND = "DND";
    public static final String PARAM_OPERATOR_NUMERIC = "OperatorNumeric";
    public static final String PARAM_PHONE_ID = "PhoneId";
    public static final String PARAM_RINGING_URI_IS_NULL = "RingingUriIsNull";
    public static final String PARAM_RINGTONE_PLAYED = "RingtonePlayed";
    public static final String PARAM_RINGTONE_VOLUME = "RingtoneVolume";
    public static final String PARAM_VOICE_REPORT = "VoiceReport";
    public static final String PROBLEM_CODE = "ProblemCode";
    public static final String RECOVERY_RESULT = "RecoveryResult";
    private static final String TAG = "MiEventUtils";
    public static final int USER_HANG_UP_PHONE_EVENT_CODE = 915200001;
    public static final int USER_HANG_UP_PHONE_RECOVERY_CODE = 815200001;

    public static MiEvent constructEvent(int eventCode) {
        MiEvent event = new MiEvent(eventCode);
        return event;
    }

    public static void trackerMiEvent(int eventCode, int phoneId) {
        MiEvent event = constructEvent(eventCode);
        event.addStr(PARAM_OPERATOR_NUMERIC, getSimOperator(phoneId));
        event.addStr(PARAM_PHONE_ID, String.valueOf(phoneId));
        sendEvent(event);
    }

    public static void trackerRecoveryMiEvent(int eventCode, int problemCode) {
        trackerRecoveryMiEvent(eventCode, problemCode, false);
    }

    public static void trackerRecoveryMiEvent(int eventCode, int problemCode, boolean recoveryResult) {
        MiEvent event = constructEvent(eventCode);
        event.addLong(PROBLEM_CODE, problemCode);
        event.addBool(RECOVERY_RESULT, recoveryResult);
        sendEvent(event);
    }

    public static void sendEvent(MiEvent event) {
        if (TelephonyManagerEx.getDefault().isMievnetSupported()) {
            MiSight.sendEvent(event);
        }
    }

    public static void sendEvent(MiEventStub event) {
        if (TelephonyManagerEx.getDefault().isMievnetSupported()) {
            MiSightStub.get().sendEvent(event);
        }
    }

    public static String getSimOperator(int phoneId) {
        TelephonyManager telephonyManager = TelephonyManager.getDefault();
        return telephonyManager.getSimOperatorNumericForPhone(phoneId);
    }
}
