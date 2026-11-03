package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.Collections;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__GetStorageDeviceStateParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__GetStorageDeviceStateParams implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.GetStorageDeviceStateParams";

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
    public GenericDocument toGenericDocument(GetStorageDeviceStateParams getStorageDeviceStateParams) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(getStorageDeviceStateParams.getNamespace(), getStorageDeviceStateParams.getId(), SCHEMA_NAME);
        Boolean requestInitiatedWhileUnlocked = getStorageDeviceStateParams.getRequestInitiatedWhileUnlocked();
        if (requestInitiatedWhileUnlocked != null) {
            builder.setPropertyBoolean("requestInitiatedWhileUnlocked", requestInitiatedWhileUnlocked.booleanValue());
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public GetStorageDeviceStateParams m4692fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        boolean[] propertyBooleanArray = genericDocument.getPropertyBooleanArray("requestInitiatedWhileUnlocked");
        return new GetStorageDeviceStateParams(namespace, id, (propertyBooleanArray == null || propertyBooleanArray.length == 0) ? null : Boolean.valueOf(propertyBooleanArray[0]));
    }
}
