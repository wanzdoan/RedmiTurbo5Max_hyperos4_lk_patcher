package org.tensorflow.lite.support.label;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/* JADX INFO: loaded from: classes.dex */
public class TensorLabel {
    private final Map<Integer, List<String>> axisLabels;
    private final int[] shape;
    private final TensorBuffer tensorBuffer;

    public TensorLabel(Map<Integer, List<String>> axisLabels, TensorBuffer tensorBuffer) {
        SupportPreconditions.checkNotNull(axisLabels, "Axis labels cannot be null.");
        SupportPreconditions.checkNotNull(tensorBuffer, "Tensor Buffer cannot be null.");
        this.axisLabels = axisLabels;
        this.tensorBuffer = tensorBuffer;
        this.shape = tensorBuffer.getShape();
        for (Map.Entry<Integer, List<String>> entry : axisLabels.entrySet()) {
            int axis = entry.getKey().intValue();
            boolean z = true;
            SupportPreconditions.checkArgument(axis >= 0 && axis < this.shape.length, "Invalid axis id: " + axis);
            SupportPreconditions.checkNotNull(entry.getValue(), "Label list is null on axis " + axis);
            if (this.shape[axis] != entry.getValue().size()) {
                z = false;
            }
            SupportPreconditions.checkArgument(z, "Label number " + entry.getValue().size() + " mismatch the shape on axis " + axis);
        }
    }

    public TensorLabel(List<String> axisLabels, TensorBuffer tensorBuffer) {
        this(makeMap(getFirstAxisWithSizeGreaterThanOne(tensorBuffer), axisLabels), tensorBuffer);
    }

    public Map<String, TensorBuffer> getMapWithTensorBuffer() {
        int labeledAxis = getFirstAxisWithSizeGreaterThanOne(this.tensorBuffer);
        Map<String, TensorBuffer> labelToTensorMap = new LinkedHashMap<>();
        SupportPreconditions.checkArgument(this.axisLabels.containsKey(Integer.valueOf(labeledAxis)), "get a <String, TensorBuffer> map requires the labels are set on the first non-1 axis.");
        List<String> labels = this.axisLabels.get(Integer.valueOf(labeledAxis));
        DataType dataType = this.tensorBuffer.getDataType();
        int typeSize = this.tensorBuffer.getTypeSize();
        int flatSize = this.tensorBuffer.getFlatSize();
        ByteBuffer byteBuffer = this.tensorBuffer.getBuffer();
        byteBuffer.rewind();
        int subArrayLength = (flatSize / this.shape[labeledAxis]) * typeSize;
        int i = 0;
        SupportPreconditions.checkNotNull(labels, "Label list should never be null");
        for (String label : labels) {
            byteBuffer.position(i * subArrayLength);
            ByteBuffer subBuffer = byteBuffer.slice();
            subBuffer.order(byteBuffer.order()).limit(subArrayLength);
            TensorBuffer labelBuffer = TensorBuffer.createDynamic(dataType);
            labelBuffer.loadBuffer(subBuffer, Arrays.copyOfRange(this.shape, labeledAxis + 1, this.shape.length));
            labelToTensorMap.put(label, labelBuffer);
            i++;
            labeledAxis = labeledAxis;
        }
        return labelToTensorMap;
    }

    public Map<String, Float> getMapWithFloatValue() {
        int labeledAxis = getFirstAxisWithSizeGreaterThanOne(this.tensorBuffer);
        SupportPreconditions.checkState(labeledAxis == this.shape.length - 1, "get a <String, Scalar> map is only valid when the only labeled axis is the last one.");
        List<String> labels = this.axisLabels.get(Integer.valueOf(labeledAxis));
        float[] data = this.tensorBuffer.getFloatArray();
        SupportPreconditions.checkState(labels.size() == data.length);
        Map<String, Float> result = new LinkedHashMap<>();
        int i = 0;
        for (String label : labels) {
            result.put(label, Float.valueOf(data[i]));
            i++;
        }
        return result;
    }

    public List<Category> getCategoryList() {
        int labeledAxis = getFirstAxisWithSizeGreaterThanOne(this.tensorBuffer);
        SupportPreconditions.checkState(labeledAxis == this.shape.length - 1, "get a Category list is only valid when the only labeled axis is the last one.");
        List<String> labels = this.axisLabels.get(Integer.valueOf(labeledAxis));
        float[] data = this.tensorBuffer.getFloatArray();
        SupportPreconditions.checkState(labels.size() == data.length);
        List<Category> result = new ArrayList<>();
        int i = 0;
        for (String label : labels) {
            result.add(new Category(label, data[i]));
            i++;
        }
        return result;
    }

    private static int getFirstAxisWithSizeGreaterThanOne(TensorBuffer tensorBuffer) {
        int[] shape = tensorBuffer.getShape();
        for (int i = 0; i < shape.length; i++) {
            if (shape[i] > 1) {
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot find an axis to label. A valid axis to label should have size larger than 1.");
    }

    private static Map<Integer, List<String>> makeMap(int axis, List<String> labels) {
        Map<Integer, List<String>> map = new LinkedHashMap<>();
        map.put(Integer.valueOf(axis), labels);
        return map;
    }
}
