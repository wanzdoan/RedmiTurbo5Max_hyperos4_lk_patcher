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

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.devicestate.$$__AppSearch__DeviceStateResponse, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DeviceStateResponse implements DocumentClassFactory {
    public static final String SCHEMA_NAME = "com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateResponse";

    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    public AppSearchSchema getSchema() throws AppSearchException {
        return new AppSearchSchema.Builder(SCHEMA_NAME).addProperty(new AppSearchSchema.DocumentPropertyConfig.Builder("perScreenDeviceStates", C$$__AppSearch__PerScreenDeviceStates.SCHEMA_NAME).setCardinality(1).setShouldIndexNestedProperties(false).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("deviceLocale").setCardinality(3).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).addProperty(new AppSearchSchema.StringPropertyConfig.Builder("globalHintText").setCardinality(2).setTokenizerType(0).setIndexingType(0).setJoinableValueType(0).build()).build();
    }

    public List getDependencyDocumentClasses() throws AppSearchException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(PerScreenDeviceStates.class);
        return arrayList;
    }

    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DeviceStateResponse deviceStateResponse) throws AppSearchException {
        GenericDocument.Builder builder = new GenericDocument.Builder(deviceStateResponse.getNamespace(), deviceStateResponse.getId(), SCHEMA_NAME);
        List perScreenDeviceStates = deviceStateResponse.getPerScreenDeviceStates();
        if (perScreenDeviceStates != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[perScreenDeviceStates.size()];
            Iterator it = perScreenDeviceStates.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((PerScreenDeviceStates) it.next());
                i++;
            }
            builder.setPropertyDocument("perScreenDeviceStates", genericDocumentArr);
        }
        String deviceLocale = deviceStateResponse.getDeviceLocale();
        if (deviceLocale != null) {
            builder.setPropertyString("deviceLocale", deviceLocale);
        }
        String globalHintText = deviceStateResponse.getGlobalHintText();
        if (globalHintText != null) {
            builder.setPropertyString("globalHintText", globalHintText);
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: fromGenericDocument, reason: merged with bridge method [inline-methods] */
    public DeviceStateResponse m4684fromGenericDocument(GenericDocument genericDocument, DocumentClassMappingContext documentClassMappingContext) throws AppSearchException {
        String namespace = genericDocument.getNamespace();
        String id = genericDocument.getId();
        GenericDocument[] propertyDocumentArray = genericDocument.getPropertyDocumentArray("perScreenDeviceStates");
        List arrayList = Collections.EMPTY_LIST;
        if (propertyDocumentArray != null) {
            arrayList = new ArrayList(propertyDocumentArray.length);
            for (GenericDocument genericDocument2 : propertyDocumentArray) {
                arrayList.add((PerScreenDeviceStates) genericDocument2.toDocumentClass(PerScreenDeviceStates.class, documentClassMappingContext));
            }
        }
        String[] propertyStringArray = genericDocument.getPropertyStringArray("deviceLocale");
        String str = null;
        String str2 = (propertyStringArray == null || propertyStringArray.length == 0) ? null : propertyStringArray[0];
        String[] propertyStringArray2 = genericDocument.getPropertyStringArray("globalHintText");
        if (propertyStringArray2 != null && propertyStringArray2.length != 0) {
            str = propertyStringArray2[0];
        }
        return new DeviceStateResponse(namespace, id, arrayList, str2, str);
    }
}
