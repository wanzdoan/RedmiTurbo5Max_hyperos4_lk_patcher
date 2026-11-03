package com.hyper.internal.telephony.ext;

import android.telephony.data.DataProfile;
import android.telephony.data.TrafficDescriptor;
import android.util.ArrayMap;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.internal.telephony.data.DataNetworkController;
import java.util.List;
import java.util.Map;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperDataManagerExt extends IHyperCommonModule {
    public static final IHyperDataManagerExt DEFAULT = new IHyperDataManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperDataManagerExt.1
    };
    public static final String DEFAULT_DIFFERENTIATOR = "1";
    public static final String INVALID_NC_STRING = "";
    public static final String KEY_DIFFERENTIATOR = "differentiator";
    public static final String KEY_NC = "nc";
    public static final String TAG = "IHyperDataManager";

    default IHyperDataManagerExt getDefault() {
        return DEFAULT;
    }

    default boolean canFqdnBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean canIpDescriptorBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean canBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default void setGwsdEnabled(int mPhoneId, boolean enabled) {
    }

    default void setGwsdEnabled(boolean enabled) {
    }

    default void setGwsdAutoRejectEnabled(boolean enabled) {
    }

    default void setGwsdListener() {
    }

    default Map<String, String> getNcAndDifferentiatorMap(DataNetworkController dnc, TrafficDescriptor.OsAppId osAppId) {
        Map<String, String> resultMap = new ArrayMap<>();
        resultMap.put(KEY_NC, "");
        resultMap.put(KEY_DIFFERENTIATOR, "1");
        return resultMap;
    }

    default void onDataNetworkStateChanged(Phone phone) {
    }

    default boolean isVowifiMmsDataNetwork(Phone phone, int transport) {
        return false;
    }

    default boolean checkVendorDataAllowed(int phoneId) {
        return true;
    }

    default int getRequestReason(DataNetworkController dnc, int reason, DataProfile dataProfile, DataNetworkController.NetworkRequestList requestList) {
        return reason;
    }

    default int getSimStateForPhone(Phone phone) {
        return 0;
    }

    default boolean isPreferredDataProfileEmpty(DataNetworkController dnc) {
        return false;
    }

    default List<DataProfile> updateDataProfiles(DataNetworkController dataNetworkController, List<DataProfile> dataProfiles) {
        return dataProfiles;
    }

    default boolean isDefaultDataSubId(Phone phone) {
        return true;
    }
}
