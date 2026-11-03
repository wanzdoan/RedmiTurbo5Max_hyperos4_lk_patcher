package com.android.internal.telephony;

import android.app.timezonedetector.TelephonyTimeZoneSuggestion;
import android.content.Context;
import android.os.Message;
import android.telephony.CellIdentity;
import android.telephony.CellInfo;
import android.telephony.MiStubParam;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.SparseArray;
import com.android.internal.telephony.cdnr.CarrierDisplayNameData;
import com.android.internal.telephony.nitz.TimeZoneSuggesterImpl;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.util.function.TriFunction;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MiuiNetworkControllerStub {
    private static int numPhones;
    private static volatile IMiuiNetworkController[] sInstances;

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiNetworkController[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiNetworkController get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiNetworkControllerStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiNetworkControllerStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }

    public interface IMiuiNetworkController {
        default CarrierDisplayNameData convertCarrierDisplayName(Phone phone, CarrierDisplayNameData carrierDisplayNameData) {
            return carrierDisplayNameData;
        }

        default int convertNetworkType(Phone phone, int i) {
            return i;
        }

        default void dispose(Phone phone) {
        }

        default void doRilMessageTimeOut(SparseArray<RILRequest> sparseArray) {
        }

        default void dump(Phone phone, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        }

        default String getDataOperatorNumeric(Phone phone, String str) {
            return str;
        }

        default String getPlmn(ServiceStateTracker serviceStateTracker) {
            return null;
        }

        default String getSpn(ServiceStateTracker serviceStateTracker) {
            return null;
        }

        default String getWfcDisplayPlmn(Phone phone, String str, String str2) {
            return str2;
        }

        default String getWfcDisplaySpn(Phone phone, String str, String str2) {
            return str2;
        }

        default String handleCarrierNameOverride(String str, int i, IccRecords iccRecords) {
            return str;
        }

        default void handleMccMncChange(Phone phone, ServiceState serviceState, ServiceState serviceState2) {
        }

        default void handleMessage(Phone phone, Message message) {
        }

        default void init(Phone phone, Object... objArr) {
        }

        default boolean isAlwaysSetNitzName(String str) {
            return false;
        }

        default boolean isDelayUpdateServiceState(Phone phone) {
            return false;
        }

        default boolean isDelayUpdateTelephonyDisplayInfo(DisplayInfoController displayInfoController, int i) {
            return false;
        }

        default boolean isOperatorConsideredNonRoaming(Phone phone, String str) {
            return false;
        }

        default boolean isValidNitz(int i, NitzSignal nitzSignal) {
            return false;
        }

        default String lookupOperatorNameEons(String str, int i, int i2, String str2, boolean z, TriFunction<Integer, String, Boolean, String> triFunction) {
            return null;
        }

        default boolean needCacheCellInfoList(Phone phone, CellIdentity cellIdentity, List<CellInfo> list) {
            return false;
        }

        default boolean needGUTIWork(Context context, ServiceStateTracker serviceStateTracker) {
            return false;
        }

        default void notifyNitzTimeChange(NitzData nitzData) {
        }

        default boolean processCacheCellInfoList(Phone phone, Message message, long j, int i, MiStubParam miStubParam) {
            return false;
        }

        default void resetServiceStateDisplay(Phone phone) {
        }

        default void startGUTITask(ServiceStateTracker serviceStateTracker) {
        }

        default void storeAndCheckTimeZone(int i, TelephonyTimeZoneSuggestion telephonyTimeZoneSuggestion, NitzSignal nitzSignal) {
        }

        default void updateModemResetFlag(boolean z) {
        }

        default void updateSignalPrimaryInNsa(SignalStrength signalStrength) {
        }

        default TelephonyTimeZoneSuggestion updateTimeZoneSuggesionFromCountryAndNitz(TimeZoneSuggesterImpl timeZoneSuggesterImpl, TelephonyTimeZoneSuggestion telephonyTimeZoneSuggestion, NitzSignal nitzSignal, String str) {
            return telephonyTimeZoneSuggestion;
        }

        default TelephonyTimeZoneSuggestion updateTimeZoneSuggesionFromNetworkCountryCode(TelephonyTimeZoneSuggestion telephonyTimeZoneSuggestion, String str) {
            return telephonyTimeZoneSuggestion;
        }

        default String getSelectNetworkManuallyOperatorNumeric(Phone phone, OperatorInfo operatorInfo) {
            return operatorInfo.getOperatorNumeric();
        }
    }
}
