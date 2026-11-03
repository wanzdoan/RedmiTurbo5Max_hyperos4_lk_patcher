package ai.onnxruntime;

import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface OnnxValue extends AutoCloseable {
    @Override // java.lang.AutoCloseable
    void close();

    ValueInfo getInfo();

    OnnxValueType getType();

    Object getValue() throws OrtException;

    public enum OnnxValueType {
        ONNX_TYPE_UNKNOWN(0),
        ONNX_TYPE_TENSOR(1),
        ONNX_TYPE_SEQUENCE(2),
        ONNX_TYPE_MAP(3),
        ONNX_TYPE_OPAQUE(4),
        ONNX_TYPE_SPARSETENSOR(5),
        ONNX_TYPE_OPTIONAL(6);

        public final int value;

        OnnxValueType(int i) {
            this.value = i;
        }
    }

    static void close(Iterable iterable) {
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            ((OnnxValue) it.next()).close();
        }
    }

    static void close(Map map) {
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            ((OnnxValue) it.next()).close();
        }
    }
}
