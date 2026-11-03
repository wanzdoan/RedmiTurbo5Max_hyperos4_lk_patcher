package com.android.phone;

import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.telephony.UssdResponse;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.util.Preconditions;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class CallWaitingUssdResultReceiver extends ResultReceiver {
    private IIntegerConsumer mCallback;
    private CarrierXmlParser mCarrierXmlParser;
    private CarrierXmlParser.SsEntry.SSAction mSsAction;

    CallWaitingUssdResultReceiver(Handler handler, IIntegerConsumer iIntegerConsumer, CarrierXmlParser carrierXmlParser, CarrierXmlParser.SsEntry.SSAction sSAction) {
        super(handler);
        this.mCallback = iIntegerConsumer;
        this.mCarrierXmlParser = carrierXmlParser;
        this.mSsAction = sSAction;
    }

    @Override // android.os.ResultReceiver
    protected void onReceiveResult(int i, Bundle bundle) {
        log("USSD:" + i);
        Preconditions.checkNotNull(bundle, "ussdResponse cannot be null.");
        UssdResponse ussdResponse = (UssdResponse) bundle.getParcelable("USSD_RESPONSE");
        if (i == 100) {
            try {
                this.mCallback.accept(getStatusFromResponse(ussdResponse));
                return;
            } catch (RemoteException e) {
                log("Fail to notify getCallWaitingStatus due to " + e);
                return;
            }
        }
        try {
            this.mCallback.accept(3);
        } catch (RemoteException e2) {
            log("Fail to notify getCallWaitingStatus due to " + e2);
        }
    }

    private int getStatusFromResponse(UssdResponse ussdResponse) {
        CarrierXmlParser.SsFeature feature = this.mCarrierXmlParser.getFeature("callwaiting");
        if (feature == null) {
            return 3;
        }
        HashMap responseSet = feature.getResponseSet(this.mSsAction, ussdResponse.getReturnMessage().toString());
        if (responseSet.get("RESPONSE_ERROR") != null || responseSet.size() == 0) {
            return 3;
        }
        String str = (String) responseSet.get("status_code");
        if (TextUtils.isEmpty(str)) {
            return 3;
        }
        if (str.equals("activate")) {
            return 1;
        }
        return str.equals("deactivate") ? 2 : 3;
    }

    private static void log(String str) {
        Log.d("CwUssdResultReceiver", str);
    }
}
