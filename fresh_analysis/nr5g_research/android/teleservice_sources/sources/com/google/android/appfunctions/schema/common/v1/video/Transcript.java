package com.google.android.appfunctions.schema.common.v1.video;

import com.google.android.appfunctions.schema.common.v1.types.Uri;
import java.util.List;

/* JADX INFO: compiled from: Video.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class Transcript {
    public abstract Uri getContent();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getText();

    public abstract List getTimedText();
}
