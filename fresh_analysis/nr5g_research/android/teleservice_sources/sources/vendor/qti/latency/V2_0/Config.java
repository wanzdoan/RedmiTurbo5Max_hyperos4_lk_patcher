package vendor.qti.latency.V2_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class Config {
    public long rat = 0;
    public long slotId = 0;
    public long uplink = 0;
    public long downlink = 0;
    public boolean enableConnectionExtension = false;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != Config.class) {
            return false;
        }
        Config config = (Config) obj;
        return this.rat == config.rat && this.slotId == config.slotId && this.uplink == config.uplink && this.downlink == config.downlink && this.enableConnectionExtension == config.enableConnectionExtension;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.rat))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.slotId))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.uplink))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.downlink))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.enableConnectionExtension))));
    }

    public final String toString() {
        return "{.rat = " + Radio.toString(this.rat) + ", .slotId = " + SlotId.toString(this.slotId) + ", .uplink = " + Level.toString(this.uplink) + ", .downlink = " + Level.toString(this.downlink) + ", .enableConnectionExtension = " + this.enableConnectionExtension + "}";
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(40);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt64(j, this.rat);
        hwBlob.putInt64(8 + j, this.slotId);
        hwBlob.putInt64(16 + j, this.uplink);
        hwBlob.putInt64(24 + j, this.downlink);
        hwBlob.putBool(j + 32, this.enableConnectionExtension);
    }
}
