package ai.onnxruntime;

/* JADX INFO: loaded from: classes.dex */
public class OrtException extends Exception {
    private static final long serialVersionUID = 1;
    private final OrtErrorCode errorCode;

    public OrtException(String str) {
        super(str);
        this.errorCode = OrtErrorCode.ORT_JAVA_UNKNOWN;
    }

    public OrtException(int i, String str) {
        this(OrtErrorCode.mapFromInt(i), str);
    }

    public OrtException(OrtErrorCode ortErrorCode, String str) {
        super("Error code - " + ortErrorCode + " - message: " + str);
        this.errorCode = ortErrorCode;
    }

    public OrtErrorCode getCode() {
        return this.errorCode;
    }

    public enum OrtErrorCode {
        ORT_JAVA_UNKNOWN(-1),
        ORT_OK(0),
        ORT_FAIL(1),
        ORT_INVALID_ARGUMENT(2),
        ORT_NO_SUCHFILE(3),
        ORT_NO_MODEL(4),
        ORT_ENGINE_ERROR(5),
        ORT_RUNTIME_EXCEPTION(6),
        ORT_INVALID_PROTOBUF(7),
        ORT_MODEL_LOADED(8),
        ORT_NOT_IMPLEMENTED(9),
        ORT_INVALID_GRAPH(10),
        ORT_EP_FAIL(11);

        private static final OrtErrorCode[] values = new OrtErrorCode[12];
        private final int value;

        static {
            for (OrtErrorCode ortErrorCode : values()) {
                if (ortErrorCode != ORT_JAVA_UNKNOWN) {
                    values[ortErrorCode.value] = ortErrorCode;
                }
            }
        }

        OrtErrorCode(int i) {
            this.value = i;
        }

        public static OrtErrorCode mapFromInt(int i) {
            if (i >= 0) {
                OrtErrorCode[] ortErrorCodeArr = values;
                if (i < ortErrorCodeArr.length) {
                    return ortErrorCodeArr[i];
                }
            }
            return ORT_JAVA_UNKNOWN;
        }
    }
}
