package androidx.appsearch.usagereporting;

/* JADX INFO: loaded from: classes.dex */
public abstract class ClickAction extends TakenAction {
    public abstract String getQuery();

    public abstract String getReferencedQualifiedId();

    public abstract int getResultRankGlobal();

    public abstract int getResultRankInBlock();

    public abstract long getTimeStayOnResultMillis();
}
