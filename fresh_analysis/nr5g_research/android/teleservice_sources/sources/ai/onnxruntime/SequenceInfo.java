package ai.onnxruntime;

/* JADX INFO: loaded from: classes.dex */
public class SequenceInfo implements ValueInfo {
    public final int length;
    public final MapInfo mapInfo;
    public final boolean sequenceOfMaps;
    public final OnnxJavaType sequenceType;

    SequenceInfo(int i, OnnxJavaType onnxJavaType) {
        this.length = i;
        this.sequenceType = onnxJavaType;
        this.sequenceOfMaps = false;
        this.mapInfo = null;
    }

    SequenceInfo(int i, int i2) {
        this.length = i;
        this.sequenceType = OnnxJavaType.mapFromOnnxTensorType(TensorInfo.OnnxTensorType.mapFromInt(i2));
        this.sequenceOfMaps = false;
        this.mapInfo = null;
    }

    SequenceInfo(int i, MapInfo mapInfo) {
        this.length = i;
        this.sequenceOfMaps = true;
        this.mapInfo = mapInfo;
        this.sequenceType = OnnxJavaType.UNKNOWN;
    }

    SequenceInfo(int i, OnnxJavaType onnxJavaType, OnnxJavaType onnxJavaType2) {
        this.length = i;
        this.sequenceType = OnnxJavaType.UNKNOWN;
        this.sequenceOfMaps = true;
        this.mapInfo = new MapInfo(onnxJavaType, onnxJavaType2);
    }

    public boolean isSequenceOfMaps() {
        return this.sequenceOfMaps;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("SequenceInfo(length=");
        int i = this.length;
        sb.append(i == -1 ? "UNKNOWN" : Integer.valueOf(i));
        String string = sb.toString();
        if (this.sequenceOfMaps) {
            return string + ",type=" + this.mapInfo.toString() + ")";
        }
        return string + ",type=" + this.sequenceType.toString() + ")";
    }
}
