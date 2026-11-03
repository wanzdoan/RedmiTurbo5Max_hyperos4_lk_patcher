package androidx.appsearch.safeparcel;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class GenericDocumentParcelCreator implements Parcelable.Creator {
    private static GenericDocumentParcel createGenericDocumentParcelFromBundle(Bundle bundle) {
        String string = bundle.getString("namespace");
        String string2 = bundle.getString("id");
        String string3 = bundle.getString("schemaType");
        if (string == null || string2 == null || string3 == null) {
            Folme$$ExternalSyntheticBUOutline0.m("GenericDocumentParcel bundle doesn't have namespace, id, or schemaType.");
            return null;
        }
        GenericDocumentParcel.Builder builder = new GenericDocumentParcel.Builder(string, string2, string3);
        ArrayList<String> stringArrayList = bundle.getStringArrayList("parentTypes");
        if (stringArrayList != null) {
            builder.setParentTypes(stringArrayList);
        }
        builder.setScore(bundle.getInt("score"));
        builder.setCreationTimestampMillis(bundle.getLong("creationTimestampMillis"));
        builder.setTtlMillis(bundle.getLong("ttlMillis"));
        Bundle bundle2 = bundle.getBundle("properties");
        if (bundle2 != null) {
            for (String str : bundle2.keySet()) {
                builder.putInPropertyMap(str, (PropertyParcel) bundle2.getParcelable(str));
            }
        }
        return builder.build();
    }

    private static Bundle createBundleFromGenericDocumentParcel(GenericDocumentParcel genericDocumentParcel) {
        Bundle bundle = new Bundle();
        bundle.putString("namespace", genericDocumentParcel.getNamespace());
        bundle.putString("id", genericDocumentParcel.getId());
        bundle.putString("schemaType", genericDocumentParcel.getSchemaType());
        bundle.putStringArrayList("parentTypes", (ArrayList) genericDocumentParcel.getParentTypes());
        bundle.putInt("score", genericDocumentParcel.getScore());
        bundle.putLong("creationTimestampMillis", genericDocumentParcel.getCreationTimestampMillis());
        bundle.putLong("ttlMillis", genericDocumentParcel.getTtlMillis());
        Bundle bundle2 = new Bundle();
        List properties = genericDocumentParcel.getProperties();
        for (int i = 0; i < properties.size(); i++) {
            PropertyParcel propertyParcel = (PropertyParcel) properties.get(i);
            bundle2.putParcelable(propertyParcel.getPropertyName(), propertyParcel);
        }
        bundle.putBundle("properties", bundle2);
        return bundle;
    }

    @Override // android.os.Parcelable.Creator
    public GenericDocumentParcel createFromParcel(Parcel parcel) {
        return createGenericDocumentParcelFromBundle(parcel.readBundle(getClass().getClassLoader()));
    }

    @Override // android.os.Parcelable.Creator
    public GenericDocumentParcel[] newArray(int i) {
        return new GenericDocumentParcel[i];
    }

    public static void writeToParcel(GenericDocumentParcel genericDocumentParcel, Parcel parcel, int i) {
        parcel.writeBundle(createBundleFromGenericDocumentParcel(genericDocumentParcel));
    }
}
