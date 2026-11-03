package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class LteDubiousCellInfoS {
    public DubiousCellInfoS cellInfo = new DubiousCellInfoS();
    public int cgi = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != LteDubiousCellInfoS.class) {
            return false;
        }
        LteDubiousCellInfoS lteDubiousCellInfoS = (LteDubiousCellInfoS) obj;
        return HidlSupport.deepEquals(this.cellInfo, lteDubiousCellInfoS.cellInfo) && this.cgi == lteDubiousCellInfoS.cgi;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.cellInfo)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.cgi))));
    }

    public final String toString() {
        return "{.cellInfo = " + this.cellInfo + ", .cgi = " + this.cgi + "}";
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 28);
        for (int i = 0; i < size; i++) {
            ((LteDubiousCellInfoS) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, i * 28);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.cellInfo.writeEmbeddedToBlob(hwBlob, j);
        hwBlob.putInt32(j + 24, this.cgi);
    }
}
