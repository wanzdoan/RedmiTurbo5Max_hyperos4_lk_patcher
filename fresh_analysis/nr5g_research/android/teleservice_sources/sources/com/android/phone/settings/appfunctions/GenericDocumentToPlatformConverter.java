package com.android.phone.settings.appfunctions;

import android.app.appsearch.GenericDocument;
import androidx.appsearch.app.EmbeddingVector;
import java.util.Objects;
import miuix.animation.motion.MergeMotion$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class GenericDocumentToPlatformConverter {
    public static GenericDocument toPlatformGenericDocument(androidx.appsearch.app.GenericDocument genericDocument) {
        Objects.requireNonNull(genericDocument, "jetpackDocument cannot be null");
        GenericDocument.Builder builder = new GenericDocument.Builder(genericDocument.getNamespace(), genericDocument.getId(), genericDocument.getSchemaType());
        builder.setScore(genericDocument.getScore()).setTtlMillis(genericDocument.getTtlMillis()).setCreationTimestampMillis(genericDocument.getCreationTimestampMillis());
        for (String str : genericDocument.getPropertyNames()) {
            Object property = genericDocument.getProperty(str);
            if (property != null) {
                if (property instanceof String[]) {
                    builder.setPropertyString(str, (String[]) property);
                } else if (property instanceof long[]) {
                    builder.setPropertyLong(str, (long[]) property);
                } else if (property instanceof double[]) {
                    builder.setPropertyDouble(str, (double[]) property);
                } else if (property instanceof boolean[]) {
                    builder.setPropertyBoolean(str, (boolean[]) property);
                } else if (property instanceof byte[][]) {
                    builder.setPropertyBytes(str, (byte[][]) property);
                } else if (property instanceof androidx.appsearch.app.GenericDocument[]) {
                    androidx.appsearch.app.GenericDocument[] genericDocumentArr = (androidx.appsearch.app.GenericDocument[]) property;
                    GenericDocument[] genericDocumentArr2 = new GenericDocument[genericDocumentArr.length];
                    for (int i = 0; i < genericDocumentArr.length; i++) {
                        genericDocumentArr2[i] = toPlatformGenericDocument(genericDocumentArr[i]);
                    }
                    builder.setPropertyDocument(str, genericDocumentArr2);
                } else {
                    if (property instanceof EmbeddingVector[]) {
                        MergeMotion$$ExternalSyntheticBUOutline0.m("SCHEMA_EMBEDDING_PROPERTY_CONFIG is not available on this AppSearch implementation.");
                        return null;
                    }
                    throw new IllegalStateException(String.format("Property \"%s\" has unsupported value type %s", str, property.getClass().toString()));
                }
            }
        }
        return builder.build();
    }
}
