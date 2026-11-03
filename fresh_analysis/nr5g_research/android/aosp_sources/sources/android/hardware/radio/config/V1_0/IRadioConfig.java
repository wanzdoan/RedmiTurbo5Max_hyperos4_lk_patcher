package android.hardware.radio.config.V1_0;

import android.internal.hidl.base.V1_0.IBase;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface IRadioConfig extends IBase {
    void getSimSlotsStatus(int i);

    void setResponseFunctions(IRadioConfigResponse iRadioConfigResponse, IRadioConfigIndication iRadioConfigIndication);

    void setSimSlotsMapping(int i, ArrayList arrayList);
}
