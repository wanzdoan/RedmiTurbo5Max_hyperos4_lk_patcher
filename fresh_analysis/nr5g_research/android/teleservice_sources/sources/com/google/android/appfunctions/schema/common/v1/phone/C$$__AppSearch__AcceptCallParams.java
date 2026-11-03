package com.google.android.appfunctions.schema.common.v1.phone;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.phone.$$__AppSearch__AcceptCallParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__AcceptCallParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(AcceptCallParams acceptCallParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(acceptCallParams.getNamespace(), acceptCallParams.getId(), "com.google.android.appfunctions.schema.common.v1.phone.AcceptCallParams");
        Boolean bool = acceptCallParams.shouldRecord;
        if (bool != null) {
            builder.setPropertyBoolean("shouldRecord", bool.booleanValue());
        }
        Boolean boolIsSpeakerOn = acceptCallParams.isSpeakerOn();
        if (boolIsSpeakerOn != null) {
            builder.setPropertyBoolean("isSpeakerOn", boolIsSpeakerOn.booleanValue());
        }
        Boolean boolIsVideoCall = acceptCallParams.isVideoCall();
        if (boolIsVideoCall != null) {
            builder.setPropertyBoolean("isVideoCall", boolIsVideoCall.booleanValue());
        }
        return builder.build();
    }
}
