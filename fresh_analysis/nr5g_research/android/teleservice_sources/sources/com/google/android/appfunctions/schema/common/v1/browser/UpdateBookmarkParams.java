package com.google.android.appfunctions.schema.common.v1.browser;

import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetUriField;

/* JADX INFO: compiled from: Browser.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateBookmarkParams {
    public abstract String getBookmarkId();

    public abstract String getId();

    public abstract SetStringField getName();

    public abstract String getNamespace();

    public abstract SetUriField getUrl();
}
