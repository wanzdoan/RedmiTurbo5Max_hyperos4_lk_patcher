package com.android.internal.telephony;

import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;

/* JADX INFO: loaded from: classes.dex */
public class MiuiCallControllerStub {
    public static final int HANGUP_FROEGROUND_RESUME_BACKGROUND_CONN_INDEX = 101;
    public static final int HANGUP_WAITING_OR_BACKGROUND_CONN_INDEX = 100;
    private static int numPhones;
    private static volatile IMiuiCallController[] sInstances;

    public interface IMiuiCallController {
        default boolean addHangupPendingConnectionIndex(Phone phone, int i) {
            return false;
        }

        default void dispose(Phone phone) {
        }

        default ImsPhoneMmiCode getMmiCodeForKrOperator(Phone phone, String str, ImsPhoneMmiCode imsPhoneMmiCode, boolean z) {
            return imsPhoneMmiCode;
        }

        default void hangupImsCallIfNeeded(Phone phone, boolean z) {
        }

        default void init(Phone phone, Object... objArr) {
        }

        default boolean isBlockedMmi(Phone phone, String str) {
            return false;
        }

        default boolean isCLIREnableForbidden(Phone phone) {
            return false;
        }

        default boolean isVodafoneFACs(Phone phone, String str) {
            return false;
        }

        default boolean processCode(GsmMmiCode gsmMmiCode, Object... objArr) {
            return false;
        }

        default void removeAllHangupPendingConnectionIndex(Phone phone) {
        }

        default boolean sendSpeechCodecIsHDBroadcast(Phone phone, boolean z) {
            return z;
        }

        default int updateCfnryTime(int i, Phone phone, int i2) {
            return i2;
        }
    }

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiCallController[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiCallController get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiCallControllerStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiCallControllerStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }
}
