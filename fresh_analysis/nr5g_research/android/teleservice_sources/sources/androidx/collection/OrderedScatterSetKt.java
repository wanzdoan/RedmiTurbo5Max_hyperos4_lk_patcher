package androidx.collection;

/* JADX INFO: compiled from: OrderedScatterSet.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class OrderedScatterSetKt {
    private static final MutableOrderedScatterSet EmptyOrderedScatterSet = new MutableOrderedScatterSet(0);

    public static final MutableOrderedScatterSet mutableOrderedScatterSetOf() {
        return new MutableOrderedScatterSet(0, 1, null);
    }
}
