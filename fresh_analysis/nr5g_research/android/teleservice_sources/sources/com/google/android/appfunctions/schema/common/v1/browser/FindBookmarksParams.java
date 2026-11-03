package com.google.android.appfunctions.schema.common.v1.browser;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Browser.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindBookmarksParams {
    public abstract DateTime getEndDate();

    public abstract String getId();

    public abstract int getMaxCount();

    public abstract String getNamespace();

    public abstract String getQuery();

    public abstract DateTime getStartDate();
}
