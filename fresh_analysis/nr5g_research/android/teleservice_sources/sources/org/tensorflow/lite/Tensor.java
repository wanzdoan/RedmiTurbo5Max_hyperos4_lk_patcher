package org.tensorflow.lite;

/* JADX INFO: loaded from: classes2.dex */
public interface Tensor {

    public class QuantizationParams {
        private final float scale;
        private final int zeroPoint;

        public QuantizationParams(float f, int i) {
            this.scale = f;
            this.zeroPoint = i;
        }
    }
}
