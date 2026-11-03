package androidx.appsearch.builtintypes;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class ImageObject extends Thing {
    public abstract byte[] getBytes();

    public abstract List getKeywords();

    public abstract String getSha256();

    public abstract String getThumbnailSha256();
}
