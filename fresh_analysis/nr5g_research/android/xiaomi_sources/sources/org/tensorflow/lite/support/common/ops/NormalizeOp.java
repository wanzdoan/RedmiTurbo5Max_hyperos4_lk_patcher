package org.tensorflow.lite.support.common.ops;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat;

/* JADX INFO: loaded from: classes.dex */
public class NormalizeOp implements TensorOperator {
    private final boolean isIdentityOp;
    private final float[] mean;
    private final int numChannels;
    private final float[] stddev;

    public NormalizeOp(float mean, float stddev) {
        if (mean == 0.0f && (stddev == 0.0f || Float.isInfinite(stddev))) {
            stddev = 1.0f;
        }
        SupportPreconditions.checkArgument(stddev != 0.0f, "Stddev cannot be zero.");
        boolean meansIsZeroAndDevsIs1 = false;
        if (mean == 0.0f && stddev == 1.0f) {
            meansIsZeroAndDevsIs1 = true;
        }
        this.isIdentityOp = meansIsZeroAndDevsIs1;
        this.mean = new float[]{mean};
        this.stddev = new float[]{stddev};
        this.numChannels = 1;
    }

    public NormalizeOp(float[] mean, float[] stddev) {
        SupportPreconditions.checkNotNull(mean, "Mean cannot be null");
        SupportPreconditions.checkNotNull(stddev, "Stddev cannot be null");
        SupportPreconditions.checkArgument(mean.length == stddev.length, "Per channel normalization requires same number of means and stddevs");
        SupportPreconditions.checkArgument(mean.length > 0, "Means and stddevs are empty.");
        this.mean = (float[]) mean.clone();
        this.stddev = (float[]) stddev.clone();
        boolean allMeansAreZeroAndAllDevsAre1 = true;
        this.numChannels = mean.length;
        for (int i = 0; i < this.numChannels; i++) {
            SupportPreconditions.checkArgument(this.stddev[i] != 0.0f, "Stddev cannot be zero.");
            if (this.stddev[i] != 1.0f || this.mean[i] != 0.0f) {
                allMeansAreZeroAndAllDevsAre1 = false;
            }
        }
        this.isIdentityOp = allMeansAreZeroAndAllDevsAre1;
    }

    @Override // org.tensorflow.lite.support.common.TensorOperator, org.tensorflow.lite.support.common.Operator
    public TensorBuffer apply(TensorBuffer input) {
        TensorBuffer output;
        if (this.isIdentityOp) {
            return input;
        }
        int[] shape = input.getShape();
        boolean z = true;
        if (this.numChannels != 1 && (shape.length == 0 || shape[shape.length - 1] != this.numChannels)) {
            z = false;
        }
        SupportPreconditions.checkArgument(z, "Number of means (stddevs) is not same with number of channels (size of last axis).");
        float[] values = input.getFloatArray();
        int j = 0;
        for (int i = 0; i < values.length; i++) {
            values[i] = (values[i] - this.mean[j]) / this.stddev[j];
            j = (j + 1) % this.numChannels;
        }
        if (input.isDynamic()) {
            output = TensorBufferFloat.createDynamic(DataType.FLOAT32);
        } else {
            output = TensorBufferFloat.createFixedSize(shape, DataType.FLOAT32);
        }
        output.loadArray(values, shape);
        return output;
    }
}
