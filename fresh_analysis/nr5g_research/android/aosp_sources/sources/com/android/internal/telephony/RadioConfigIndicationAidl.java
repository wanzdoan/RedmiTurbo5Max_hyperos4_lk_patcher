package com.android.internal.telephony;

import android.hardware.radio.config.IRadioConfigIndication;
import android.hardware.radio.config.SimSlotStatus;
import android.os.AsyncResult;
import android.os.Trace;
import com.android.internal.telephony.uicc.IccSlotStatus;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public class RadioConfigIndicationAidl extends IRadioConfigIndication.Stub {
    private final RadioConfig mRadioConfig;

    public int getInterfaceVersion() {
        return 5;
    }

    public RadioConfigIndicationAidl(RadioConfig radioConfig) {
        this.mRadioConfig = radioConfig;
    }

    public void simSlotsStatusChanged(int i, SimSlotStatus[] simSlotStatusArr) {
        ArrayList<IccSlotStatus> arrayListConvertHalSlotStatus = RILUtils.convertHalSlotStatus(simSlotStatusArr);
        logd("UNSOL_SIM_SLOT_STATUS_CHANGED " + arrayListConvertHalSlotStatus.toString());
        Registrant registrant = this.mRadioConfig.mSimSlotStatusRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, arrayListConvertHalSlotStatus, (Throwable) null));
        }
    }

    public void onSimultaneousCallingSupportChanged(int[] iArr) {
        Object objPrimitiveArrayToArrayList;
        if (iArr == null) {
            objPrimitiveArrayToArrayList = Collections.EMPTY_LIST;
        } else {
            objPrimitiveArrayToArrayList = RILUtils.primitiveArrayToArrayList(iArr);
        }
        logd("onSimultaneousCallingSupportChanged: enabledLogicalSlots = " + objPrimitiveArrayToArrayList);
        if (this.mRadioConfig.mSimultaneousCallingSupportStatusRegistrant != null) {
            logd("onSimultaneousCallingSupportChanged: notifying registrant");
            this.mRadioConfig.mSimultaneousCallingSupportStatusRegistrant.notifyRegistrant(new AsyncResult((Object) null, objPrimitiveArrayToArrayList, (Throwable) null));
        }
    }

    private static void logd(String str) {
        Rlog.d("RadioConfigIndicationAidl", "[UNSL]< " + str);
        Trace.instantForTrack(2097152L, "RIL", str);
    }

    public String getInterfaceHash() {
        return "b9f08521b4288bc71112cdeb47220af1ff758383";
    }
}
