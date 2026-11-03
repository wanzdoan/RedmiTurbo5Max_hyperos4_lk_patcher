package kotlinx.coroutines.flow;

import kotlinx.coroutines.Job;

/* JADX INFO: compiled from: Share.kt */
/* JADX INFO: loaded from: classes2.dex */
final class ReadonlyStateFlow implements StateFlow {
    private final /* synthetic */ StateFlow $$delegate_0;

    @Override // kotlinx.coroutines.flow.StateFlow
    public Object getValue() {
        return this.$$delegate_0.getValue();
    }

    public ReadonlyStateFlow(StateFlow stateFlow, Job job) {
        stateFlow.getClass();
        this.$$delegate_0 = stateFlow;
    }
}
