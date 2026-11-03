package androidx.appsearch.safeparcel;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class GenericDocumentParcel extends AbstractSafeParcelable implements Parcelable {
    public static final Parcelable.Creator CREATOR = new GenericDocumentParcelCreator();
    private final long mCreationTimestampMillis;
    private Integer mHashCode;
    private final String mId;
    private final String mNamespace;
    private final List mParentTypes;
    private final List mProperties;
    private final Map mPropertyMap;
    private final String mSchemaType;
    private final int mScore;
    private final long mTtlMillis;

    GenericDocumentParcel(String str, String str2, String str3, long j, long j2, int i, List list, List list2) {
        this(str, str2, str3, j, j2, i, list, createPropertyMapFromPropertyArray(list), list2);
    }

    GenericDocumentParcel(String str, String str2, String str3, long j, long j2, int i, List list, Map map, List list2) {
        Objects.requireNonNull(str);
        this.mNamespace = str;
        Objects.requireNonNull(str2);
        this.mId = str2;
        Objects.requireNonNull(str3);
        this.mSchemaType = str3;
        this.mCreationTimestampMillis = j;
        this.mTtlMillis = j2;
        this.mScore = i;
        Objects.requireNonNull(list);
        this.mProperties = list;
        Objects.requireNonNull(map);
        this.mPropertyMap = map;
        this.mParentTypes = list2;
    }

    private static Map createPropertyMapFromPropertyArray(List list) {
        Objects.requireNonNull(list);
        ArrayMap arrayMap = new ArrayMap(list.size());
        for (int i = 0; i < list.size(); i++) {
            PropertyParcel propertyParcel = (PropertyParcel) list.get(i);
            arrayMap.put(propertyParcel.getPropertyName(), propertyParcel);
        }
        return arrayMap;
    }

    public String getId() {
        return this.mId;
    }

    public String getNamespace() {
        return this.mNamespace;
    }

    public String getSchemaType() {
        return this.mSchemaType;
    }

    public long getCreationTimestampMillis() {
        return this.mCreationTimestampMillis;
    }

    public long getTtlMillis() {
        return this.mTtlMillis;
    }

    public int getScore() {
        return this.mScore;
    }

    public Set getPropertyNames() {
        return this.mPropertyMap.keySet();
    }

    public List getProperties() {
        return this.mProperties;
    }

    public Map getPropertyMap() {
        return this.mPropertyMap;
    }

    public List getParentTypes() {
        return this.mParentTypes;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GenericDocumentParcel)) {
            return false;
        }
        GenericDocumentParcel genericDocumentParcel = (GenericDocumentParcel) obj;
        return this.mNamespace.equals(genericDocumentParcel.mNamespace) && this.mId.equals(genericDocumentParcel.mId) && this.mSchemaType.equals(genericDocumentParcel.mSchemaType) && this.mTtlMillis == genericDocumentParcel.mTtlMillis && this.mCreationTimestampMillis == genericDocumentParcel.mCreationTimestampMillis && this.mScore == genericDocumentParcel.mScore && Objects.equals(this.mProperties, genericDocumentParcel.mProperties) && Objects.equals(this.mPropertyMap, genericDocumentParcel.mPropertyMap) && Objects.equals(this.mParentTypes, genericDocumentParcel.mParentTypes);
    }

    public int hashCode() {
        if (this.mHashCode == null) {
            this.mHashCode = Integer.valueOf(Objects.hash(this.mNamespace, this.mId, this.mSchemaType, Long.valueOf(this.mTtlMillis), Integer.valueOf(this.mScore), Long.valueOf(this.mCreationTimestampMillis), Integer.valueOf(Objects.hashCode(this.mProperties)), Integer.valueOf(Objects.hashCode(this.mPropertyMap)), Integer.valueOf(Objects.hashCode(this.mParentTypes))));
        }
        return this.mHashCode.intValue();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        GenericDocumentParcelCreator.writeToParcel(this, parcel, i);
    }

    public final class Builder {
        private long mCreationTimestampMillis;
        private String mId;
        private String mNamespace;
        private List mParentTypes;
        private Map mPropertyMap;
        private String mSchemaType;
        private int mScore;
        private long mTtlMillis;

        public Builder(String str, String str2, String str3) {
            Objects.requireNonNull(str);
            this.mNamespace = str;
            Objects.requireNonNull(str2);
            this.mId = str2;
            Objects.requireNonNull(str3);
            this.mSchemaType = str3;
            this.mCreationTimestampMillis = -1L;
            this.mTtlMillis = 0L;
            this.mScore = 0;
            this.mPropertyMap = new ArrayMap();
        }

        public Builder setScore(int i) {
            this.mScore = i;
            return this;
        }

        public Builder setCreationTimestampMillis(long j) {
            this.mCreationTimestampMillis = j;
            return this;
        }

        public Builder setTtlMillis(long j) {
            if (j < 0) {
                Folme$$ExternalSyntheticBUOutline0.m("Document ttlMillis cannot be negative.");
                return null;
            }
            this.mTtlMillis = j;
            return this;
        }

        public Builder setParentTypes(List list) {
            if (list == null) {
                this.mParentTypes = null;
                return this;
            }
            this.mParentTypes = new ArrayList(list);
            return this;
        }

        public Builder putInPropertyMap(String str, String[] strArr) {
            putInPropertyMap(str, new PropertyParcel.Builder(str).setStringValues(strArr).build());
            return this;
        }

        public Builder putInPropertyMap(String str, boolean[] zArr) {
            putInPropertyMap(str, new PropertyParcel.Builder(str).setBooleanValues(zArr).build());
            return this;
        }

        public Builder putInPropertyMap(String str, double[] dArr) {
            putInPropertyMap(str, new PropertyParcel.Builder(str).setDoubleValues(dArr).build());
            return this;
        }

        public Builder putInPropertyMap(String str, long[] jArr) {
            putInPropertyMap(str, new PropertyParcel.Builder(str).setLongValues(jArr).build());
            return this;
        }

        public Builder putInPropertyMap(String str, byte[][] bArr) {
            putInPropertyMap(str, new PropertyParcel.Builder(str).setBytesValues(bArr).build());
            return this;
        }

        public Builder putInPropertyMap(String str, GenericDocumentParcel[] genericDocumentParcelArr) {
            putInPropertyMap(str, new PropertyParcel.Builder(str).setDocumentValues(genericDocumentParcelArr).build());
            return this;
        }

        public Builder putInPropertyMap(String str, PropertyParcel propertyParcel) {
            Objects.requireNonNull(propertyParcel);
            this.mPropertyMap.put(str, propertyParcel);
            return this;
        }

        public GenericDocumentParcel build() {
            if (this.mCreationTimestampMillis == -1) {
                this.mCreationTimestampMillis = System.currentTimeMillis();
            }
            return new GenericDocumentParcel(this.mNamespace, this.mId, this.mSchemaType, this.mCreationTimestampMillis, this.mTtlMillis, this.mScore, new ArrayList(this.mPropertyMap.values()), this.mParentTypes);
        }
    }
}
