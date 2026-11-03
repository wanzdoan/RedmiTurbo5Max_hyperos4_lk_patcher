package androidx.appsearch.builtintypes;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class Thing {
    public abstract List getAlternateNames();

    public abstract long getCreationTimestampMillis();

    public abstract String getDescription();

    public abstract int getDocumentScore();

    public abstract long getDocumentTtlMillis();

    public abstract String getId();

    public abstract String getImage();

    public abstract String getName();

    public abstract String getNamespace();

    public abstract List getPotentialActions();

    public abstract String getUrl();
}
