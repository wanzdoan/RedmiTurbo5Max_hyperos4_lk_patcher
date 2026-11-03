package com.android.internal.telephony.data;

import android.net.LinkProperties;
import android.net.NetworkAgentConfig;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Message;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import com.android.internal.telephony.MiuiPhoneStub$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.Phone;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.net.InetAddress;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDataControllerStub {
    private static int numPhones;
    private static volatile IMiuiDataController[] sInstances;

    public interface IMiuiDataController {
        default void addIAApnSetting(Phone phone, String str, List<DataProfile> list) {
        }

        default boolean attachNetworkRequests(DataNetworkController.NetworkRequestList networkRequestList) {
            return false;
        }

        default void createNetworkAgent(DataNetwork dataNetwork, NetworkAgentConfig.Builder builder) {
        }

        default void dispose(Phone phone) {
        }

        default DataEvaluation evaluateDataNetwork(Phone phone, DataEvaluation dataEvaluation) {
            return dataEvaluation;
        }

        default DataEvaluation evaluateNetworkRequest(Phone phone, TelephonyNetworkRequest telephonyNetworkRequest, DataEvaluation dataEvaluation) {
            return dataEvaluation;
        }

        default boolean fillDnsByDefault(Phone phone, LinkProperties linkProperties) {
            return false;
        }

        default void filterSpecialDunApn(Phone phone, List<DataProfile> list, DataProfile dataProfile, TelephonyNetworkRequest telephonyNetworkRequest) {
        }

        default boolean findSatisfiableNetworkRequests(DataNetworkController.NetworkRequestList networkRequestList) {
            return false;
        }

        default ApnSetting getDataProfileForNetworkRequest(Phone phone, List<DataProfile> list, ApnSetting apnSetting, TelephonyNetworkRequest telephonyNetworkRequest) {
            return apnSetting;
        }

        default long getRetryDelayMillis(long j, int i) {
            return j;
        }

        default void handleMessage(Phone phone, Message message) {
        }

        default boolean ignoreDataSetupRetry(Phone phone, DataProfile dataProfile, int i) {
            return false;
        }

        default boolean ignoreTetheringDataProfile(Phone phone) {
            return false;
        }

        default void init(Phone phone, List<DataNetwork> list, Object... objArr) {
        }

        default boolean isAllowedOnRoaming(Phone phone, boolean z) {
            return z;
        }

        default boolean isDataRoamingEnabled(Phone phone) {
            return false;
        }

        default boolean isNotNeedPreferredApn(Phone phone, TelephonyNetworkRequest telephonyNetworkRequest) {
            return false;
        }

        default boolean isRebootModem() {
            return false;
        }

        default boolean isRecoveryNotNeeded(Phone phone, boolean z, Object obj) {
            return false;
        }

        default void markApnPermanentFail(String str, String str2, int i, String str3) {
        }

        default void markApnPermanentFailReset(String str, int i, String str2) {
        }

        default void markRetryRuleReset(int i, String str) {
        }

        default void markSetupRetryMax(int[] iArr, String str, int i, int i2) {
        }

        default void onAllApnProfileChanged(Phone phone, List<ApnSetting> list) {
        }

        default void onDataCallListChanged(Phone phone, int i, String str) {
        }

        default long onDataEvaluationFailed(Phone phone, TelephonyNetworkRequest telephonyNetworkRequest, DataEvaluation dataEvaluation, DataEvaluation.DataEvaluationReason dataEvaluationReason) {
            return 0L;
        }

        default void onDataNetworkConnected(Phone phone, DataNetwork dataNetwork) {
        }

        default void onDataNetworkDisconnected(Phone phone, DataNetwork dataNetwork, int i) {
        }

        default void onDataNetworkDisconnected(Phone phone, DataNetwork dataNetwork, int i, int i2) {
        }

        default long onDataNetworkSetupFailed(Phone phone, DataNetwork dataNetwork, DataNetworkController.NetworkRequestList networkRequestList, int i, long j) {
            return j;
        }

        default void onDataNetworkValidationStatusChanged(Phone phone, DataNetwork dataNetwork, int i, Uri uri) {
        }

        default void onDataStallReestablishInternet(Phone phone) {
        }

        default boolean onDeactivateResponse(DataNetwork dataNetwork) {
            return false;
        }

        default void onDetachNetworkRequest(DataNetwork dataNetwork) {
        }

        default void onInternetValidationStatusChanged(Phone phone, DataStallRecoveryManager dataStallRecoveryManager, int i) {
        }

        default void onRemoveNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        }

        default void onSetupResponse(DataNetwork dataNetwork, int i, DataCallResponse dataCallResponse) {
        }

        default void onTearDown(DataNetwork dataNetwork, int i) {
        }

        default void recordDataInterruptionRecoveryMeasures(Phone phone, boolean z, String str, int i, int i2) {
        }

        default void removeWapApn(List<DataProfile> list, DataProfile dataProfile, TelephonyNetworkRequest telephonyNetworkRequest) {
        }

        default boolean setInitialAttachApn(Phone phone, DataServiceManager dataServiceManager, DataProfile dataProfile) {
            return false;
        }

        default DataProfile setupDataNetwork(Phone phone, DataProfile dataProfile) {
            return dataProfile;
        }

        default void showDataUsageDialogCustomized(Phone phone, DataProfile dataProfile) {
        }

        default void tearDownDunDataNetworkIfNeeded(Phone phone, int i, Object obj) {
        }

        default void updateDataConfig(Phone phone) {
        }

        default void updateLinkProperties(Phone phone, LinkProperties linkProperties) {
        }

        default void updateNetworkAgent(Phone phone, DataProfile dataProfile, TelephonyNetworkAgent telephonyNetworkAgent) {
        }

        default void updateNetworkCapabilities(Phone phone, NetworkCapabilities.Builder builder, DataNetwork dataNetwork) {
        }

        default void updateNetworkScore(Phone phone, boolean z) {
        }

        default int updatePreferredMtu(Phone phone, InetAddress inetAddress, int i, int i2) {
            return i2;
        }

        default boolean validateRadioToggleCondition(Phone phone) {
            return false;
        }

        default boolean validateResetModemCondition(Phone phone) {
            return false;
        }
    }

    static {
        int i = TelephonyCommonProxyFactory.PHONE_COUNT;
        numPhones = i;
        sInstances = new IMiuiDataController[i];
    }

    private static boolean isValidPhoneId(int i) {
        return i >= 0 && i < numPhones;
    }

    public static IMiuiDataController get(int i) {
        if (!isValidPhoneId(i)) {
            MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("invalid phoneId ", i);
            return null;
        }
        if (sInstances[i] == null) {
            synchronized (MiuiDataControllerStub.class) {
                try {
                    if (sInstances[i] == null) {
                        sInstances[i] = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiDataControllerStub(i);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstances[i];
    }
}
