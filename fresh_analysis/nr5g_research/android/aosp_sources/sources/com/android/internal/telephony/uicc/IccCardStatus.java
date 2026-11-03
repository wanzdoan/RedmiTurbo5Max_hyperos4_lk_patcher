package com.android.internal.telephony.uicc;

import android.telephony.SubscriptionInfo;
import com.android.internal.telephony.RIL$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class IccCardStatus {
    public static final int CARD_MAX_APPS = 8;
    public String atr;
    public String eid;
    public String iccid;
    public IccCardApplicationStatus[] mApplications;
    public CardState mCardState;
    public int mCdmaSubscriptionAppIndex;
    public int mGsmUmtsSubscriptionAppIndex;
    public int mImsSubscriptionAppIndex;
    public IccSlotPortMapping mSlotPortMapping;
    public IccSlotStatus.MultipleEnabledProfilesMode mSupportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.NONE;
    public PinState mUniversalPinState;

    public enum PinState {
        PINSTATE_UNKNOWN,
        PINSTATE_ENABLED_NOT_VERIFIED,
        PINSTATE_ENABLED_VERIFIED,
        PINSTATE_DISABLED,
        PINSTATE_ENABLED_BLOCKED,
        PINSTATE_ENABLED_PERM_BLOCKED
    }

    public enum CardState {
        CARDSTATE_ABSENT,
        CARDSTATE_PRESENT,
        CARDSTATE_ERROR,
        CARDSTATE_RESTRICTED;

        public boolean isCardPresent() {
            return this == CARDSTATE_PRESENT || this == CARDSTATE_RESTRICTED;
        }
    }

    public void setMultipleEnabledProfilesMode(int i) {
        if (i == 0) {
            this.mSupportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.NONE;
            return;
        }
        if (i == 1) {
            this.mSupportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.MEP_A1;
            return;
        }
        if (i == 2) {
            this.mSupportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.MEP_A2;
        } else if (i == 3) {
            this.mSupportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.MEP_B;
        } else {
            RIL$$ExternalSyntheticBUOutline0.m("Unrecognized RIL_MultipleEnabledProfilesMode: ", i);
        }
    }

    public void setCardState(int i) {
        if (i == 0) {
            this.mCardState = CardState.CARDSTATE_ABSENT;
            return;
        }
        if (i == 1) {
            this.mCardState = CardState.CARDSTATE_PRESENT;
            return;
        }
        if (i == 2) {
            this.mCardState = CardState.CARDSTATE_ERROR;
        } else if (i == 3) {
            this.mCardState = CardState.CARDSTATE_RESTRICTED;
        } else {
            RIL$$ExternalSyntheticBUOutline0.m("Unrecognized RIL_CardState: ", i);
        }
    }

    public void setUniversalPinState(int i) {
        if (i == 0) {
            this.mUniversalPinState = PinState.PINSTATE_UNKNOWN;
            return;
        }
        if (i == 1) {
            this.mUniversalPinState = PinState.PINSTATE_ENABLED_NOT_VERIFIED;
            return;
        }
        if (i == 2) {
            this.mUniversalPinState = PinState.PINSTATE_ENABLED_VERIFIED;
            return;
        }
        if (i == 3) {
            this.mUniversalPinState = PinState.PINSTATE_DISABLED;
            return;
        }
        if (i == 4) {
            this.mUniversalPinState = PinState.PINSTATE_ENABLED_BLOCKED;
        } else if (i == 5) {
            this.mUniversalPinState = PinState.PINSTATE_ENABLED_PERM_BLOCKED;
        } else {
            RIL$$ExternalSyntheticBUOutline0.m("Unrecognized RIL_PinState: ", i);
        }
    }

    public String toString() {
        int i;
        int i2;
        int i3;
        StringBuilder sb = new StringBuilder("IccCardState {");
        sb.append(this.mCardState);
        sb.append(",");
        sb.append(this.mUniversalPinState);
        if (this.mApplications != null) {
            sb.append(",num_apps=");
            sb.append(this.mApplications.length);
        } else {
            sb.append(",mApplications=null");
        }
        sb.append(",gsm_id=");
        sb.append(this.mGsmUmtsSubscriptionAppIndex);
        Object[] objArr = this.mApplications;
        if (objArr != null && (i3 = this.mGsmUmtsSubscriptionAppIndex) >= 0 && i3 < objArr.length) {
            Object obj = objArr[i3];
            if (obj == null) {
                obj = "null";
            }
            sb.append(obj);
        }
        sb.append(",cdma_id=");
        sb.append(this.mCdmaSubscriptionAppIndex);
        Object[] objArr2 = this.mApplications;
        if (objArr2 != null && (i2 = this.mCdmaSubscriptionAppIndex) >= 0 && i2 < objArr2.length) {
            Object obj2 = objArr2[i2];
            if (obj2 == null) {
                obj2 = "null";
            }
            sb.append(obj2);
        }
        sb.append(",ims_id=");
        sb.append(this.mImsSubscriptionAppIndex);
        IccCardApplicationStatus[] iccCardApplicationStatusArr = this.mApplications;
        if (iccCardApplicationStatusArr != null && (i = this.mImsSubscriptionAppIndex) >= 0 && i < iccCardApplicationStatusArr.length) {
            IccCardApplicationStatus iccCardApplicationStatus = iccCardApplicationStatusArr[i];
            sb.append(iccCardApplicationStatus != null ? iccCardApplicationStatus : "null");
        }
        sb.append(",atr=");
        sb.append(this.atr);
        sb.append(",iccid=");
        sb.append(SubscriptionInfo.getPrintableId(this.iccid));
        sb.append(",eid=");
        sb.append(Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.eid));
        sb.append(",SupportedMepMode=");
        sb.append(this.mSupportedMepMode);
        sb.append(",SlotPortMapping=");
        sb.append(this.mSlotPortMapping);
        sb.append("}");
        return sb.toString();
    }
}
