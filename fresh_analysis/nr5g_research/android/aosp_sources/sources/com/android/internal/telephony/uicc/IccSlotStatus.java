package com.android.internal.telephony.uicc;

import android.text.TextUtils;
import com.android.internal.telephony.RIL$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class IccSlotStatus {
    public static final int STATE_ACTIVE = 1;
    public String atr;
    public IccCardStatus.CardState cardState;
    public String eid;
    public IccSimPortInfo[] mSimPortInfos;
    public MultipleEnabledProfilesMode mSupportedMepMode = MultipleEnabledProfilesMode.NONE;

    public enum MultipleEnabledProfilesMode {
        NONE,
        MEP_A1,
        MEP_A2,
        MEP_B;

        public boolean isMepAMode() {
            return this == MEP_A1 || this == MEP_A2;
        }

        public boolean isMepA1Mode() {
            return this == MEP_A1;
        }

        public boolean isMepMode() {
            return this != NONE;
        }
    }

    public void setCardState(int i) {
        if (i == 0) {
            this.cardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
            return;
        }
        if (i == 1) {
            this.cardState = IccCardStatus.CardState.CARDSTATE_PRESENT;
            return;
        }
        if (i == 2) {
            this.cardState = IccCardStatus.CardState.CARDSTATE_ERROR;
        } else if (i == 3) {
            this.cardState = IccCardStatus.CardState.CARDSTATE_RESTRICTED;
        } else {
            RIL$$ExternalSyntheticBUOutline0.m("Unrecognized RIL_CardState: ", i);
        }
    }

    public void setMultipleEnabledProfilesMode(int i) {
        if (i == 0) {
            this.mSupportedMepMode = MultipleEnabledProfilesMode.NONE;
            return;
        }
        if (i == 1) {
            this.mSupportedMepMode = MultipleEnabledProfilesMode.MEP_A1;
            return;
        }
        if (i == 2) {
            this.mSupportedMepMode = MultipleEnabledProfilesMode.MEP_A2;
        } else if (i == 3) {
            this.mSupportedMepMode = MultipleEnabledProfilesMode.MEP_B;
        } else {
            RIL$$ExternalSyntheticBUOutline0.m("Unrecognized RIL_MultipleEnabledProfilesMode: ", i);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("IccSlotStatus {");
        sb.append(this.cardState);
        sb.append(",atr=");
        sb.append(this.atr);
        sb.append(",eid=");
        sb.append(Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.eid));
        sb.append(",");
        if (this.mSimPortInfos != null) {
            sb.append("num_ports=");
            sb.append(this.mSimPortInfos.length);
            for (int i = 0; i < this.mSimPortInfos.length; i++) {
                sb.append(", IccSimPortInfo-" + i + this.mSimPortInfos[i]);
            }
        } else {
            sb.append("num_ports=null");
        }
        sb.append(", SupportedMepMode=" + this.mSupportedMepMode);
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            IccSlotStatus iccSlotStatus = (IccSlotStatus) obj;
            if (this.cardState == iccSlotStatus.cardState && TextUtils.equals(this.atr, iccSlotStatus.atr) && TextUtils.equals(this.eid, iccSlotStatus.eid) && Arrays.equals(this.mSimPortInfos, iccSlotStatus.mSimPortInfos)) {
                return true;
            }
        }
        return false;
    }
}
