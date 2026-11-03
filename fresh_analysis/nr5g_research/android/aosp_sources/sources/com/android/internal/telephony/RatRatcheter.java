package com.android.internal.telephony;

import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.telephony.Rlog;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class RatRatcheter {
    private final Phone mPhone;
    private final SparseArray mRatFamilyMap = new SparseArray();

    public static boolean updateBandwidths(int[] iArr, ServiceState serviceState) {
        if (iArr == null) {
            return false;
        }
        if (Arrays.stream(iArr).sum() <= Arrays.stream(serviceState.getCellBandwidths()).sum()) {
            return false;
        }
        serviceState.setCellBandwidths(iArr);
        return true;
    }

    public RatRatcheter(Phone phone) {
        this.mPhone = phone;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(phone.getContext().getMainExecutor(), new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.RatRatcheter$$ExternalSyntheticLambda0
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        resetRatFamilyMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        resetRatFamilyMap();
    }

    private int ratchetRat(int i, int i2) {
        int iNetworkTypeToRilRadioTechnology = ServiceState.networkTypeToRilRadioTechnology(i);
        int iNetworkTypeToRilRadioTechnology2 = ServiceState.networkTypeToRilRadioTechnology(i2);
        synchronized (this.mRatFamilyMap) {
            try {
                SparseIntArray sparseIntArray = (SparseIntArray) this.mRatFamilyMap.get(iNetworkTypeToRilRadioTechnology);
                if (sparseIntArray == null) {
                    return i2;
                }
                SparseIntArray sparseIntArray2 = (SparseIntArray) this.mRatFamilyMap.get(iNetworkTypeToRilRadioTechnology2);
                if (sparseIntArray2 != sparseIntArray) {
                    return i2;
                }
                if (sparseIntArray2.get(iNetworkTypeToRilRadioTechnology, -1) <= sparseIntArray2.get(iNetworkTypeToRilRadioTechnology2, -1)) {
                    iNetworkTypeToRilRadioTechnology = iNetworkTypeToRilRadioTechnology2;
                }
                return ServiceState.rilRadioTechnologyToNetworkType(iNetworkTypeToRilRadioTechnology);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void ratchet(ServiceState serviceState, ServiceState serviceState2) {
        if (!isSameRatFamily(serviceState, serviceState2)) {
            Rlog.e("RilRatcheter", "Same cell cannot have different RAT Families. Likely bug.");
            return;
        }
        int[] iArr = {1, 2};
        for (int i = 0; i < 2; i++) {
            int i2 = iArr[i];
            NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(i2, 1);
            NetworkRegistrationInfo networkRegistrationInfo2 = serviceState2.getNetworkRegistrationInfo(i2, 1);
            networkRegistrationInfo2.setAccessNetworkTechnology(ratchetRat(networkRegistrationInfo.getAccessNetworkTechnology(), networkRegistrationInfo2.getAccessNetworkTechnology()));
            if (networkRegistrationInfo.isUsingCarrierAggregation()) {
                networkRegistrationInfo2.setIsUsingCarrierAggregation(true);
            }
            serviceState2.addNetworkRegistrationInfo(networkRegistrationInfo2);
        }
        updateBandwidths(serviceState.getCellBandwidths(), serviceState2);
    }

    private boolean isSameRatFamily(ServiceState serviceState, ServiceState serviceState2) {
        synchronized (this.mRatFamilyMap) {
            boolean z = true;
            try {
                if (serviceState.getNetworkRegistrationInfo(2, 1) != null && serviceState2.getNetworkRegistrationInfo(2, 1) != null) {
                    int iNetworkTypeToRilRadioTechnology = ServiceState.networkTypeToRilRadioTechnology(serviceState.getNetworkRegistrationInfo(2, 1).getAccessNetworkTechnology());
                    int iNetworkTypeToRilRadioTechnology2 = ServiceState.networkTypeToRilRadioTechnology(serviceState2.getNetworkRegistrationInfo(2, 1).getAccessNetworkTechnology());
                    if (iNetworkTypeToRilRadioTechnology == 14 && serviceState.isUsingCarrierAggregation()) {
                        iNetworkTypeToRilRadioTechnology = 19;
                    }
                    if (iNetworkTypeToRilRadioTechnology2 == 14 && serviceState2.isUsingCarrierAggregation()) {
                        iNetworkTypeToRilRadioTechnology2 = 19;
                    }
                    if (iNetworkTypeToRilRadioTechnology == iNetworkTypeToRilRadioTechnology2) {
                        return true;
                    }
                    if (this.mRatFamilyMap.get(iNetworkTypeToRilRadioTechnology) == null) {
                        return false;
                    }
                    if (this.mRatFamilyMap.get(iNetworkTypeToRilRadioTechnology) != this.mRatFamilyMap.get(iNetworkTypeToRilRadioTechnology2)) {
                        z = false;
                    }
                    return z;
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void resetRatFamilyMap() {
        synchronized (this.mRatFamilyMap) {
            try {
                this.mRatFamilyMap.clear();
                PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mPhone.getContext(), this.mPhone.getSubId(), new String[]{"ratchet_rat_families"});
                if (carrierConfigSubset != null && !carrierConfigSubset.isEmpty()) {
                    String[] stringArray = carrierConfigSubset.getStringArray("ratchet_rat_families");
                    if (stringArray == null) {
                        return;
                    }
                    for (String str : stringArray) {
                        String[] strArrSplit = str.split(",");
                        if (strArrSplit.length >= 2) {
                            SparseIntArray sparseIntArray = new SparseIntArray(strArrSplit.length);
                            int length = strArrSplit.length;
                            int i = 0;
                            int i2 = 0;
                            while (i < length) {
                                String str2 = strArrSplit[i];
                                try {
                                    int i3 = Integer.parseInt(str2.trim());
                                    if (this.mRatFamilyMap.get(i3) != null) {
                                        Rlog.e("RilRatcheter", "RAT listed twice: " + str2);
                                        break;
                                    }
                                    sparseIntArray.put(i3, i2);
                                    this.mRatFamilyMap.put(i3, sparseIntArray);
                                    i++;
                                    i2++;
                                } catch (NumberFormatException unused) {
                                    Rlog.e("RilRatcheter", "NumberFormatException on " + str2);
                                }
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
