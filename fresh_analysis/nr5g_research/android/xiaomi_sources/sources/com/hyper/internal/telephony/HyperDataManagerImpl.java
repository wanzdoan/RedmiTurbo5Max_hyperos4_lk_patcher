package com.hyper.internal.telephony;

import android.telephony.data.DataProfile;
import android.telephony.data.TrafficDescriptor;
import com.android.internal.telephony.IHyperDataManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.DataNetworkController;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class HyperDataManagerImpl implements IHyperDataManager {
    private static HyperDataManagerImpl sInstance;
    private DataNetworkController mDataNetworkController;
    private Phone mPhone;

    public HyperDataManagerImpl(DataNetworkController dnc, Phone phone) {
        if (dnc == null || phone == null) {
            throw new IllegalArgumentException("dataNetworkController or phone cannot be null");
        }
        this.mDataNetworkController = dnc;
        this.mPhone = phone;
    }

    public boolean canFqdnBeSatisfiedBy(DataProfile dataProfile) {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().canFqdnBeSatisfiedBy(dataProfile);
    }

    public boolean canIpDescriptorBeSatisfiedBy(DataProfile dataProfile) {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().canIpDescriptorBeSatisfiedBy(dataProfile);
    }

    public Map<String, String> getNcAndDifferentiatorMap(TrafficDescriptor.OsAppId osAppId) {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().getNcAndDifferentiatorMap(this.mDataNetworkController, osAppId);
    }

    public void onDataNetworkStateChanged() {
        HyperTelephonyInternalManager.getInstance().getHyperDataManager().onDataNetworkStateChanged(this.mPhone);
    }

    public boolean canBeSatisfiedBy(DataProfile dataProfile) {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().canBeSatisfiedBy(dataProfile);
    }

    public boolean isVowifiMmsDataNetwork(int transport) {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().isVowifiMmsDataNetwork(this.mPhone, transport);
    }

    public boolean checkVendorDataAllowed() {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().checkVendorDataAllowed(this.mPhone.getPhoneId());
    }

    public int getRequestReason(int reason, DataProfile dataProfile, DataNetworkController.NetworkRequestList requestList) {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().getRequestReason(this.mDataNetworkController, reason, dataProfile, requestList);
    }

    public int getSimStateForPhone() {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().getSimStateForPhone(this.mPhone);
    }

    public boolean isPreferredDataProfileEmpty() {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().isPreferredDataProfileEmpty(this.mDataNetworkController);
    }

    public List<DataProfile> updateDataProfiles(List<DataProfile> dataProfiles) {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().updateDataProfiles(this.mDataNetworkController, dataProfiles);
    }

    public boolean isDefaultDataSubId() {
        return HyperTelephonyInternalManager.getInstance().getHyperDataManager().isDefaultDataSubId(this.mPhone);
    }
}
