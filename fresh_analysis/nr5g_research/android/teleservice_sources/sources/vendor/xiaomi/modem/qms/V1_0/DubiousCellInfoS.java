package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class DubiousCellInfoS {
    public PlmnIdS plmnId = new PlmnIdS();
    public int activeBand = 0;
    public int arfcn = 0;
    public short pci = 0;
    public int causeCodeBitMask = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != DubiousCellInfoS.class) {
            return false;
        }
        DubiousCellInfoS dubiousCellInfoS = (DubiousCellInfoS) obj;
        return HidlSupport.deepEquals(this.plmnId, dubiousCellInfoS.plmnId) && this.activeBand == dubiousCellInfoS.activeBand && this.arfcn == dubiousCellInfoS.arfcn && this.pci == dubiousCellInfoS.pci && this.causeCodeBitMask == dubiousCellInfoS.causeCodeBitMask;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.plmnId)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.activeBand))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.arfcn))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.pci))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.causeCodeBitMask))));
    }

    public final String toString() {
        return "{.plmnId = " + this.plmnId + ", .activeBand = " + activeBandE.toString(this.activeBand) + ", .arfcn = " + this.arfcn + ", .pci = " + ((int) this.pci) + ", .causeCodeBitMask = " + this.causeCodeBitMask + "}";
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.plmnId.writeEmbeddedToBlob(hwBlob, j);
        hwBlob.putInt32(8 + j, this.activeBand);
        hwBlob.putInt32(12 + j, this.arfcn);
        hwBlob.putInt16(16 + j, this.pci);
        hwBlob.putInt32(j + 20, this.causeCodeBitMask);
    }
}
