package com.google.android.appfunctions.schema.common.v1.browser;

import com.google.android.appfunctions.schema.common.v1.types.Uri;

/* JADX INFO: compiled from: Browser.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class CreateBookmarkParams {
    public abstract String getId();

    public abstract String getName();

    public abstract String getNamespace();

    public abstract Uri getUrl();
}
