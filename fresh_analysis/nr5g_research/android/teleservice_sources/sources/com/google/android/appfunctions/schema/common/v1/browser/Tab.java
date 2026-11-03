package com.google.android.appfunctions.schema.common.v1.browser;

import com.google.android.appfunctions.schema.common.v1.types.Uri;

/* JADX INFO: compiled from: Browser.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class Tab {
    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getTitle();

    public abstract Uri getUrl();
}
