package org.tensorflow.lite;

/* JADX INFO: loaded from: classes2.dex */
public enum DataType {
    FLOAT32(1),
    INT32(2),
    UINT8(3),
    INT64(4),
    STRING(5),
    BOOL(6),
    INT16(7),
    INT8(9);

    private static final DataType[] values = values();
    private final int value;

    DataType(int i) {
        this.value = i;
    }

    public int byteSize() {
        switch (this) {
            case FLOAT32:
            case INT32:
                return 4;
            case UINT8:
            case INT8:
                return 1;
            case INT64:
                return 8;
            case STRING:
            case BOOL:
                return -1;
            case INT16:
                return 2;
            default:
                DataType$$ExternalSyntheticBUOutline0.m("DataType error: DataType ", this, " is not supported yet");
                return 0;
        }
    }
}
