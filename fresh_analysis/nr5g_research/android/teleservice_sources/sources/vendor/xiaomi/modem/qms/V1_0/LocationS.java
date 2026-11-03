package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class LocationS {
    public int longitude = 0;
    public int latitude = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != LocationS.class) {
            return false;
        }
        LocationS locationS = (LocationS) obj;
        return this.longitude == locationS.longitude && this.latitude == locationS.latitude;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.longitude))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.latitude))));
    }

    public final String toString() {
        return "{.longitude = " + this.longitude + ", .latitude = " + this.latitude + "}";
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j, this.longitude);
        hwBlob.putInt32(j + 4, this.latitude);
    }
}
