package kotlin.sequences;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: compiled from: SequencesJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
public final class ConstrainedOnceSequence implements Sequence {
    private final AtomicReference sequenceRef;

    public ConstrainedOnceSequence(Sequence sequence) {
        sequence.getClass();
        this.sequenceRef = new AtomicReference(sequence);
    }

    @Override // kotlin.sequences.Sequence
    public Iterator iterator() {
        Sequence sequence = (Sequence) this.sequenceRef.getAndSet(null);
        if (sequence == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("This sequence can be consumed only once.");
            return null;
        }
        return sequence.iterator();
    }
}
