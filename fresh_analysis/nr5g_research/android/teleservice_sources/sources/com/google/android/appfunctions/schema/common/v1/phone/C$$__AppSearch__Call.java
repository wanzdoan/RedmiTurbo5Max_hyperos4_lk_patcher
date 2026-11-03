package com.google.android.appfunctions.schema.common.v1.phone;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.phone.$$__AppSearch__Call, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Call implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Call call) {
        GenericDocument.Builder builder = new GenericDocument.Builder(call.getNamespace(), call.getId(), "com.google.android.appfunctions.schema.common.v1.phone.Call");
        String contactId = call.getContactId();
        if (contactId != null) {
            builder.setPropertyString("contactId", contactId);
        }
        Boolean boolIsVideoCall = call.isVideoCall();
        if (boolIsVideoCall != null) {
            builder.setPropertyBoolean("isVideoCall", boolIsVideoCall.booleanValue());
        }
        return builder.build();
    }
}
