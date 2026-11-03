package com.google.android.appfunctions.schema.common.v1.phone;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.phone.$$__AppSearch__CallRecord, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CallRecord implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CallRecord callRecord) {
        GenericDocument.Builder builder = new GenericDocument.Builder(callRecord.getNamespace(), callRecord.getId(), "com.google.android.appfunctions.schema.common.v1.phone.CallRecord");
        String contactId = callRecord.getContactId();
        if (contactId != null) {
            builder.setPropertyString("contactId", contactId);
        }
        String callType = callRecord.getCallType();
        if (callType != null) {
            builder.setPropertyString("callType", callType);
        }
        Boolean boolIsVideoCall = callRecord.isVideoCall();
        if (boolIsVideoCall != null) {
            builder.setPropertyBoolean("isVideoCall", boolIsVideoCall.booleanValue());
        }
        callRecord.getStartTime();
        Long durationMillis = callRecord.getDurationMillis();
        if (durationMillis != null) {
            builder.setPropertyLong("durationMillis", durationMillis.longValue());
        }
        String sipAddress = callRecord.getSipAddress();
        if (sipAddress != null) {
            builder.setPropertyString("sipAddress", sipAddress);
        }
        String phoneNumber = callRecord.getPhoneNumber();
        if (phoneNumber != null) {
            builder.setPropertyString("phoneNumber", phoneNumber);
        }
        return builder.build();
    }
}
