package com.google.android.appfunctions.schema.common.v1.clock;

import com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField;
import com.google.android.appfunctions.schema.common.v1.types.SetTimeOfDayField;

/* JADX INFO: compiled from: Clock.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateAlarmParams {
    public abstract String getAlarmId();

    public abstract SetStringNullableField getAlarmStatus();

    public abstract SetDayPatternNullableField getDayPattern();

    public abstract String getId();

    public abstract SetStringNullableField getLabel();

    public abstract String getNamespace();

    public abstract SetTimeOfDayField getTime();
}
