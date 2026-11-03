package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Message;
import android.telephony.MiStubParam;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPhoneStub {
    private static int numPhones;
    private static volatile IMiuiPhone[] sInstances;

    public interface IMiuiPhone {
        default boolean disableSecondarySim(int i, boolean z) {
            return false;
        }

        default void dispose(Phone phone) {
        }

        default int forceSetLteonlyAndIgnoreUIPrefer(int i, int i2) {
            return i2;
        }

        default boolean forceUseCsForCall(Phone phone, String str) {
            return false;
        }

        default int generatorDeviceStateNewFilter(Phone phone, int i, int i2) {
            return i2;
        }

        default String getIccId(int i) {
            return null;
        }

        default String getLine1Number(String str, Phone phone) {
            return str;
        }

        default boolean getVMNumberIgnoreSimRecords(Phone phone, String str) {
            return false;
        }

        default void handleMessage(Phone phone, Message message, MiStubParam miStubParam) {
        }

        default void init(Phone phone, Object... objArr) {
        }

        default void injectDeviceStateMonitor(Phone phone, DeviceStateMonitor deviceStateMonitor) {
        }

        default boolean isActiveSubId(int i, Phone phone) {
            return false;
        }

        default boolean isNeedUpdateDeviceState(Phone phone, int i, MiStubParam miStubParam) {
            return false;
        }

        default boolean isRadioOffForced(int i, boolean z, boolean z2, MiStubParam miStubParam) {
            return false;
        }

        default boolean isUssdRelease(Phone phone, int i) {
            return false;
        }

        default boolean needSendUssdWhenAPM(Phone phone, String str) {
            return false;
        }

        default void notifyDeviceStateListeners(Phone phone, int i, boolean z, boolean z2, boolean z3) {
        }

        default void notifyDeviceStateListeners(Phone phone, boolean z, boolean z2, boolean z3) {
        }

        default void notifyHandoverProvider(Context context, int i, int i2) {
        }

        default boolean notifyStkProCmdForEsimBinding(String str) {
            return false;
        }

        default ActivityInfo overrideBestEuiccUiActivity(PackageManager packageManager, Intent intent, ActivityInfo activityInfo, List<ResolveInfo> list) {
            return null;
        }

        default void purgeGuti(Context context, String str, int i) {
        }

        default void updateAllowedNetworkTypesFromDb(Phone phone) {
        }
    }

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiPhone[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiPhone get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiPhoneStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiPhoneStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }
}
