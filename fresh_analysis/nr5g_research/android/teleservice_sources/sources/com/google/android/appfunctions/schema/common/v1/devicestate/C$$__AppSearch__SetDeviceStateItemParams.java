package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import com.ot.pubsub.a.a;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__SetDeviceStateItemParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDeviceStateItemParams implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.SetDeviceStateItemParams";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("key").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("itemizationKeys").setCardinality(1).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder(a.p).setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.BooleanPropertyConfig.Builder("requestInitiatedWhileUnlocked").setCardinality(2).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        return Collections.EMPTY_LIST;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDeviceStateItemParams setDeviceStateItemParams) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDeviceStateItemParams.getNamespace(), setDeviceStateItemParams.getId(), SCHEMA_NAME);
        String key = setDeviceStateItemParams.getKey();
        if (key != null) {
            builder.setPropertyString("key", key);
        }
        List itemizationKeys = setDeviceStateItemParams.getItemizationKeys();
        if (itemizationKeys != null) {
            builder.setPropertyString("itemizationKeys", (String[]) itemizationKeys.toArray(new String[0]));
        }
        String value = setDeviceStateItemParams.getValue();
        if (value != null) {
            builder.setPropertyString(a.p, value);
        }
        Boolean requestInitiatedWhileUnlocked = setDeviceStateItemParams.getRequestInitiatedWhileUnlocked();
        if (requestInitiatedWhileUnlocked != null) {
            builder.setPropertyBoolean("requestInitiatedWhileUnlocked", requestInitiatedWhileUnlocked.booleanValue());
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public SetDeviceStateItemParams m4701fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
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
        String[] propertyStringArray3 = genericDocument.getPropertyStringArray(a.p);
        String str2 = (propertyStringArray3 == null || propertyStringArray3.length == 0) ? null : propertyStringArray3[0];
        boolean[] propertyBooleanArray = genericDocument.getPropertyBooleanArray("requestInitiatedWhileUnlocked");
        if (propertyBooleanArray != null && propertyBooleanArray.length != 0) {
            boolValueOf = Boolean.valueOf(propertyBooleanArray[0]);
        }
        return new SetDeviceStateItemParams(namespace, id, str, listAsList, str2, boolValueOf);
    }
}
