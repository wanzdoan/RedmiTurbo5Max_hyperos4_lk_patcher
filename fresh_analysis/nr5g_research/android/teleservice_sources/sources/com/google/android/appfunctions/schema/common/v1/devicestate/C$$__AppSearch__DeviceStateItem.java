package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__DeviceStateItem, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DeviceStateItem implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateItem";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("key").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("purpose").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.DocumentPropertyConfig.Builder("name", C$$__AppSearch__LocalizedString.SCHEMA_NAME).setCardinality(2).setShouldIndexNestedProperties(false).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("jsonValue").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.LongPropertyConfig.Builder("lastUpdatedEpochMillis").setCardinality(2).setIndexingType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("hintText").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(LocalizedString.class);
        return arrayList;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DeviceStateItem deviceStateItem) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(deviceStateItem.getNamespace(), deviceStateItem.getId(), SCHEMA_NAME);
        String key = deviceStateItem.getKey();
        if (key != null) {
            builder.setPropertyString("key", key);
        }
        String purpose = deviceStateItem.getPurpose();
        if (purpose != null) {
            builder.setPropertyString("purpose", purpose);
        }
        LocalizedString name = deviceStateItem.getName();
        if (name != null) {
            builder.setPropertyDocument("name", GenericDocument.fromDocumentClass(name));
        }
        String jsonValue = deviceStateItem.getJsonValue();
        if (jsonValue != null) {
            builder.setPropertyString("jsonValue", jsonValue);
        }
        Long lastUpdatedEpochMillis = deviceStateItem.getLastUpdatedEpochMillis();
        if (lastUpdatedEpochMillis != null) {
            builder.setPropertyLong("lastUpdatedEpochMillis", lastUpdatedEpochMillis.longValue());
        }
        String hintText = deviceStateItem.getHintText();
        if (hintText != null) {
            builder.setPropertyString("hintText", hintText);
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public DeviceStateItem m4680fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        String[] propertyStringArray = genericDocument.getPropertyStringArray("key");
        String str = null;
        String str2 = (propertyStringArray == null || propertyStringArray.length == 0) ? null : propertyStringArray[0];
        String[] propertyStringArray2 = genericDocument.getPropertyStringArray("purpose");
        String str3 = (propertyStringArray2 == null || propertyStringArray2.length == 0) ? null : propertyStringArray2[0];
        GenericDocument propertyDocument = genericDocument.getPropertyDocument("name");
        LocalizedString localizedString = propertyDocument != null ? (LocalizedString) propertyDocument.toDocumentClass(LocalizedString.class, documentClassMappingContext) : null;
        String[] propertyStringArray3 = genericDocument.getPropertyStringArray("jsonValue");
        String str4 = (propertyStringArray3 == null || propertyStringArray3.length == 0) ? null : propertyStringArray3[0];
        long[] propertyLongArray = genericDocument.getPropertyLongArray("lastUpdatedEpochMillis");
        Long lValueOf = (propertyLongArray == null || propertyLongArray.length == 0) ? null : Long.valueOf(propertyLongArray[0]);
        String[] propertyStringArray4 = genericDocument.getPropertyStringArray("hintText");
        if (propertyStringArray4 != null && propertyStringArray4.length != 0) {
            str = propertyStringArray4[0];
        }
        return new DeviceStateItem(namespace, id, str2, str3, localizedString, str4, lValueOf, str);
    }
}
