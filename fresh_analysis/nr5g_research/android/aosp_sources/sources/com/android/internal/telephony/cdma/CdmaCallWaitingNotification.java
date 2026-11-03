package com.android.internal.telephony.cdma;

import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class CdmaCallWaitingNotification {
    public String number = null;
    public int numberPresentation = 0;
    public String name = null;
    public int namePresentation = 0;
    public int numberType = 0;
    public int numberPlan = 0;
    public int isPresent = 0;
    public int signalType = 0;
    public int alertPitch = 0;
    public int signal = 0;

    public String toString() {
        return super.toString() + "Call Waiting Notification   number: " + Rlog.pii("CdmaCallWaitingNotification", this.number) + " numberPresentation: " + this.numberPresentation + " name: " + this.name + " namePresentation: " + this.namePresentation + " numberType: " + this.numberType + " numberPlan: " + this.numberPlan + " isPresent: " + this.isPresent + " signalType: " + this.signalType + " alertPitch: " + this.alertPitch + " signal: " + this.signal;
    }

    public static int presentationFromCLIP(int i) {
        if (i == 0) {
            return 1;
        }
        if (i == 1) {
            return 2;
        }
        if (i != 2) {
            Rlog.d("CdmaCallWaitingNotification", "Unexpected presentation " + i);
        }
        return 3;
    }
}
