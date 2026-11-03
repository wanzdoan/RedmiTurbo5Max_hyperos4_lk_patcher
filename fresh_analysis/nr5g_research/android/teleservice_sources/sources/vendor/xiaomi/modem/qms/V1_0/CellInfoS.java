package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class CellInfoS {
    public PlmnIdS plmn = new PlmnIdS();
    public long cgi = 0;
    public int band = 0;
    public int freq = 0;
    public short pci = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != CellInfoS.class) {
            return false;
        }
        CellInfoS cellInfoS = (CellInfoS) obj;
        return HidlSupport.deepEquals(this.plmn, cellInfoS.plmn) && this.cgi == cellInfoS.cgi && this.band == cellInfoS.band && this.freq == cellInfoS.freq && this.pci == cellInfoS.pci;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.plmn)), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.cgi))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.band))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.freq))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.pci))));
    }

    public final String toString() {
        return "{.plmn = " + this.plmn + ", .cgi = " + this.cgi + ", .band = " + activeBandE.toString(this.band) + ", .freq = " + this.freq + ", .pci = " + ((int) this.pci) + "}";
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.plmn.writeEmbeddedToBlob(hwBlob, j);
        hwBlob.putInt64(8 + j, this.cgi);
        hwBlob.putInt32(16 + j, this.band);
        hwBlob.putInt32(20 + j, this.freq);
        hwBlob.putInt16(j + 24, this.pci);
    }
}
