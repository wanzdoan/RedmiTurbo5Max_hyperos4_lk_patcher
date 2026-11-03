package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__PerScreenMetadata, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__PerScreenMetadata implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.PerScreenMetadata";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("description").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("intentUri").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("itemizationTypes").setCardinality(1).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.DocumentPropertyConfig.Builder("deviceStateItemsMetadata", C$$__AppSearch__DeviceStateItemMetadata.SCHEMA_NAME).setCardinality(1).setShouldIndexNestedProperties(false).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(DeviceStateItemMetadata.class);
        return arrayList;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(PerScreenMetadata perScreenMetadata) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(perScreenMetadata.getNamespace(), perScreenMetadata.getId(), SCHEMA_NAME);
        String description = perScreenMetadata.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String intentUri = perScreenMetadata.getIntentUri();
        if (intentUri != null) {
            builder.setPropertyString("intentUri", intentUri);
        }
        List itemizationTypes = perScreenMetadata.getItemizationTypes();
        int i = 0;
        if (itemizationTypes != null) {
            builder.setPropertyString("itemizationTypes", (String[]) itemizationTypes.toArray(new String[0]));
        }
        List deviceStateItemsMetadata = perScreenMetadata.getDeviceStateItemsMetadata();
        if (deviceStateItemsMetadata != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[deviceStateItemsMetadata.size()];
            Iterator it = deviceStateItemsMetadata.iterator();
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((DeviceStateItemMetadata) it.next());
                i++;
            }
            builder.setPropertyDocument("deviceStateItemsMetadata", genericDocumentArr);
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public PerScreenMetadata m4700fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        String[] propertyStringArray = genericDocument.getPropertyStringArray("description");
        String str = null;
        String str2 = (propertyStringArray == null || propertyStringArray.length == 0) ? null : propertyStringArray[0];
        String[] propertyStringArray2 = genericDocument.getPropertyStringArray("intentUri");
        if (propertyStringArray2 != null && propertyStringArray2.length != 0) {
            str = propertyStringArray2[0];
        }
        String str3 = str;
        String[] propertyStringArray3 = genericDocument.getPropertyStringArray("itemizationTypes");
        List arrayList = Collections.EMPTY_LIST;
        List listAsList = propertyStringArray3 != null ? Arrays.asList(propertyStringArray3) : arrayList;
        GenericDocument[] propertyDocumentArray = genericDocument.getPropertyDocumentArray("deviceStateItemsMetadata");
        if (propertyDocumentArray != null) {
            arrayList = new ArrayList(propertyDocumentArray.length);
            for (GenericDocument genericDocument2 : propertyDocumentArray) {
                arrayList.add((DeviceStateItemMetadata) genericDocument2.toDocumentClass(DeviceStateItemMetadata.class, documentClassMappingContext));
            }
        }
        return new PerScreenMetadata(namespace, id, str2, str3, listAsList, arrayList);
    }
}
