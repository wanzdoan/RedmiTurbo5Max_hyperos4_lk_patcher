package com.android.internal.telephony;

import android.content.Context;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;

/* JADX INFO: loaded from: classes.dex */
public class MiuiRadioControllerStub {
    private static int numPhones;
    private static volatile IMiuiRadioController[] sInstances;

    public interface IMiuiRadioController {
        default void dispose(RIL ril) {
        }

        default void doRilMessageTimeOut() {
        }

        default void init(RIL ril, Context context, Object... objArr) {
        }

        default void processIndication() {
        }

        default void processResponseInternal() {
        }

        default boolean skipRadioStateIfNeed(int i) {
            return false;
        }
    }

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiRadioController[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiRadioController get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiRadioControllerStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiRadioControllerStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }
}
