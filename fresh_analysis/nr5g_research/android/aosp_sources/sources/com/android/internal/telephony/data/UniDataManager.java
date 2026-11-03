package com.android.internal.telephony.data;

import android.content.Context;
import android.provider.Settings;
import android.service.carrier.CarrierIdentifier;
import android.telephony.IBooleanConsumer;
import android.telephony.IIntegerConsumer;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.data.DataProfile;
import android.telephony.data.ExtendedTrafficDescriptor;
import android.telephony.data.ITrafficDescriptorListConsumer;
import android.telephony.data.TrafficDescriptor;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.CarrierResolver;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.UniFrameworkTelephonyManager;
import com.android.internal.telephony.UniTelephonyInjectionFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;

/* JADX INFO: loaded from: classes.dex */
public class UniDataManager {
    public static final int ARGS_LENGTH = 7;
    public static final String DEFAULT_DIFFERENTIATOR = "1";
    public static final String FLAG_FAST_SETUP = "FastSetupDataNetwork";
    public static final int INDEX_FLAG = 0;
    public static final int INDEX_GID1 = 4;
    public static final int INDEX_ICCID = 5;
    public static final int INDEX_IMSI = 3;
    public static final int INDEX_MCCMNC = 1;
    public static final int INDEX_PNN = 6;
    public static final int INDEX_SPN = 2;
    public static final String INVALID_NC_STRING = "";
    public static final String KEY_CARRIER_SUPPORTS_VOWIFI_MMS = "support_vowifi_mms";
    public static final String KEY_DIFFERENTIATOR = "differentiator";
    public static final String KEY_NC = "nc";
    private static final String TAG = "UniDataManager";
    private static UniDataManager sInstance;

    public static boolean canBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    public static boolean canFqdnBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    public static boolean canIpDescriptorBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    public static ExtendedTrafficDescriptor getExtendedTrafficDescriptorFromDataProfile(DataProfile dataProfile) {
        return null;
    }

    public void addCarrierIdFetchDoneListener(IIntegerConsumer iIntegerConsumer) {
    }

    public void addOnResetConsumer(DataNetworkController dataNetworkController, IntConsumer intConsumer) {
    }

    public void addServiceBoundedListener(DataServiceManager dataServiceManager, IntBinaryOperator intBinaryOperator) {
    }

    public CarrierResolver.CarrierMatchingRule createCarrierMatchingRule(int i, String str, List<String> list) {
        return null;
    }

    public void evaluateVendorDataAllowed(DataNetworkController dataNetworkController, DataEvaluation.DataEvaluationReason dataEvaluationReason, DataEvaluation dataEvaluation) {
    }

    public void factoryReset(int i) {
    }

    public String[] generateSelectionArgs(DataProfileManager dataProfileManager) {
        return null;
    }

    public String getArgFromCarrierIdentifierInfo(int i, int i2, String str) {
        return str;
    }

    public CarrierIdentifier getCarrierIdentifier(int i, CarrierIdentifier carrierIdentifier) {
        return carrierIdentifier;
    }

    public int getNationalRoamingValue(int i) {
        return 0;
    }

    public int getRequestReason(DataNetworkController dataNetworkController, int i, DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList) {
        return i;
    }

    public void getUniDataCallList(Phone phone) {
    }

    public boolean ignoreSimState(int i) {
        return false;
    }

    public boolean isDsrmRecoveryAlreadyStarted(Phone phone) {
        return false;
    }

    public boolean isPreferredDataProfileEmpty(DataNetworkController dataNetworkController) {
        return false;
    }

    public boolean isSuspendDueToOtherPhone(Phone phone) {
        return false;
    }

    public boolean isVowifiMmsDataNetwork(Phone phone, int i) {
        return false;
    }

    public void onCarrierConfigFetchDone(int i) {
    }

    public void onDataNetworkStateChanged(Phone phone) {
    }

    public void onSubIdChanged() {
    }

    public void registerAnyDataNetworkExistChangedCallback(int i, IBooleanConsumer iBooleanConsumer) {
    }

    public void registerTrafficDescriptorListChanged(int i, ITrafficDescriptorListConsumer iTrafficDescriptorListConsumer) {
    }

