package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class Nr5gNflEntryTypeS {
    public int arfcn = 0;
    public int activeBand = 0;
    public int scsType = 0;
    public short plmnIdArrayLength = 0;
    public ArrayList plmnIdArray = new ArrayList();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != Nr5gNflEntryTypeS.class) {
            return false;
        }
        Nr5gNflEntryTypeS nr5gNflEntryTypeS = (Nr5gNflEntryTypeS) obj;
        return this.arfcn == nr5gNflEntryTypeS.arfcn && this.activeBand == nr5gNflEntryTypeS.activeBand && this.scsType == nr5gNflEntryTypeS.scsType && this.plmnIdArrayLength == nr5gNflEntryTypeS.plmnIdArrayLength && HidlSupport.deepEquals(this.plmnIdArray, nr5gNflEntryTypeS.plmnIdArray);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.arfcn))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.activeBand))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.scsType))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.plmnIdArrayLength))), Integer.valueOf(HidlSupport.deepHashCode(this.plmnIdArray)));
    }

    public final String toString() {
        return "{.arfcn = " + this.arfcn + ", .activeBand = " + activeBandE.toString(this.activeBand) + ", .scsType = " + nr5gScsTypeE.toString(this.scsType) + ", .plmnIdArrayLength = " + ((int) this.plmnIdArrayLength) + ", .plmnIdArray = " + this.plmnIdArray + "}";
    }

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob buffer = hwParcel.readBuffer(16L);
        int int32 = buffer.getInt32(8L);
        HwBlob embeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 32, buffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            Nr5gNflEntryTypeS nr5gNflEntryTypeS = new Nr5gNflEntryTypeS();
            nr5gNflEntryTypeS.readEmbeddedFromParcel(hwParcel, embeddedBuffer, i * 32);
            arrayList.add(nr5gNflEntryTypeS);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.arfcn = hwBlob.getInt32(j);
        this.activeBand = hwBlob.getInt32(4 + j);
        this.scsType = hwBlob.getInt32(8 + j);
        this.plmnIdArrayLength = hwBlob.getInt16(12 + j);
        long j2 = j + 16;
        int int32 = hwBlob.getInt32(j + 24);
        HwBlob embeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 6, hwBlob.handle(), j2, true);
        this.plmnIdArray.clear();
        for (int i = 0; i < int32; i++) {
            PlmnIdS plmnIdS = new PlmnIdS();
            plmnIdS.readEmbeddedFromParcel(hwParcel, embeddedBuffer, i * 6);
            this.plmnIdArray.add(plmnIdS);
        }
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 32);
        for (int i = 0; i < size; i++) {
            ((Nr5gNflEntryTypeS) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, i * 32);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j, this.arfcn);
        hwBlob.putInt32(4 + j, this.activeBand);
        hwBlob.putInt32(8 + j, this.scsType);
        hwBlob.putInt16(12 + j, this.plmnIdArrayLength);
        int size = this.plmnIdArray.size();
        long j2 = 16 + j;
        hwBlob.putInt32(24 + j, size);
        hwBlob.putBool(j + 28, false);
        HwBlob hwBlob2 = new HwBlob(size * 6);
        for (int i = 0; i < size; i++) {
            ((PlmnIdS) this.plmnIdArray.get(i)).writeEmbeddedToBlob(hwBlob2, i * 6);
        }
        hwBlob.putBlob(j2, hwBlob2);
    }
}
