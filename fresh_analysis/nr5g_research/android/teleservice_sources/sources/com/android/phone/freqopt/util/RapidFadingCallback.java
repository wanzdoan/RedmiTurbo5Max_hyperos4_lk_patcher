package com.android.phone.freqopt.util;

import com.xiaomi.telephony.CommMsgCallback;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public abstract class RapidFadingCallback extends CommMsgCallback {
    public abstract void onRapidFadingEvent(RapidFadingInfo rapidFadingInfo);

    public void onUnsolCommMsg(int i, int i2, byte[] bArr) {
        if (i2 != 5 || bArr == null) {
            return;
        }
        if (bArr.length < 28) {
            LogUtils.w("Freq_RapidFadingCb", "onUnsolCommMsg: data too short, expected=28, actual=" + bArr.length);
        } else {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            onRapidFadingEvent(new RapidFadingInfo(byteBufferWrap.getInt(), byteBufferWrap.getLong(), byteBufferWrap.getInt(), byteBufferWrap.getShort() & 65535, byteBufferWrap.getFloat(), byteBufferWrap.getFloat(), byteBufferWrap.get(), byteBufferWrap.get()));
        }
    }

    public static boolean register(int i, RapidFadingCallback rapidFadingCallback) {
        LogUtils.d("Freq_RapidFadingCb", "register slotId=" + i);
        return XmTelephonyManagerInner.getDefault().registerCommMsgCallback(i, 5, rapidFadingCallback);
    }

    public static boolean unregister(int i, RapidFadingCallback rapidFadingCallback) {
        LogUtils.d("Freq_RapidFadingCb", "unregister slotId=" + i);
        return XmTelephonyManagerInner.getDefault().unRegisterCommMsgCallback(i, 5, rapidFadingCallback);
    }

    public class RapidFadingInfo {
        public final int arfcn;
        public final byte eventType;
        public final int pci;
        public final float rsrp;
        public final float snr;
        public final byte state;
        public final int subId;
        public final long timestamp;

        public RapidFadingInfo(int i, long j, int i2, int i3, float f, float f2, byte b, byte b2) {
            this.subId = i;
            this.timestamp = j;
            this.arfcn = i2;
            this.pci = i3;
            this.rsrp = f;
            this.snr = f2;
            this.eventType = b;
            this.state = b2;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("RapidFadingInfo{subId=");
            sb.append(this.subId);
            sb.append(", timestamp=");
            sb.append(this.timestamp);
            sb.append(", arfcn=");
            sb.append(this.arfcn);
            sb.append(", pci=");
            sb.append(this.pci);
            sb.append(", rsrp=");
            sb.append(this.rsrp);
            sb.append(", snr=");
            sb.append(this.snr);
            sb.append(", eventType=");
            sb.append(this.eventType == 0 ? "FAST_FADING_START" : "RECOVERY");
            sb.append(", state=");
            sb.append(this.state == 0 ? "NORMAL" : "FAST_FADING");
            sb.append('}');
            return sb.toString();
        }
    }
}
