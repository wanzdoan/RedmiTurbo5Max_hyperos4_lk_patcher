package ai.onnxruntime.providers;

/* JADX INFO: loaded from: classes.dex */
public enum CoreMLFlags implements OrtFlags {
    CPU_ONLY(1),
    ENABLE_ON_SUBGRAPH(2),
    ONLY_ENABLE_DEVICE_WITH_ANE(4);

    public final int value;

    CoreMLFlags(int i) {
        this.value = i;
    }

    @Override // ai.onnxruntime.providers.OrtFlags
    public int getValue() {
        return this.value;
    }
}
