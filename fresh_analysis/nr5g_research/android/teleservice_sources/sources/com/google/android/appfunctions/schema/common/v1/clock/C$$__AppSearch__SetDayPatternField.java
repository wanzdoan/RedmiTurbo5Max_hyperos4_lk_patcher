package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__SetDayPatternField, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDayPatternField implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDayPatternField setDayPatternField) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDayPatternField.getNamespace(), setDayPatternField.getId(), "com.google.android.appfunctions.schema.common.v1.clock.SetDayPatternField");
        setDayPatternField.getValue();
        return builder.build();
    }
}
