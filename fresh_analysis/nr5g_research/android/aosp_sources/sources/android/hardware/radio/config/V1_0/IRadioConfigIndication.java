package android.hardware.radio.config.V1_0;

import android.internal.hidl.base.V1_0.IBase;
import android.os.IHwBinder;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface IRadioConfigIndication extends IBase {
    IHwBinder asBinder();

    void simSlotsStatusChanged(int i, ArrayList arrayList);
}
