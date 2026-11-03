package org.tensorflow.lite.support.common;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
public interface TensorOperator extends Operator<TensorBuffer> {
    @Override // org.tensorflow.lite.support.common.Operator
    TensorBuffer apply(TensorBuffer tensorBuffer);
}
