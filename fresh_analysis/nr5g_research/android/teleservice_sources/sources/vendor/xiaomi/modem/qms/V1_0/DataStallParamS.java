package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class DataStallParamS {
    public int trafficDirection = 0;
    public boolean dataStallStatus = false;
    public int appType = 0;
    public int expectedIpPacketCount = 0;
    public int expectedDataRate = 0;
    public int lostIpPacketCount = 0;
    public int measurementInterval = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != DataStallParamS.class) {
            return false;
        }
        DataStallParamS dataStallParamS = (DataStallParamS) obj;
        return this.trafficDirection == dataStallParamS.trafficDirection && this.dataStallStatus == dataStallParamS.dataStallStatus && this.appType == dataStallParamS.appType && this.expectedIpPacketCount == dataStallParamS.expectedIpPacketCount && this.expectedDataRate == dataStallParamS.expectedDataRate && this.lostIpPacketCount == dataStallParamS.lostIpPacketCount && this.measurementInterval == dataStallParamS.measurementInterval;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.trafficDirection))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.dataStallStatus))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.appType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.expectedIpPacketCount))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.expectedDataRate))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.lostIpPacketCount))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.measurementInterval))));
    }

    public final String toString() {
        return "{.trafficDirection = " + trafficDirectionE.toString(this.trafficDirection) + ", .dataStallStatus = " + this.dataStallStatus + ", .appType = " + applicationTypeE.toString(this.appType) + ", .expectedIpPacketCount = " + this.expectedIpPacketCount + ", .expectedDataRate = " + this.expectedDataRate + ", .lostIpPacketCount = " + this.lostIpPacketCount + ", .measurementInterval = " + this.measurementInterval + "}";
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(28);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j, this.trafficDirection);
        hwBlob.putBool(4 + j, this.dataStallStatus);
        hwBlob.putInt32(8 + j, this.appType);
        hwBlob.putInt32(12 + j, this.expectedIpPacketCount);
        hwBlob.putInt32(16 + j, this.expectedDataRate);
        hwBlob.putInt32(20 + j, this.lostIpPacketCount);
        hwBlob.putInt32(j + 24, this.measurementInterval);
    }
}
