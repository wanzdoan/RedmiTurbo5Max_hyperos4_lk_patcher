package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__DeviceStateItemResponse, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DeviceStateItemResponse implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateItemResponse";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.DocumentPropertyConfig.Builder("deviceStateItem", C$$__AppSearch__DeviceStateItem.SCHEMA_NAME).setCardinality(3).setShouldIndexNestedProperties(false).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(DeviceStateItem.class);
        return arrayList;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DeviceStateItemResponse deviceStateItemResponse) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(deviceStateItemResponse.getNamespace(), deviceStateItemResponse.getId(), SCHEMA_NAME);
        DeviceStateItem deviceStateItem = deviceStateItemResponse.getDeviceStateItem();
        if (deviceStateItem != null) {
            builder.setPropertyDocument("deviceStateItem", GenericDocument.fromDocumentClass(deviceStateItem));
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public DeviceStateItemResponse m4682fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        GenericDocument propertyDocument = genericDocument.getPropertyDocument("deviceStateItem");
        return new DeviceStateItemResponse(namespace, id, propertyDocument != null ? (DeviceStateItem) propertyDocument.toDocumentClass(DeviceStateItem.class, documentClassMappingContext) : null);
    }
}
