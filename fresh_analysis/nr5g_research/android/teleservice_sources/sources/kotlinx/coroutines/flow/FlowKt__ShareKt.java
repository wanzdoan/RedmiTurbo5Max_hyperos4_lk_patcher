package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Share.kt */
/* JADX INFO: loaded from: classes2.dex */
abstract /* synthetic */ class FlowKt__ShareKt {
    public static final StateFlow asStateFlow(MutableStateFlow mutableStateFlow) {
        mutableStateFlow.getClass();
        return new ReadonlyStateFlow(mutableStateFlow, null);
    }
}