    public void registerUniDataNetworkStateCallback(int i, IIntegerConsumer iIntegerConsumer) {
    }

    public void removeOnResetConsumer(DataNetworkController dataNetworkController, IntConsumer intConsumer) {
    }

    public void requestSwitchPreferDataSubIdForReason(int i, String str) {
    }

    public void resetModemRetryTimer(DataProfile dataProfile, DataRetryManager.DataSetupRetryEntry dataSetupRetryEntry, DataNetworkController dataNetworkController) {
    }

    public void resumePreferDataSubIdForReason(int i, String str) {
    }

    public void setNationalRoamingValue(int i, int i2) {
    }

    public void uniCleanUpDataNetwork(Phone phone) {
    }

    public void unregisterAnyDataNetworkExistChangedCallback(int i, IBooleanConsumer iBooleanConsumer) {
    }

    public void unregisterTrafficDescriptorListChanged(int i, ITrafficDescriptorListConsumer iTrafficDescriptorListConsumer) {
    }

    public void unregisterUniDataNetworkStateCallback(int i, IIntegerConsumer iIntegerConsumer) {
    }

    public List<DataProfile> updateDataProfiles(DataNetworkController dataNetworkController, List<DataProfile> list) {
        return list;
    }

    public UniDataManager() {
        log("UniDataManager created");
    }

    public static UniDataManager getInstance() {
        UniDataManager uniDataManager;
        synchronized (UniDataManager.class) {
            try {
                if (sInstance == null) {
                    sInstance = UniTelephonyInjectionFactory.getInstance().getUniDataManager();
                }
                uniDataManager = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return uniDataManager;
    }

    public void setVendorDataEnabled(int i, boolean z, int i2) {
        log("setVendorDataEnabled return");
    }

    public int getSimStateForPhone(Phone phone) {
        if (phone == null || phone.getIccCard() == null) {
            return 0;
        }
        return phone.getIccCard().getState().ordinal();
    }

    public static boolean isPhoneStateIdle() {
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null && phone.getState() != PhoneConstants.State.IDLE) {
                log("isPhoneStateIdle: Voice call active on phone: " + i);
                return false;
            }
        }
        return true;
    }

    public static boolean hasSameApnSetting(DataProfile dataProfile, DataProfile dataProfile2) {
        return (dataProfile == null || dataProfile2 == null || !Objects.equals(dataProfile.getApnSetting(), dataProfile2.getApnSetting())) ? false : true;
    }

    public static int getDefaultDataSubIdFromDb(Context context) {
        if (context == null) {
            return -1;
        }
        return Settings.Global.getInt(context.getContentResolver(), "multi_sim_data_call", -1);
    }

    public Map<String, String> getNcAndDifferentiatorMap(DataNetworkController dataNetworkController, TrafficDescriptor.OsAppId osAppId) {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put("nc", "");
        arrayMap.put("differentiator", "1");
        return arrayMap;
    }

    public static boolean isActiveSubId(Context context, int i) {
        SubscriptionManager subscriptionManager;
        return SubscriptionManager.isValidSubscriptionId(i) && (subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service")) != null && subscriptionManager.isActiveSubId(i);
    }

    public static boolean isDefaultDataSubId(Phone phone) {
        if (phone == null) {
            return true;
        }
        int subId = phone.getSubId();
        int defaultDataSubIdFromDb = getDefaultDataSubIdFromDb(phone.getContext());
        if (!SubscriptionManager.isValidSubscriptionId(subId) || !SubscriptionManager.isValidSubscriptionId(defaultDataSubIdFromDb) || subId == defaultDataSubIdFromDb) {
            return true;
        }
        log("isDefaultDataSubId: dds = " + defaultDataSubIdFromDb + ", subId = " + subId);
        return false;
    }

    public boolean needUpdateDataProfiles(DataProfileManager dataProfileManager, DataConfigManager dataConfigManager) {
        return dataConfigManager.isConfigCarrierSpecific();
    }

    public static boolean isFastSetupDataNetworkSupported() {
        return UniFrameworkTelephonyManager.isFastSetupDataNetworkSupported();
    }

    private static void log(String str) {
        Log.d(TAG, str);
    }
}
