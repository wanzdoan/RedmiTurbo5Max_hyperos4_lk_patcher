package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public final class DataRecommendationS {
    public int dataRecommendationAction = 0;
    public int dataRecommendationSubscription = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != DataRecommendationS.class) {
            return false;
        }
        DataRecommendationS dataRecommendationS = (DataRecommendationS) obj;
        return this.dataRecommendationAction == dataRecommendationS.dataRecommendationAction && this.dataRecommendationSubscription == dataRecommendationS.dataRecommendationSubscription;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.dataRecommendationAction))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.dataRecommendationSubscription))));
    }

    public static final DataRecommendationS readFromParcel(HwParcel hwParcel) {
        DataRecommendationS dataRecommendationS = new DataRecommendationS();
        HwBlob buffer = hwParcel.readBuffer(8L);
        dataRecommendationS.dataRecommendationAction = buffer.getInt32(0L);
        dataRecommendationS.dataRecommendationSubscription = buffer.getInt32(4L);
        return dataRecommendationS;
    }

    public final String toString() {
        return "{.dataRecommendationAction = " + this.dataRecommendationAction + ", .dataRecommendationSubscription = " + this.dataRecommendationSubscription + "}";
    }
}
