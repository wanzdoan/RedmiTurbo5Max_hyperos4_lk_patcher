package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class PreferredCellInfoS {
    public CellInfoS cell_info = new CellInfoS();
    public int cause_bitmask = 0;
    public boolean priority_valid = false;
    public byte priority = 0;
    public boolean location_valid = false;
    public LocationS Location = new LocationS();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != PreferredCellInfoS.class) {
            return false;
        }
        PreferredCellInfoS preferredCellInfoS = (PreferredCellInfoS) obj;
        return HidlSupport.deepEquals(this.cell_info, preferredCellInfoS.cell_info) && this.cause_bitmask == preferredCellInfoS.cause_bitmask && this.priority_valid == preferredCellInfoS.priority_valid && this.priority == preferredCellInfoS.priority && this.location_valid == preferredCellInfoS.location_valid && HidlSupport.deepEquals(this.Location, preferredCellInfoS.Location);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.cell_info)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.cause_bitmask))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.priority_valid))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.priority))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.location_valid))), Integer.valueOf(HidlSupport.deepHashCode(this.Location)));
    }

    public final String toString() {
        return "{.cell_info = " + this.cell_info + ", .cause_bitmask = " + this.cause_bitmask + ", .priority_valid = " + this.priority_valid + ", .priority = " + ((int) this.priority) + ", .location_valid = " + this.location_valid + ", .Location = " + this.Location + "}";
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.cell_info.writeEmbeddedToBlob(hwBlob, j);
        hwBlob.putInt32(32 + j, this.cause_bitmask);
        hwBlob.putBool(36 + j, this.priority_valid);
        hwBlob.putInt8(37 + j, this.priority);
        hwBlob.putBool(38 + j, this.location_valid);
        this.Location.writeEmbeddedToBlob(hwBlob, j + 40);
    }
}
