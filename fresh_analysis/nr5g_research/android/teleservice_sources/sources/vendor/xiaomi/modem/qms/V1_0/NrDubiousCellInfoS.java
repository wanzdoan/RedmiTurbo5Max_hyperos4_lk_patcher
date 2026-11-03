package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class NrDubiousCellInfoS {
    public DubiousCellInfoS cellInfo = new DubiousCellInfoS();
    public long cgi = 0;
    public int nrScsType = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != NrDubiousCellInfoS.class) {
            return false;
        }
        NrDubiousCellInfoS nrDubiousCellInfoS = (NrDubiousCellInfoS) obj;
        return HidlSupport.deepEquals(this.cellInfo, nrDubiousCellInfoS.cellInfo) && this.cgi == nrDubiousCellInfoS.cgi && this.nrScsType == nrDubiousCellInfoS.nrScsType;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.cellInfo)), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.cgi))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.nrScsType))));
    }

    public final String toString() {
        return "{.cellInfo = " + this.cellInfo + ", .cgi = " + this.cgi + ", .nrScsType = " + nr5gScsTypeE.toString(this.nrScsType) + "}";
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 40);
        for (int i = 0; i < size; i++) {
            ((NrDubiousCellInfoS) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, i * 40);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.cellInfo.writeEmbeddedToBlob(hwBlob, j);
        hwBlob.putInt64(24 + j, this.cgi);
        hwBlob.putInt32(j + 32, this.nrScsType);
    }
}
