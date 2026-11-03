package org.tensorflow.lite.support.common.ops;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
public class CastOp implements TensorOperator {
    private final DataType destinationType;

    public CastOp(DataType destinationType) {
        SupportPreconditions.checkArgument(destinationType == DataType.UINT8 || destinationType == DataType.FLOAT32, "Destination type " + destinationType + " is not supported.");
        this.destinationType = destinationType;
    }

    @Override // org.tensorflow.lite.support.common.TensorOperator, org.tensorflow.lite.support.common.Operator
    public TensorBuffer apply(TensorBuffer input) {
        if (input.getDataType() == this.destinationType) {
            return input;
        }
        return TensorBuffer.createFrom(input, this.destinationType);
    }
}
