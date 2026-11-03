package androidx.appsearch.usagereporting;

/* JADX INFO: loaded from: classes.dex */
public abstract class TakenAction {
    public abstract long getActionTimestampMillis();

    public abstract int getActionType();

    public abstract long getDocumentTtlMillis();

    public abstract String getId();

    public abstract String getNamespace();
}
