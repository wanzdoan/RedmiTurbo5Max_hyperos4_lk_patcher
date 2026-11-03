package com.mediatek.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse;
import vendor.mediatek.hardware.mtkradioex.data.SetupDataCallResultSlice;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExDataResponse extends IMtkRadioExDataResponse.Stub {
    MtkRIL mMtkRil;

    public MtkRadioExDataResponse(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    public void acknowledgeRequest(int serial) {
        this.mMtkRil.processRequestAck(serial);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public void dataConnectionAttachResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(1, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public void dataConnectionDetachResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(1, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public void enableDsdaIndicationResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(1, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public void getDsdaStatusResponse(RadioResponseInfo info, int mode) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(1, this.mMtkRil, info, new int[]{mode});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public void resetAllConnectionsResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(1, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public void resetMdDataRetryCountResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(1, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public void setupDataCallResponseSlice(RadioResponseInfo responseInfo, SetupDataCallResultSlice setupDataCallResult) {
        this.mMtkRil.riljLog("setupDataCallSlice response, you want this? add more codes.");
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public String getInterfaceHash() {
        return "0f9b0198bf345f123790d9c279bde6bd25a2a227";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
    public int getInterfaceVersion() {
        return 3;
    }
}
