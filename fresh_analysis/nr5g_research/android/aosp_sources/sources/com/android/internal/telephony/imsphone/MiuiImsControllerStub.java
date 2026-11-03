package com.android.internal.telephony.imsphone;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.MiuiPhoneStub$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.Phone;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;

/* JADX INFO: loaded from: classes.dex */
public class MiuiImsControllerStub {
    private static int numPhones;
    private static volatile IMiuiImsController[] sInstances;

    public interface IMiuiImsController {
        default void broadcastCallAutoReject(Phone phone, ImsCallProfile imsCallProfile, ImsReasonInfo imsReasonInfo) {
        }

        default void dispose(Phone phone) {
        }

        default int getClirModeForEmergencyCall(int i, Phone phone, boolean z, int i2) {
            return i2;
        }

        default String getEffectiveNumber(Phone phone, String str) {
            return str;
        }

        default void holdActiveCall(ImsPhoneCallTracker imsPhoneCallTracker, Object obj) {
        }

        default void holdActiveCallForWaitingCall(ImsPhoneCallTracker imsPhoneCallTracker, Object obj) {
        }

        default void init(Phone phone, Object... objArr) {
        }

        default boolean isCarrierConfigedBusyTone(Phone phone) {
            return false;
        }

        default boolean isCarrierConfigedLocalTone(Phone phone) {
            return false;
        }

        default boolean isCarrierSupportCbOverIms(Phone phone) {
            return false;
        }

        default boolean isCarrierSupportCbOverIms(Phone phone, PersistableBundle persistableBundle) {
            return false;
        }

        default boolean isCustForKrAndKrSimInserted(Phone phone) {
            return false;
        }

        default boolean isGwsdMode(Phone phone) {
            return false;
        }

        default boolean isPseudoDsdaCall(int i) {
            return false;
        }

        default boolean processCode(Phone phone, ImsPhoneMmiCode imsPhoneMmiCode, Object... objArr) {
            return false;
        }

        default void sendIMSRegistedStateBroadcast(boolean z, Phone phone) {
        }

        default void setActiveCallDisconnectedOnAnswer(Connection connection, int i) {
        }

        default boolean shallDialOnCircuitSwitch(int i, Bundle bundle) {
            return false;
        }

        default boolean shouldNotifyAutoRejectedCall(Phone phone, ImsReasonInfo imsReasonInfo) {
            return false;
        }
    }

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiImsController[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiImsController get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiImsControllerStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiImsControllerStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }
}
