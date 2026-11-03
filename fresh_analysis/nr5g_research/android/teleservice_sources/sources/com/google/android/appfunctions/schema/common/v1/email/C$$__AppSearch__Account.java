package com.google.android.appfunctions.schema.common.v1.email;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.email.$$__AppSearch__Account, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Account implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Account account) {
        GenericDocument.Builder builder = new GenericDocument.Builder(account.getNamespace(), account.getId(), "com.google.android.appfunctions.schema.common.v1.email.Account");
        String name = account.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        String emailAddress = account.getEmailAddress();
        if (emailAddress != null) {
            builder.setPropertyString("emailAddress", emailAddress);
        }
        return builder.build();
    }
}
