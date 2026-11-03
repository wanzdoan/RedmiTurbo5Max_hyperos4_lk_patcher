package com.google.android.appfunctions.schema.common.v1.phone;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.phone.$$__AppSearch__MakeCallParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__MakeCallParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(MakeCallParams makeCallParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(makeCallParams.getNamespace(), makeCallParams.getId(), "com.google.android.appfunctions.schema.common.v1.phone.MakeCallParams");
        String type = makeCallParams.getType();
        if (type != null) {
            builder.setPropertyString("type", type);
        }
        String phoneNumber = makeCallParams.getPhoneNumber();
        if (phoneNumber != null) {
            builder.setPropertyString("phoneNumber", phoneNumber);
        }
        String contactId = makeCallParams.getContactId();
        if (contactId != null) {
            builder.setPropertyString("contactId", contactId);
        }
        Boolean bool = makeCallParams.shouldRecord;
        if (bool != null) {
            builder.setPropertyBoolean("shouldRecord", bool.booleanValue());
        }
        Boolean boolIsSpeakerOn = makeCallParams.isSpeakerOn();
        if (boolIsSpeakerOn != null) {
            builder.setPropertyBoolean("isSpeakerOn", boolIsSpeakerOn.booleanValue());
        }
        Boolean boolIsVideoCall = makeCallParams.isVideoCall();
        if (boolIsVideoCall != null) {
            builder.setPropertyBoolean("isVideoCall", boolIsVideoCall.booleanValue());
        }
        return builder.build();
    }
}
