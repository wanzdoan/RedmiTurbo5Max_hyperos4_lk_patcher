package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: AbstractSharedFlow.kt */
/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractSharedFlow {
    private AbstractSharedFlowSlot[] slots;

    protected final AbstractSharedFlowSlot[] getSlots() {
        return this.slots;
    }
}
