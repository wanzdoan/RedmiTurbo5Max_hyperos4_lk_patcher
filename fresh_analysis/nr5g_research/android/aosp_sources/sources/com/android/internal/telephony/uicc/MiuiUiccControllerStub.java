package com.android.internal.telephony.uicc;

import com.android.internal.telephony.MiuiPhoneStub$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.Phone;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MiuiUiccControllerStub {
    private static int numPhones;
    private static volatile IMiuiUiccController[] sInstances;

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiUiccController[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiUiccController get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiUiccControllerStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiUiccControllerStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }

    public interface IMiuiUiccController {
        default void broadcastIccRefresh(int i, IccRefreshResponse iccRefreshResponse) {
        }

        default void dispose(Phone phone) {
        }

        default void disposeIccRecord(int i, UiccCardApplication uiccCardApplication) {
        }

        default int getMncLength(int i, String str, String[] strArr) {
            return 0;
        }

        default int getRecoveryMncLength(int i, String str, int i2) {
            return i2;
        }

        default void init(Phone phone, Object... objArr) {
        }

        default void initIccRecord(int i, UiccCardApplication uiccCardApplication, IccFileHandler iccFileHandler, Object... objArr) {
        }

        default boolean isNumberBlockedByFDNMiui(int i, String str, String str2) {
            return false;
        }

        default String lookupOperatorName(String str, int i, boolean z) {
            return str;
        }

        default boolean mvnoMatchesCustomized(int i, IccRecords iccRecords, String str, String str2) {
            return false;
        }

        default String onOperatorNumericOrNameSet(int i, String str, String str2) {
            return str2;
        }

        default void onSimRefresh(int i, UiccProfile uiccProfile, UiccSlot uiccSlot) {
        }

        default String overrideSpnFromConfig(int i, String str, IccRecords iccRecords) {
            return str;
        }

        default void resetIccRecord(int i, UiccCardApplication uiccCardApplication) {
        }

        default boolean resetIccRecordFileUpdate(int i, UiccCardApplication uiccCardApplication, int i2) {
            return false;
        }

        default String resetIccid(int i, String str, byte[] bArr) {
            return str;
        }

        default void setTelephonyProperty(int i, String str, String str2) {
        }

        default void updateIccRecords(int i, IccRecords iccRecords) {
        }

        default ArrayList<AdnRecord> getRecordsIfLoaded(Phone phone, IccRecords iccRecords, int i) {
            return new ArrayList<>();
        }
    }
}
