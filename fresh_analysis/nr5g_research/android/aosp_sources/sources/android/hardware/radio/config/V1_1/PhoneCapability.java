package android.hardware.radio.config.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class PhoneCapability {
    public byte maxActiveData = 0;
    public byte maxActiveInternetData = 0;
    public boolean isInternetLingeringSupported = false;
    public ArrayList logicalModemList = new ArrayList();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != PhoneCapability.class) {
            return false;
        }
        PhoneCapability phoneCapability = (PhoneCapability) obj;
        return this.maxActiveData == phoneCapability.maxActiveData && this.maxActiveInternetData == phoneCapability.maxActiveInternetData && this.isInternetLingeringSupported == phoneCapability.isInternetLingeringSupported && HidlSupport.deepEquals(this.logicalModemList, phoneCapability.logicalModemList);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.maxActiveData))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.maxActiveInternetData))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isInternetLingeringSupported))), Integer.valueOf(HidlSupport.deepHashCode(this.logicalModemList)));
    }

    public final String toString() {
        return "{.maxActiveData = " + ((int) this.maxActiveData) + ", .maxActiveInternetData = " + ((int) this.maxActiveInternetData) + ", .isInternetLingeringSupported = " + this.isInternetLingeringSupported + ", .logicalModemList = " + this.logicalModemList + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(24L), 0L);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.maxActiveData = hwBlob.getInt8(j);
        this.maxActiveInternetData = hwBlob.getInt8(1 + j);
        this.isInternetLingeringSupported = hwBlob.getBool(2 + j);
        long j2 = j + 8;
        int int32 = hwBlob.getInt32(j + 16);
        HwBlob embeddedBuffer = hwParcel.readEmbeddedBuffer(int32, hwBlob.handle(), j2, true);
        this.logicalModemList.clear();
        for (int i = 0; i < int32; i++) {
            ModemInfo modemInfo = new ModemInfo();
            modemInfo.readEmbeddedFromParcel(hwParcel, embeddedBuffer, i);
            this.logicalModemList.add(modemInfo);
        }
    }
}
