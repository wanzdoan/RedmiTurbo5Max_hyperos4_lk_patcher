package com.android.internal.telephony.data;

import android.content.Intent;
import com.android.internal.telephony.MiuiPhoneStub$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.Phone;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDataServiceManagerStub {
    private static int numPhones;
    private static volatile IMiuiDataServiceManager[] sInstances;

    public interface IMiuiDataServiceManager {
        default Intent getDataServiceIntent(int i, Intent intent, String str, String str2) {
            return intent;
        }

        default String getDataServicePackageName(int i, String str) {
            return str;
        }

        default void init(Phone phone, Object... objArr) {
        }
    }

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiDataServiceManager[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiDataServiceManager get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiDataServiceManagerStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiDataServiceManagerStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }
}
