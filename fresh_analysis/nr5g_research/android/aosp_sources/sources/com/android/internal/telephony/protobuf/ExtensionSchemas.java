package com.android.internal.telephony.protobuf;

import com.android.internal.telephony.SomeArgs$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
abstract class ExtensionSchemas {
    private static final ExtensionSchema LITE_SCHEMA = new ExtensionSchemaLite();
    private static final ExtensionSchema FULL_SCHEMA = loadSchemaForFullRuntime();

    private static ExtensionSchema loadSchemaForFullRuntime() {
        if (Android.assumeLiteRuntime) {
            return null;
        }
        try {
            Class[] clsArr = new Class[0];
            return (ExtensionSchema) Class.forName("com.android.internal.telephony.protobuf.ExtensionSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
            return null;
        }
    }

    static ExtensionSchema lite() {
        return LITE_SCHEMA;
    }

    static ExtensionSchema full() {
        ExtensionSchema extensionSchema = FULL_SCHEMA;
        if (extensionSchema != null) {
            return extensionSchema;
        }
        SomeArgs$$ExternalSyntheticBUOutline0.m("Protobuf runtime is not correctly loaded.");
        return null;
    }
}
