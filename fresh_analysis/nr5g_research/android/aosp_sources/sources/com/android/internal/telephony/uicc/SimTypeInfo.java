package com.android.internal.telephony.uicc;

import com.android.internal.telephony.RIL$$ExternalSyntheticBUOutline0;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public class SimTypeInfo {
    public int mCurrentSimType = 0;
    public int mPhysicalSlotIndex;
    public int mSupportedSimTypes;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SimType {
        public static final int SIM_TYPE_ESIM = 2;
        public static final int SIM_TYPE_PHYSICAL = 1;
        public static final int SIM_TYPE_UNKNOWN = 0;
    }

    public void setCurrentSimType(int i) {
        if (i == 0) {
            this.mCurrentSimType = 0;
            return;
        }
        if (i == 1) {
            this.mCurrentSimType = 1;
        } else if (i == 2) {
            this.mCurrentSimType = 2;
        } else {
            RIL$$ExternalSyntheticBUOutline0.m("Unrecognized RIL_SimType: ", i);
        }
    }

    public String toString() {
        return "SimTypeInfo {activeSimType=" + this.mCurrentSimType + ",supportedSimType=" + this.mSupportedSimTypes + ",physicalSlotIndex=" + this.mPhysicalSlotIndex + "}";
    }
}
