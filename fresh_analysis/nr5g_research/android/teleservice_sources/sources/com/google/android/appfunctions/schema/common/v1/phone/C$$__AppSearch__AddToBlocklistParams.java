package com.google.android.appfunctions.schema.common.v1.phone;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.phone.$$__AppSearch__AddToBlocklistParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__AddToBlocklistParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(AddToBlocklistParams addToBlocklistParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(addToBlocklistParams.getNamespace(), addToBlocklistParams.getId(), "com.google.android.appfunctions.schema.common.v1.phone.AddToBlocklistParams");
        String type = addToBlocklistParams.getType();
        if (type != null) {
            builder.setPropertyString("type", type);
        }
        String phoneNumber = addToBlocklistParams.getPhoneNumber();
        if (phoneNumber != null) {
            builder.setPropertyString("phoneNumber", phoneNumber);
        }
        String contactId = addToBlocklistParams.getContactId();
        if (contactId != null) {
            builder.setPropertyString("contactId", contactId);
        }
        return builder.build();
    }
}
