package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__OffsetNumericDeviceStateItemByValueParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__OffsetNumericDeviceStateItemByValueParams implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.OffsetNumericDeviceStateItemByValueParams";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("key").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("itemizationKeys").setCardinality(1).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.DoublePropertyConfig.Builder("valueAdjustment").setCardinality(3).build()).addProperty(new AppSearchSchema.BooleanPropertyConfig.Builder("requestInitiatedWhileUnlocked").setCardinality(2).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        return Collections.EMPTY_LIST;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(OffsetNumericDeviceStateItemByValueParams offsetNumericDeviceStateItemByValueParams) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(offsetNumericDeviceStateItemByValueParams.getNamespace(), offsetNumericDeviceStateItemByValueParams.getId(), SCHEMA_NAME);
        String key = offsetNumericDeviceStateItemByValueParams.getKey();
        if (key != null) {
            builder.setPropertyString("key", key);
        }
        List itemizationKeys = offsetNumericDeviceStateItemByValueParams.getItemizationKeys();
        if (itemizationKeys != null) {
            builder.setPropertyString("itemizationKeys", (String[]) itemizationKeys.toArray(new String[0]));
        }
        builder.setPropertyDouble("valueAdjustment", offsetNumericDeviceStateItemByValueParams.getValueAdjustment());
        Boolean requestInitiatedWhileUnlocked = offsetNumericDeviceStateItemByValueParams.getRequestInitiatedWhileUnlocked();
        if (requestInitiatedWhileUnlocked != null) {
            builder.setPropertyBoolean("requestInitiatedWhileUnlocked", requestInitiatedWhileUnlocked.booleanValue());
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public OffsetNumericDeviceStateItemByValueParams m4698fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        String[] propertyStringArray = genericDocument.getPropertyStringArray("key");
        Boolean boolValueOf = null;
        String str = (propertyStringArray == null || propertyStringArray.length == 0) ? null : propertyStringArray[0];
        String[] propertyStringArray2 = genericDocument.getPropertyStringArray("itemizationKeys");
        List listAsList = Collections.EMPTY_LIST;
        if (propertyStringArray2 != null) {
            listAsList = Arrays.asList(propertyStringArray2);
        }
        double propertyDouble = genericDocument.getPropertyDouble("valueAdjustment");
        boolean[] propertyBooleanArray = genericDocument.getPropertyBooleanArray("requestInitiatedWhileUnlocked");
        if (propertyBooleanArray != null && propertyBooleanArray.length != 0) {
            boolValueOf = Boolean.valueOf(propertyBooleanArray[0]);
        }
        return new OffsetNumericDeviceStateItemByValueParams(namespace, id, str, listAsList, propertyDouble, boolValueOf);
    }
}
