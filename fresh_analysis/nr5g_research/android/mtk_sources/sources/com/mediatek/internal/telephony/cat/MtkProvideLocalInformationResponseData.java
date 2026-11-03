package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.ComprehensionTlvTag;
import com.android.internal.telephony.cat.ResponseData;
import java.io.ByteArrayOutputStream;

/* JADX INFO: compiled from: MtkResponseData.java */
/* JADX INFO: loaded from: classes.dex */
class MtkProvideLocalInformationResponseData extends ResponseData {
    private int mBatteryState;

    public MtkProvideLocalInformationResponseData(int batteryState) {
        this.mBatteryState = batteryState;
    }

    public void format(ByteArrayOutputStream buf) {
        int tag = ComprehensionTlvTag.BATTERY_STATE.value() | 128;
        buf.write(tag);
        buf.write(1);
        buf.write(this.mBatteryState);
    }
}
