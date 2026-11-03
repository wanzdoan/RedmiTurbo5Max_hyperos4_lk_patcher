package com.android.internal.telephony;

import android.telephony.data.DataProfile;
import android.telephony.data.TrafficDescriptor;
import android.util.ArrayMap;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.internal.telephony.data.DataNetworkController;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperDataManager extends IHyperCommonModule {
    public static final IHyperDataManager DEFAULT = new IHyperDataManager() { // from class: com.android.internal.telephony.IHyperDataManager.1
    };
    public static final String DEFAULT_DIFFERENTIATOR = "1";
    public static final String INVALID_NC_STRING = "";
    public static final String KEY_DIFFERENTIATOR = "differentiator";
    public static final String KEY_NC = "nc";
    public static final String TAG = "IHyperDataManager";

    default boolean canBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean canFqdnBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean canIpDescriptorBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean checkVendorDataAllowed() {
        return true;
    }

    default int getRequestReason(int i, DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList) {
        return i;
    }

    default int getSimStateForPhone() {
        return 0;
    }

    default boolean isDefaultDataSubId() {
        return true;
    }

    default boolean isPreferredDataProfileEmpty() {
        return false;
    }

    default boolean isVowifiMmsDataNetwork(int i) {
        return false;
    }

    default void onDataNetworkStateChanged() {
    }

    default List<DataProfile> updateDataProfiles(List<DataProfile> list) {
        return list;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperDataManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default Map<String, String> getNcAndDifferentiatorMap(DataNetworkController dataNetworkController, TrafficDescriptor.OsAppId osAppId) {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put("nc", "");
        arrayMap.put("differentiator", "1");
        return arrayMap;
    }
}
