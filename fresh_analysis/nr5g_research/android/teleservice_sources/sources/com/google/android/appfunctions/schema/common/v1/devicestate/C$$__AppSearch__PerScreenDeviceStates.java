package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__PerScreenDeviceStates, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__PerScreenDeviceStates implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.PerScreenDeviceStates";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("description").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("intentUri").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.DocumentPropertyConfig.Builder("deviceStateItems", C$$__AppSearch__DeviceStateItem.SCHEMA_NAME).setCardinality(1).setShouldIndexNestedProperties(false).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(DeviceStateItem.class);
        return arrayList;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(PerScreenDeviceStates perScreenDeviceStates) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(perScreenDeviceStates.getNamespace(), perScreenDeviceStates.getId(), SCHEMA_NAME);
        String description = perScreenDeviceStates.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String intentUri = perScreenDeviceStates.getIntentUri();
        if (intentUri != null) {
            builder.setPropertyString("intentUri", intentUri);
        }
        List deviceStateItems = perScreenDeviceStates.getDeviceStateItems();
        if (deviceStateItems != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[deviceStateItems.size()];
            Iterator it = deviceStateItems.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((DeviceStateItem) it.next());
                i++;
            }
            builder.setPropertyDocument("deviceStateItems", genericDocumentArr);
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public PerScreenDeviceStates m4699fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
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
        GenericDocument[] propertyDocumentArray = genericDocument.getPropertyDocumentArray("deviceStateItems");
        List arrayList = Collections.EMPTY_LIST;
        if (propertyDocumentArray != null) {
            arrayList = new ArrayList(propertyDocumentArray.length);
            for (GenericDocument genericDocument2 : propertyDocumentArray) {
                arrayList.add((DeviceStateItem) genericDocument2.toDocumentClass(DeviceStateItem.class, documentClassMappingContext));
            }
        }
        return new PerScreenDeviceStates(namespace, id, str2, str3, arrayList);
    }
}
