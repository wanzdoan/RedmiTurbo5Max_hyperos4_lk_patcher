package com.android.internal.telephony;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import com.android.internal.telephony.gsm.SmsMessage;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSmsControllerStub {
    public static final String CALLING_PACKAGE_NAME_KEY = "calling_package_name";
    protected static final int OPTIMIZATION_INIT = 0;
    protected static final int OPTIMIZATION_READY = 2;
    protected static final int OPTIMIZATION_START = 1;
    protected static final int SMS_OPTIMIZATION_TYPE_DONE = 2;
    protected static final int SMS_OPTIMIZATION_TYPE_ENABLE = 1;
    protected static final int SMS_OPTIMIZATION_TYPE_INIT = 0;
    private static int numPhones;
    private static volatile IMiuiSmsController[] sInstances;

    public interface IMiuiSmsController {
        default boolean beforeDispatchIntent(InboundSmsHandler inboundSmsHandler, Intent intent, String str, int i, String str2, String[] strArr) {
            return false;
        }

        default boolean carrierDisableSendSmsOverIms(Phone phone) {
            return false;
        }

        default boolean containsSmscOptimization(Phone phone, SMSDispatcher.SmsTracker smsTracker) {
            return false;
        }

        default void dispose(Phone phone) {
        }

        default void enableSynced(Phone phone, SmsDispatchersController smsDispatchersController, boolean z) {
        }

        default void enqueueOrSendSms(Phone phone, SMSDispatcher.SmsTracker smsTracker, SmsDispatchersController smsDispatchersController) {
        }

        default PackageInfo getCallerAppInfo(SMSDispatcher sMSDispatcher, HashMap<String, Object> map, PackageInfo packageInfo) {
            return packageInfo;
        }

        default SMSDispatcher getNtnSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
            return null;
        }

        default String getSmscAddressResponse(RIL ril, String str, Object... objArr) {
            return str;
        }

        default SmsMessage.SubmitPdu getSubmitPdu(Phone phone, String str, String str2, byte[] bArr, boolean z, SmsMessageBase.SubmitPduBase submitPduBase, int i, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
            return null;
        }

        default void init(Phone phone, SmsDispatchersController smsDispatchersController, Object... objArr) {
        }

        default boolean isBlocked(Phone phone, String str, Bundle bundle, long j, boolean z) {
            return z;
        }

        default boolean isBlocked(Phone phone, String str, boolean z) {
            return z;
        }

        default boolean isCTAutoRegMsg(Phone phone, SmsMessage smsMessage) {
            return false;
        }

        default boolean isNeedSendCsRetry(Phone phone, SMSDispatcher.SmsTracker smsTracker, int i, boolean z) {
            return false;
        }

        default boolean isNeedSendCsSMS(Phone phone, SMSDispatcher.SmsTracker smsTracker, String str) {
            return false;
        }

        default boolean isSmsSaOptimization() {
            return false;
        }

        default boolean isWapPushForMms(byte[] bArr, InboundSmsHandler inboundSmsHandler, InboundSmsTracker inboundSmsTracker, WapPushOverSms wapPushOverSms) {
            return false;
        }

        default int miuiSmsSendOptimizationStatus(Phone phone, SMSDispatcher.SmsTracker smsTracker, Map<Integer, SMSDispatcher.SmsTracker> map, String str, boolean z, boolean z2, int i, SmsDispatchersController smsDispatchersController) {
            return -1;
        }

        default void newNtnBoxSms() {
        }

        default boolean ntnInboundSmsHandlerIsWaiting(Phone phone) {
            return false;
        }

        default void processNextSms(Phone phone, SmsDispatchersController smsDispatchersController) {
        }

        default String reMapSmscNumber(Phone phone, String str) {
            return str;
        }

        default String replaceSmscAddr(Phone phone, String str, String str2) {
            return str2;
        }

        default void sendSmsDotBroadcast(Phone phone, SMSDispatcher.SmsTracker smsTracker, String str, int i, boolean z) {
        }

        default String setSmscAddress(Context context, int i, String str) {
            return str;
        }

        default boolean shouldCheckSmsUsage(Phone phone, String str) {
            return false;
        }

        default boolean smsSendRestricted(Phone phone, String str) {
            return false;
        }

        default boolean startConsentUiIfNeeded(Context context, String str, int i, String str2, String str3, String str4, String str5, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, long j) {
            return false;
        }
    }

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiSmsController[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiSmsController get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiSmsControllerStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiSmsControllerStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }
}
