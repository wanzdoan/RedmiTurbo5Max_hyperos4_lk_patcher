package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class SubmodeInfoS {
    public int submode = 0;
    public int txConcurrency = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != SubmodeInfoS.class) {
            return false;
        }
        SubmodeInfoS submodeInfoS = (SubmodeInfoS) obj;
        return this.submode == submodeInfoS.submode && this.txConcurrency == submodeInfoS.txConcurrency;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.submode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.txConcurrency))));
    }

    public static final SubmodeInfoS readFromParcel(HwParcel hwParcel) {
        SubmodeInfoS submodeInfoS = new SubmodeInfoS();
        HwBlob buffer = hwParcel.readBuffer(8L);
        submodeInfoS.submode = buffer.getInt32(0L);
        submodeInfoS.txConcurrency = buffer.getInt32(4L);
        return submodeInfoS;
    }
}
