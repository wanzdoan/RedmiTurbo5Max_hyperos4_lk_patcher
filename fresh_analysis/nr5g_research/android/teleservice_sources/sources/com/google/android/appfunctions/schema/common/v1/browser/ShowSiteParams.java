package com.google.android.appfunctions.schema.common.v1.browser;

import com.google.android.appfunctions.schema.common.v1.types.Uri;

/* JADX INFO: compiled from: Browser.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class ShowSiteParams {
    public final boolean shouldShowInNewTab;

    public abstract String getId();

    public abstract String getNamespace();

    public abstract Uri getUrl();
}
