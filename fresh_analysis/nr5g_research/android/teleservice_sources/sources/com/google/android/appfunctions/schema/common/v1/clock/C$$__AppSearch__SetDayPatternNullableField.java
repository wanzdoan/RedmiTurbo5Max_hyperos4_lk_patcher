package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__SetDayPatternNullableField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDayPatternNullableField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDayPatternNullableField setDayPatternNullableField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDayPatternNullableField.getNamespace(), setDayPatternNullableField.getId(), "com.google.android.appfunctions.schema.common.v1.clock.SetDayPatternNullableField");
        setDayPatternNullableField.getValue();
        return builder.build();
    }
}
