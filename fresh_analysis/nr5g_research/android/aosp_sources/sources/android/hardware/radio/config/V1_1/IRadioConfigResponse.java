package android.hardware.radio.config.V1_1;

import android.hardware.radio.V1_0.RadioResponseInfo;

/* JADX INFO: loaded from: classes.dex */
public interface IRadioConfigResponse extends android.hardware.radio.config.V1_0.IRadioConfigResponse {
    void getModemsConfigResponse(RadioResponseInfo radioResponseInfo, ModemsConfig modemsConfig);

    void getPhoneCapabilityResponse(RadioResponseInfo radioResponseInfo, PhoneCapability phoneCapability);

    void setModemsConfigResponse(RadioResponseInfo radioResponseInfo);

    void setPreferredDataModemResponse(RadioResponseInfo radioResponseInfo);
}
