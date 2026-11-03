package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.Collections;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__GetUncategorizedDeviceStateParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__GetUncategorizedDeviceStateParams implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.GetUncategorizedDeviceStateParams";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.BooleanPropertyConfig.Builder("requestInitiatedWhileUnlocked").setCardinality(2).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        return Collections.EMPTY_LIST;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(GetUncategorizedDeviceStateParams getUncategorizedDeviceStateParams) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(getUncategorizedDeviceStateParams.getNamespace(), getUncategorizedDeviceStateParams.getId(), SCHEMA_NAME);
        Boolean requestInitiatedWhileUnlocked = getUncategorizedDeviceStateParams.getRequestInitiatedWhileUnlocked();
        if (requestInitiatedWhileUnlocked != null) {
            builder.setPropertyBoolean("requestInitiatedWhileUnlocked", requestInitiatedWhileUnlocked.booleanValue());
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public GetUncategorizedDeviceStateParams m4693fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        boolean[] propertyBooleanArray = genericDocument.getPropertyBooleanArray("requestInitiatedWhileUnlocked");
        return new GetUncategorizedDeviceStateParams(namespace, id, (propertyBooleanArray == null || propertyBooleanArray.length == 0) ? null : Boolean.valueOf(propertyBooleanArray[0]));
    }
}
