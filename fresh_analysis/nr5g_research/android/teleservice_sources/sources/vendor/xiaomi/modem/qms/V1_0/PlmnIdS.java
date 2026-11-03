package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class PlmnIdS {
    public short mcc = 0;
    public short mnc = 0;
    public boolean mncIncludesPcsDigit = false;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != PlmnIdS.class) {
            return false;
        }
        PlmnIdS plmnIdS = (PlmnIdS) obj;
        return this.mcc == plmnIdS.mcc && this.mnc == plmnIdS.mnc && this.mncIncludesPcsDigit == plmnIdS.mncIncludesPcsDigit;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.mcc))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.mnc))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.mncIncludesPcsDigit))));
    }

    public final String toString() {
        return "{.mcc = " + ((int) this.mcc) + ", .mnc = " + ((int) this.mnc) + ", .mncIncludesPcsDigit = " + this.mncIncludesPcsDigit + "}";
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.mcc = hwBlob.getInt16(j);
        this.mnc = hwBlob.getInt16(2 + j);
        this.mncIncludesPcsDigit = hwBlob.getBool(j + 4);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt16(j, this.mcc);
        hwBlob.putInt16(2 + j, this.mnc);
        hwBlob.putBool(j + 4, this.mncIncludesPcsDigit);
    }
}
