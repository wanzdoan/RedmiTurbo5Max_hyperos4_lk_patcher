package com.android.phone.streaming;

import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class StreamingQoEHandler {
    private final QoECallback mCallback;
    private final StreamingOptimizationState mOptStateMgr;

    public interface QoECallback {
        void onCongestionReason(int i, int i2, int i3, int i4);

        void onNonCongestionReason(int i, int i2, int i3, int i4);

        void onScoreForStableCell(int i);
    }

    private boolean isCongestionReason(int i) {
        return i == 1 || i == 2 || i == 3 || i == 4;
    }

    public StreamingQoEHandler(StreamingOptimizationState streamingOptimizationState, QoECallback qoECallback) {
        Objects.requireNonNull(streamingOptimizationState, "optStateMgr cannot be null");
        this.mOptStateMgr = streamingOptimizationState;
        Objects.requireNonNull(qoECallback, "callback cannot be null");
        this.mCallback = qoECallback;
    }

    public void handleNetworkQoeChanged(int i, int i2, int i3, int i4, int i5) {
        if (isBasicValid(i, i3)) {
            if (!this.mOptStateMgr.getAuxState().isStableCellDecided()) {
                this.mCallback.onScoreForStableCell(i5);
            }
            if (this.mOptStateMgr.getOptState() != 0 || i5 <= 80) {
                this.mOptStateMgr.getAuxState().setLastQoeReason(i4);
                boolean zIsCongestionReason = isCongestionReason(i4);
                QoECallback qoECallback = this.mCallback;
                if (!zIsCongestionReason) {
                    qoECallback.onNonCongestionReason(i, i2, i4, i5);
                } else {
                    qoECallback.onCongestionReason(i, i2, i4, i5);
                }
            }
        }
    }

    private boolean isBasicValid(int i, int i2) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && !MiuiPhoneUtils.isDuringCall()) {
            return i == this.mOptStateMgr.getSceneContext().getDdsSlot() && isAppTypeNeedHandle(i2) && this.mOptStateMgr.getSceneContext().isMobileDataConnected();
        }
        Log.d("StreamingQoEHandler", "Invalid slotId or during call, skip handling");
        return false;
    }

    public boolean isAppTypeNeedHandle(int i) {
        return (i == 1 || i == 2) && this.mOptStateMgr.getSceneContext().getAppType() == 1;
    }
}
