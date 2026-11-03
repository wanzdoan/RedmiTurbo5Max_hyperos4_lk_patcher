package com.google.android.appfunctions.schema.common.v1.clock;

import com.google.android.appfunctions.schema.common.v1.types.SetLongField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField;

/* JADX INFO: compiled from: Clock.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateTimerParams {
    public abstract SetLongField getDurationMs();

    public abstract String getId();

    public abstract SetStringNullableField getLabel();

    public abstract String getNamespace();

    public abstract String getTimerId();

    public abstract SetStringField getTimerState();
}
