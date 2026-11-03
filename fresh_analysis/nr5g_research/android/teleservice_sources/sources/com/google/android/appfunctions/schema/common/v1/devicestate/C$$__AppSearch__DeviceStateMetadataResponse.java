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

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__DeviceStateMetadataResponse, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DeviceStateMetadataResponse implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateMetadataResponse";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.DocumentPropertyConfig.Builder("perScreenMetadata", C$$__AppSearch__PerScreenMetadata.SCHEMA_NAME).setCardinality(1).setShouldIndexNestedProperties(false).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("deviceLocale").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("globalHintText").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.DocumentPropertyConfig.Builder("itemizationTypes", C$$__AppSearch__ItemizationType.SCHEMA_NAME).setCardinality(1).setShouldIndexNestedProperties(false).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(PerScreenMetadata.class);
        arrayList.add(ItemizationType.class);
        return arrayList;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DeviceStateMetadataResponse deviceStateMetadataResponse) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(deviceStateMetadataResponse.getNamespace(), deviceStateMetadataResponse.getId(), SCHEMA_NAME);
        List perScreenMetadata = deviceStateMetadataResponse.getPerScreenMetadata();
        int i = 0;
        if (perScreenMetadata != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[perScreenMetadata.size()];
            Iterator it = perScreenMetadata.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                genericDocumentArr[i2] = GenericDocument.fromDocumentClass((PerScreenMetadata) it.next());
                i2++;
            }
            builder.setPropertyDocument("perScreenMetadata", genericDocumentArr);
        }
        String deviceLocale = deviceStateMetadataResponse.getDeviceLocale();
        if (deviceLocale != null) {
            builder.setPropertyString("deviceLocale", deviceLocale);
        }
        String globalHintText = deviceStateMetadataResponse.getGlobalHintText();
        if (globalHintText != null) {
            builder.setPropertyString("globalHintText", globalHintText);
        }
        List itemizationTypes = deviceStateMetadataResponse.getItemizationTypes();
        if (itemizationTypes != null) {
            GenericDocument[] genericDocumentArr2 = new GenericDocument[itemizationTypes.size()];
            Iterator it2 = itemizationTypes.iterator();
            while (it2.hasNext()) {
                genericDocumentArr2[i] = GenericDocument.fromDocumentClass((ItemizationType) it2.next());
                i++;
            }
            builder.setPropertyDocument("itemizationTypes", genericDocumentArr2);
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public DeviceStateMetadataResponse m4683fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        GenericDocument[] propertyDocumentArray = genericDocument.getPropertyDocumentArray("perScreenMetadata");
        List arrayList = Collections.EMPTY_LIST;
        if (propertyDocumentArray != null) {
            arrayList = new ArrayList(propertyDocumentArray.length);
            for (GenericDocument genericDocument2 : propertyDocumentArray) {
                arrayList.add((PerScreenMetadata) genericDocument2.toDocumentClass(PerScreenMetadata.class, documentClassMappingContext));
            }
        }
        String[] propertyStringArray = genericDocument.getPropertyStringArray("deviceLocale");
        String str = null;
        String str2 = (propertyStringArray == null || propertyStringArray.length == 0) ? null : propertyStringArray[0];
        String[] propertyStringArray2 = genericDocument.getPropertyStringArray("globalHintText");
        if (propertyStringArray2 != null && propertyStringArray2.length != 0) {
            str = propertyStringArray2[0];
        }
        String str3 = str;
        GenericDocument[] propertyDocumentArray2 = genericDocument.getPropertyDocumentArray("itemizationTypes");
        List arrayList2 = Collections.EMPTY_LIST;
        if (propertyDocumentArray2 != null) {
            arrayList2 = new ArrayList(propertyDocumentArray2.length);
            for (GenericDocument genericDocument3 : propertyDocumentArray2) {
                arrayList2.add((ItemizationType) genericDocument3.toDocumentClass(ItemizationType.class, documentClassMappingContext));
            }
        }
        return new DeviceStateMetadataResponse(namespace, id, arrayList, str2, str3, arrayList2);
    }
}
