package com.google.android.appfunctions.schema.common.v1.memory;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;
import java.util.List;

/* JADX INFO: compiled from: Memory.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class MemoryItem {
    public abstract String getAdditionalMetadataJson();

    public abstract DateTime getCreatedAt();

    public abstract String getId();

    public abstract List getKeywords();

    public abstract DateTime getLastUpdatedAt();

    public abstract String getNamespace();

    public abstract String getSummary();

    public abstract String getTextContent();

    public abstract String getTitle();
}
