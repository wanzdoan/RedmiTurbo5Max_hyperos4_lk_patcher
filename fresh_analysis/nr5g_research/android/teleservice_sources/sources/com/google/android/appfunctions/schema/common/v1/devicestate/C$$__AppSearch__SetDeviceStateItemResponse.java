package com.google.android.appfunctions.schema.common.v1.devicestate;

import androidx.appsearch.app.AppSearchSchema;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.DocumentClassMappingContext;
import androidx.appsearch.app.GenericDocument;
import androidx.appsearch.exceptions.AppSearchException;
import java.util.Collections;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__SetDeviceStateItemResponse, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetDeviceStateItemResponse implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.SetDeviceStateItemResponse";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.BooleanPropertyConfig.Builder("isSuccessful").setCardinality(3).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("currentValue").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("failureReason").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        return Collections.EMPTY_LIST;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetDeviceStateItemResponse setDeviceStateItemResponse) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(setDeviceStateItemResponse.getNamespace(), setDeviceStateItemResponse.getId(), SCHEMA_NAME);
        builder.setPropertyBoolean("isSuccessful", setDeviceStateItemResponse.isSuccessful());
        String currentValue = setDeviceStateItemResponse.getCurrentValue();
        if (currentValue != null) {
            builder.setPropertyString("currentValue", currentValue);
        }
        String failureReason = setDeviceStateItemResponse.getFailureReason();
        if (failureReason != null) {
            builder.setPropertyString("failureReason", failureReason);
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public SetDeviceStateItemResponse m4702fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        boolean propertyBoolean = genericDocument.getPropertyBoolean("isSuccessful");
        String[] propertyStringArray = genericDocument.getPropertyStringArray("currentValue");
        String str = null;
        String str2 = (propertyStringArray == null || propertyStringArray.length == 0) ? null : propertyStringArray[0];
        String[] propertyStringArray2 = genericDocument.getPropertyStringArray("failureReason");
        if (propertyStringArray2 != null && propertyStringArray2.length != 0) {
            str = propertyStringArray2[0];
        }
        return new SetDeviceStateItemResponse(namespace, id, propertyBoolean, str2, str);
    }
}
