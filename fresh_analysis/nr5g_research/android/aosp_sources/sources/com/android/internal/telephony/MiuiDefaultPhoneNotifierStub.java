package com.android.internal.telephony;

import android.telephony.CellIdentity;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.TelephonyRegistryManager;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDefaultPhoneNotifierStub {
    private static volatile IMiuiDefaultPhoneNotifier sInstance;

    public static IMiuiDefaultPhoneNotifier get() {
        if (sInstance == null) {
            synchronized (MiuiDefaultPhoneNotifierStub.class) {
                try {
                    if (sInstance == null) {
                        sInstance = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiDefaultPhoneNotifierStub();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public interface IMiuiDefaultPhoneNotifier {
        default ServiceState handleServiceStateForAisSim(ServiceState serviceState) {
            return serviceState;
        }

        default void init(TelephonyRegistryManager telephonyRegistryManager) {
        }

        default void notifyCellLocation(Phone phone, CellIdentity cellIdentity) {
        }

        default void notifyDataConnection(Phone phone, PreciseDataConnectionState preciseDataConnectionState) {
        }

        default void notifyPreciseCallState(Phone phone) {
        }

        default void notifyServiceStateForSubId(Phone phone, ServiceState serviceState, int i) {
        }

        default boolean notifySignalStrength(Phone phone) {
            return true;
        }

        default HashMap<String, String> getSignalStrengthOptimizationCountMap() {
            return new HashMap<>();
        }
    }
}
