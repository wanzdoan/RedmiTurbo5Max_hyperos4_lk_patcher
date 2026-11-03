package com.android.phone.slice;

/* JADX INFO: loaded from: classes.dex */
public class PremiumNetworkEntitlementResponse {
    public int mEntitlementStatus;
    public int mProvisionStatus;
    public String mServiceFlowContentsType;
    public String mServiceFlowURL;
    public String mServiceFlowUserData;

    public boolean isAlreadyPurchased() {
        int i = this.mEntitlementStatus;
        if (i != 1 && i != 4) {
            return false;
        }
        int i2 = this.mProvisionStatus;
        return i2 == 1 || i2 == 2;
    }

    public boolean isProvisioningInProgress() {
        int i = this.mEntitlementStatus;
        if (i == 1) {
            return this.mProvisionStatus == 3;
        }
        if (i != 3) {
            return false;
        }
        int i2 = this.mProvisionStatus;
        return i2 == 3 || i2 == 2;
    }

    public boolean isPremiumNetworkCapabilityAllowed() {
        int i = this.mEntitlementStatus;
        if (i == 0 || i == 2) {
            return false;
        }
        return !isInvalidResponse();
    }

    public boolean isInvalidResponse() {
        int i = this.mEntitlementStatus;
        if (i == 3) {
            int i2 = this.mProvisionStatus;
            return i2 == 0 || i2 == 1;
        }
        if (i != 4) {
            return false;
        }
        int i3 = this.mProvisionStatus;
        return i3 == 0 || i3 == 3;
    }

    public String toString() {
        return "PremiumNetworkEntitlementResponse{mEntitlementStatus=" + this.mEntitlementStatus + ", mProvisionStatus=" + this.mProvisionStatus + ", mServiceFlowURL=" + this.mServiceFlowURL + ", mServiceFlowUserData=" + this.mServiceFlowUserData + ", mServiceFlowContentsType=" + this.mServiceFlowContentsType + "}";
    }
}
