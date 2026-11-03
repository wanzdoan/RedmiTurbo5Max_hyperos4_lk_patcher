package android.hardware.radio.config.V1_0;

import android.hardware.radio.V1_0.RadioResponseInfo;
import android.internal.hidl.base.V1_0.IBase;
import android.os.IHwBinder;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface IRadioConfigResponse extends IBase {
    IHwBinder asBinder();

    void getSimSlotsStatusResponse(RadioResponseInfo radioResponseInfo, ArrayList arrayList);

    void setSimSlotsMappingResponse(RadioResponseInfo radioResponseInfo);
}
