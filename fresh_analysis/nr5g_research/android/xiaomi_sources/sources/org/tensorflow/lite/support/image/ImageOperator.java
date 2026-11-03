package org.tensorflow.lite.support.image;

import android.graphics.PointF;
import org.tensorflow.lite.support.common.Operator;

/* JADX INFO: loaded from: classes.dex */
public interface ImageOperator extends Operator<TensorImage> {
    @Override // org.tensorflow.lite.support.common.Operator
    TensorImage apply(TensorImage tensorImage);

    int getOutputImageHeight(int i, int i2);

    int getOutputImageWidth(int i, int i2);

    PointF inverseTransform(PointF pointF, int i, int i2);
}
