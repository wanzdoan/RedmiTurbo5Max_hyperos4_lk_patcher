package com.mediatek.internal.telephony;

import android.hardware.radio.V1_0.OperatorInfo;
import android.hardware.radio.V1_0.RadioResponseInfo;
import android.telephony.MtkRadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.NetworkScanResult;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILRequest;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.RadioResponse;
import com.android.internal.telephony.ServiceStateTracker;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioResponse extends RadioResponse {
    private static final String TAG = "MtkRadioResp";
    private MtkRIL mMtkRil;

    public MtkRadioResponse(RIL ril) {
        super(ril);
        this.mMtkRil = (MtkRIL) ril;
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    public void getAvailableNetworksResponse(RadioResponseInfo responseInfo, ArrayList<OperatorInfo> networkInfos) {
        int mLac = -1;
        Phone phone = PhoneFactory.getPhone(this.mMtkRil.mInstanceId.intValue());
        if (phone != null) {
            ServiceStateTracker sst = phone.getServiceStateTracker();
            ServiceState serviceState = sst.mSS;
            mLac = ((MtkServiceStateTracker) sst).getLac();
        }
        for (int i = 0; i < networkInfos.size(); i++) {
            String mccmnc = networkInfos.get(i).operatorNumeric;
            networkInfos.get(i).alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, mLac);
            networkInfos.get(i).alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, mLac);
        }
        super.getAvailableNetworksResponse(responseInfo, networkInfos);
    }

    public void getPreferredNetworkTypeBitmapResponse(RadioResponseInfo responseInfo, int halRadioAccessFamilyBitmap) {
        int networkType = MtkRadioAccessFamily.getNetworkTypeFromRaf(RILUtils.convertHalNetworkTypeBitMask(halRadioAccessFamilyBitmap));
        super.getPreferredNetworkTypeResponse(responseInfo, networkType);
    }

    public void startNetworkScanResponse_1_5(RadioResponseInfo responseInfo) {
        RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr == null) {
            return;
        }
        NetworkScanResult nsr = null;
        if (responseInfo.error == 0) {
            nsr = new NetworkScanResult(1, 0, (List) null);
            sendMessageResponse(rr.mResult, nsr);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, nsr);
    }
}
