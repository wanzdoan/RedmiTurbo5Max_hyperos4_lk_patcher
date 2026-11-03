package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class PreferredCellListS {
    public int expiry_timer = 0;
    public byte lte_cell_list_len = 0;
    public ArrayList lte_cell_list = new ArrayList();
    public byte nr_cell_list_len = 0;
    public ArrayList nr_cell_list = new ArrayList();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != PreferredCellListS.class) {
            return false;
        }
        PreferredCellListS preferredCellListS = (PreferredCellListS) obj;
        return this.expiry_timer == preferredCellListS.expiry_timer && this.lte_cell_list_len == preferredCellListS.lte_cell_list_len && HidlSupport.deepEquals(this.lte_cell_list, preferredCellListS.lte_cell_list) && this.nr_cell_list_len == preferredCellListS.nr_cell_list_len && HidlSupport.deepEquals(this.nr_cell_list, preferredCellListS.nr_cell_list);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.expiry_timer))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.lte_cell_list_len))), Integer.valueOf(HidlSupport.deepHashCode(this.lte_cell_list)), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.nr_cell_list_len))), Integer.valueOf(HidlSupport.deepHashCode(this.nr_cell_list)));
    }

    public final String toString() {
        return "{.expiry_timer = " + this.expiry_timer + ", .lte_cell_list_len = " + ((int) this.lte_cell_list_len) + ", .lte_cell_list = " + this.lte_cell_list + ", .nr_cell_list_len = " + ((int) this.nr_cell_list_len) + ", .nr_cell_list = " + this.nr_cell_list + "}";
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(48);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j, this.expiry_timer);
        hwBlob.putInt8(4 + j, this.lte_cell_list_len);
        int size = this.lte_cell_list.size();
        long j2 = 8 + j;
        hwBlob.putInt32(16 + j, size);
        hwBlob.putBool(20 + j, false);
        HwBlob hwBlob2 = new HwBlob(size * 48);
        for (int i = 0; i < size; i++) {
            ((PreferredCellInfoS) this.lte_cell_list.get(i)).writeEmbeddedToBlob(hwBlob2, i * 48);
        }
        hwBlob.putBlob(j2, hwBlob2);
        hwBlob.putInt8(24 + j, this.nr_cell_list_len);
        int size2 = this.nr_cell_list.size();
        long j3 = 32 + j;
        hwBlob.putInt32(40 + j, size2);
        hwBlob.putBool(j + 44, false);
        HwBlob hwBlob3 = new HwBlob(size2 * 48);
        for (int i2 = 0; i2 < size2; i2++) {
            ((PreferredCellInfoS) this.nr_cell_list.get(i2)).writeEmbeddedToBlob(hwBlob3, i2 * 48);
        }
        hwBlob.putBlob(j3, hwBlob3);
    }
}
