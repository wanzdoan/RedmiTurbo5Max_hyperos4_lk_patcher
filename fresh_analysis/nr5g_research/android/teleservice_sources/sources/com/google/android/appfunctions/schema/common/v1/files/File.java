package com.google.android.appfunctions.schema.common.v1.files;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Files.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class File {
    public abstract DateTime getDateCreated();

    public abstract String getId();

    public abstract String getName();

    public abstract String getNamespace();
}
