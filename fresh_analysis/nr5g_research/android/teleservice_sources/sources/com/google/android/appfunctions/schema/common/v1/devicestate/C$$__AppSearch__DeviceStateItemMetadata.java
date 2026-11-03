package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__DeviceStateItemMetadata, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DeviceStateItemMetadata implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateItemMetadata";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("key").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("purpose").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.DocumentPropertyConfig.Builder("name", C$$__AppSearch__LocalizedString.SCHEMA_NAME).setCardinality(2).setShouldIndexNestedProperties(false).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("sensitivity").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.BooleanPropertyConfig.Builder("writable").setCardinality(2).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("possibleValues").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("hintText").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("defaultValue").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(LocalizedString.class);
        return arrayList;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DeviceStateItemMetadata deviceStateItemMetadata) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(deviceStateItemMetadata.getNamespace(), deviceStateItemMetadata.getId(), SCHEMA_NAME);
        String key = deviceStateItemMetadata.getKey();
        if (key != null) {
            builder.setPropertyString("key", key);
        }
        String purpose = deviceStateItemMetadata.getPurpose();
        if (purpose != null) {
            builder.setPropertyString("purpose", purpose);
        }
        LocalizedString name = deviceStateItemMetadata.getName();
        if (name != null) {
            builder.setPropertyDocument("name", GenericDocument.fromDocumentClass(name));
        }
        String sensitivity = deviceStateItemMetadata.getSensitivity();
        if (sensitivity != null) {
            builder.setPropertyString("sensitivity", sensitivity);
        }
        Boolean writable = deviceStateItemMetadata.getWritable();
        if (writable != null) {
            builder.setPropertyBoolean("writable", writable.booleanValue());
        }
        String possibleValues = deviceStateItemMetadata.getPossibleValues();
        if (possibleValues != null) {
            builder.setPropertyString("possibleValues", possibleValues);
        }
        String hintText = deviceStateItemMetadata.getHintText();
        if (hintText != null) {
            builder.setPropertyString("hintText", hintText);
        }
        String defaultValue = deviceStateItemMetadata.getDefaultValue();
        if (defaultValue != null) {
            builder.setPropertyString("defaultValue", defaultValue);
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public DeviceStateItemMetadata m4681fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        String[] propertyStringArray = genericDocument.getPropertyStringArray("key");
        String str = null;
        String str2 = (propertyStringArray == null || propertyStringArray.length == 0) ? null : propertyStringArray[0];
        String[] propertyStringArray2 = genericDocument.getPropertyStringArray("purpose");
        String str3 = (propertyStringArray2 == null || propertyStringArray2.length == 0) ? null : propertyStringArray2[0];
        GenericDocument propertyDocument = genericDocument.getPropertyDocument("name");
        LocalizedString localizedString = propertyDocument != null ? (LocalizedString) propertyDocument.toDocumentClass(LocalizedString.class, documentClassMappingContext) : null;
        String[] propertyStringArray3 = genericDocument.getPropertyStringArray("sensitivity");
        String str4 = (propertyStringArray3 == null || propertyStringArray3.length == 0) ? null : propertyStringArray3[0];
        boolean[] propertyBooleanArray = genericDocument.getPropertyBooleanArray("writable");
        Boolean boolValueOf = (propertyBooleanArray == null || propertyBooleanArray.length == 0) ? null : Boolean.valueOf(propertyBooleanArray[0]);
        String[] propertyStringArray4 = genericDocument.getPropertyStringArray("possibleValues");
        String str5 = (propertyStringArray4 == null || propertyStringArray4.length == 0) ? null : propertyStringArray4[0];
        String[] propertyStringArray5 = genericDocument.getPropertyStringArray("hintText");
        String str6 = (propertyStringArray5 == null || propertyStringArray5.length == 0) ? null : propertyStringArray5[0];
        String[] propertyStringArray6 = genericDocument.getPropertyStringArray("defaultValue");
        if (propertyStringArray6 != null && propertyStringArray6.length != 0) {
            str = propertyStringArray6[0];
        }
        return new DeviceStateItemMetadata(namespace, id, str2, str3, localizedString, str4, boolValueOf, str5, str6, str);
    }
}
