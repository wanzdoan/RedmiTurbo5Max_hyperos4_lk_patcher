package com.android.internal.telephony.uicc;

import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class IccSimPortInfo {
    public String mIccId;
    public int mLogicalSlotIndex;
    public boolean mPortActive;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            IccSimPortInfo iccSimPortInfo = (IccSimPortInfo) obj;
            if (this.mPortActive == iccSimPortInfo.mPortActive && this.mLogicalSlotIndex == iccSimPortInfo.mLogicalSlotIndex && TextUtils.equals(this.mIccId, iccSimPortInfo.mIccId)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.mPortActive), Integer.valueOf(this.mLogicalSlotIndex), this.mIccId);
    }

    public String toString() {
        return "{iccid=" + SubscriptionInfo.getPrintableId(this.mIccId) + ",logicalSlotIndex=" + this.mLogicalSlotIndex + ",portActive=" + this.mPortActive + "}";
    }
}
