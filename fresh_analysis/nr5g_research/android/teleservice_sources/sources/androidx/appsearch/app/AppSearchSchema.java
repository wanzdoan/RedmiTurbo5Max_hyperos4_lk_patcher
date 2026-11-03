package androidx.appsearch.app;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.appsearch.exceptions.IllegalSchemaException;
import androidx.appsearch.safeparcel.AbstractSafeParcelable;
import androidx.appsearch.safeparcel.PropertyConfigParcel;
import androidx.appsearch.safeparcel.stub.AbstractCreator;
import androidx.appsearch.util.IndentingStringBuilder;
import androidx.collection.ArraySet;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class AppSearchSchema extends AbstractSafeParcelable {
    public static final Parcelable.Creator CREATOR = new AbstractCreator() { // from class: androidx.appsearch.safeparcel.stub.StubCreators$AppSearchSchemaCreator
        @Override // androidx.appsearch.safeparcel.stub.AbstractCreator, android.os.Parcelable.Creator
        public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return super.createFromParcel(parcel);
        }

        @Override // androidx.appsearch.safeparcel.stub.AbstractCreator, android.os.Parcelable.Creator
        public /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return super.newArray(i);
        }
    };
    private final String mDescription;
    private final List mParentTypes;
    final List mPropertyConfigParcels;
    private final String mSchemaType;

    AppSearchSchema(String str, List list, List list2, String str2) {
        this.mSchemaType = (String) Preconditions.checkNotNull(str);
        this.mPropertyConfigParcels = (List) Preconditions.checkNotNull(list);
        this.mParentTypes = (List) Preconditions.checkNotNull(list2);
        this.mDescription = (String) Preconditions.checkNotNull(str2);
    }

    public String toString() {
        IndentingStringBuilder indentingStringBuilder = new IndentingStringBuilder();
        appendAppSearchSchemaString(indentingStringBuilder);
        return indentingStringBuilder.toString();
    }

    private void appendAppSearchSchemaString(IndentingStringBuilder indentingStringBuilder) {
        Preconditions.checkNotNull(indentingStringBuilder);
        indentingStringBuilder.append("{\n");
        indentingStringBuilder.increaseIndentLevel();
        indentingStringBuilder.append("schemaType: \"").append(getSchemaType()).append("\",\n");
        indentingStringBuilder.append("description: \"").append(getDescription()).append("\",\n");
        indentingStringBuilder.append("properties: [\n");
        PropertyConfig[] propertyConfigArr = (PropertyConfig[]) getProperties().toArray(new PropertyConfig[0]);
        Arrays.sort(propertyConfigArr, new Comparator() { // from class: androidx.appsearch.app.AppSearchSchema$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ((AppSearchSchema.PropertyConfig) obj).getName().compareTo(((AppSearchSchema.PropertyConfig) obj2).getName());
            }
        });
        for (int i = 0; i < propertyConfigArr.length; i++) {
            PropertyConfig propertyConfig = propertyConfigArr[i];
            indentingStringBuilder.increaseIndentLevel();
            propertyConfig.appendPropertyConfigString(indentingStringBuilder);
            if (i != propertyConfigArr.length - 1) {
                indentingStringBuilder.append(",\n");
            }
            indentingStringBuilder.decreaseIndentLevel();
        }
        indentingStringBuilder.append("\n");
        indentingStringBuilder.append("]\n");
        indentingStringBuilder.decreaseIndentLevel();
        indentingStringBuilder.append("}");
    }

    public String getSchemaType() {
        return this.mSchemaType;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public List getProperties() {
        if (this.mPropertyConfigParcels.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList(this.mPropertyConfigParcels.size());
        for (int i = 0; i < this.mPropertyConfigParcels.size(); i++) {
            arrayList.add(PropertyConfig.fromParcel((PropertyConfigParcel) this.mPropertyConfigParcels.get(i)));
        }
        return arrayList;
    }

    public List getParentTypes() {
        return Collections.unmodifiableList(this.mParentTypes);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AppSearchSchema)) {
            return false;
        }
        AppSearchSchema appSearchSchema = (AppSearchSchema) obj;
        if (getSchemaType().equals(appSearchSchema.getSchemaType()) && getDescription().equals(appSearchSchema.getDescription()) && getParentTypes().equals(appSearchSchema.getParentTypes())) {
            return getProperties().equals(appSearchSchema.getProperties());
        }
        return false;
    }

    public int hashCode() {
        return ObjectsCompat.hash(getSchemaType(), getProperties(), getParentTypes(), getDescription());
    }

    public final class Builder {
        private String mSchemaType;
        private String mDescription = "";
        private ArrayList mPropertyConfigParcels = new ArrayList();
        private LinkedHashSet mParentTypes = new LinkedHashSet();
        private final Set mPropertyNames = new ArraySet();
        private boolean mBuilt = false;

        public Builder(String str) {
            this.mSchemaType = (String) Preconditions.checkNotNull(str);
        }

        public Builder addProperty(PropertyConfig propertyConfig) {
            Preconditions.checkNotNull(propertyConfig);
            resetIfBuilt();
            String name = propertyConfig.getName();
            if (!this.mPropertyNames.add(name)) {
                throw new IllegalSchemaException("Property defined more than once: " + name);
            }
            this.mPropertyConfigParcels.add(propertyConfig.mPropertyConfigParcel);
            return this;
        }

        public AppSearchSchema build() {
            this.mBuilt = true;
            return new AppSearchSchema(this.mSchemaType, this.mPropertyConfigParcels, new ArrayList(this.mParentTypes), this.mDescription);
        }

        private void resetIfBuilt() {
            if (this.mBuilt) {
                this.mPropertyConfigParcels = new ArrayList(this.mPropertyConfigParcels);
                this.mParentTypes = new LinkedHashSet(this.mParentTypes);
                this.mBuilt = false;
            }
        }
    }

    public abstract class PropertyConfig {
        final PropertyConfigParcel mPropertyConfigParcel;

        PropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            this.mPropertyConfigParcel = (PropertyConfigParcel) Preconditions.checkNotNull(propertyConfigParcel);
        }

        public String toString() {
            IndentingStringBuilder indentingStringBuilder = new IndentingStringBuilder();
            appendPropertyConfigString(indentingStringBuilder);
            return indentingStringBuilder.toString();
        }

        void appendPropertyConfigString(IndentingStringBuilder indentingStringBuilder) {
            Preconditions.checkNotNull(indentingStringBuilder);
            indentingStringBuilder.append("{\n");
            indentingStringBuilder.increaseIndentLevel();
            indentingStringBuilder.append("name: \"").append(getName()).append("\",\n");
            indentingStringBuilder.append("description: \"").append(getDescription()).append("\",\n");
            if (this instanceof StringPropertyConfig) {
                ((StringPropertyConfig) this).appendStringPropertyConfigFields(indentingStringBuilder);
            } else if (this instanceof DocumentPropertyConfig) {
                ((DocumentPropertyConfig) this).appendDocumentPropertyConfigFields(indentingStringBuilder);
            } else if (this instanceof LongPropertyConfig) {
                ((LongPropertyConfig) this).appendLongPropertyConfigFields(indentingStringBuilder);
            }
            int cardinality = getCardinality();
            if (cardinality == 1) {
                indentingStringBuilder.append("cardinality: CARDINALITY_REPEATED,\n");
            } else if (cardinality == 2) {
                indentingStringBuilder.append("cardinality: CARDINALITY_OPTIONAL,\n");
            } else if (cardinality == 3) {
                indentingStringBuilder.append("cardinality: CARDINALITY_REQUIRED,\n");
            } else {
                indentingStringBuilder.append("cardinality: CARDINALITY_UNKNOWN,\n");
            }
            switch (getDataType()) {
                case 1:
                    indentingStringBuilder.append("dataType: DATA_TYPE_STRING,\n");
                    break;
                case 2:
                    indentingStringBuilder.append("dataType: DATA_TYPE_LONG,\n");
                    break;
                case 3:
                    indentingStringBuilder.append("dataType: DATA_TYPE_DOUBLE,\n");
                    break;
                case 4:
                    indentingStringBuilder.append("dataType: DATA_TYPE_BOOLEAN,\n");
                    break;
                case 5:
                    indentingStringBuilder.append("dataType: DATA_TYPE_BYTES,\n");
                    break;
                case 6:
                    indentingStringBuilder.append("dataType: DATA_TYPE_DOCUMENT,\n");
                    break;
                case 7:
                    indentingStringBuilder.append("dataType: DATA_TYPE_EMBEDDING,\n");
                    break;
                case 8:
                    indentingStringBuilder.append("dataType: DATA_TYPE_BLOB_HANDLE,\n");
                    break;
                default:
                    indentingStringBuilder.append("dataType: DATA_TYPE_UNKNOWN,\n");
                    break;
            }
            indentingStringBuilder.decreaseIndentLevel();
            indentingStringBuilder.append("}");
        }

        public String getName() {
            return this.mPropertyConfigParcel.getName();
        }

        public String getDescription() {
            return this.mPropertyConfigParcel.getDescription();
        }

        public int getDataType() {
            return this.mPropertyConfigParcel.getDataType();
        }

        public int getCardinality() {
            return this.mPropertyConfigParcel.getCardinality();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof PropertyConfig) {
                return ObjectsCompat.equals(this.mPropertyConfigParcel, ((PropertyConfig) obj).mPropertyConfigParcel);
            }
            return false;
        }

        public int hashCode() {
            return this.mPropertyConfigParcel.hashCode();
        }

        public static PropertyConfig fromParcel(PropertyConfigParcel propertyConfigParcel) {
            Preconditions.checkNotNull(propertyConfigParcel);
            switch (propertyConfigParcel.getDataType()) {
                case 1:
                    return new StringPropertyConfig(propertyConfigParcel);
                case 2:
                    return new LongPropertyConfig(propertyConfigParcel);
                case 3:
                    return new DoublePropertyConfig(propertyConfigParcel);
                case 4:
                    return new BooleanPropertyConfig(propertyConfigParcel);
                case 5:
                    return new BytesPropertyConfig(propertyConfigParcel);
                case 6:
                    return new DocumentPropertyConfig(propertyConfigParcel);
                case 7:
                    return new EmbeddingPropertyConfig(propertyConfigParcel);
                case 8:
                    return new BlobHandlePropertyConfig(propertyConfigParcel);
                default:
                    throw new IllegalArgumentException("Unsupported property bundle of type " + propertyConfigParcel.getDataType() + "; contents: " + propertyConfigParcel);
            }
        }
    }

    public final class StringPropertyConfig extends PropertyConfig {
        StringPropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            super(propertyConfigParcel);
        }

        public int getIndexingType() {
            PropertyConfigParcel.StringIndexingConfigParcel stringIndexingConfigParcel = this.mPropertyConfigParcel.getStringIndexingConfigParcel();
            if (stringIndexingConfigParcel == null) {
                return 0;
            }
            return stringIndexingConfigParcel.getIndexingType();
        }

        public int getTokenizerType() {
            PropertyConfigParcel.StringIndexingConfigParcel stringIndexingConfigParcel = this.mPropertyConfigParcel.getStringIndexingConfigParcel();
            if (stringIndexingConfigParcel == null) {
                return 0;
            }
            return stringIndexingConfigParcel.getTokenizerType();
        }

        public int getJoinableValueType() {
            PropertyConfigParcel.JoinableConfigParcel joinableConfigParcel = this.mPropertyConfigParcel.getJoinableConfigParcel();
            if (joinableConfigParcel == null) {
                return 0;
            }
            return joinableConfigParcel.getJoinableValueType();
        }

        public int getDeletePropagationType() {
            PropertyConfigParcel.JoinableConfigParcel joinableConfigParcel = this.mPropertyConfigParcel.getJoinableConfigParcel();
            if (joinableConfigParcel == null) {
                return 0;
            }
            return joinableConfigParcel.getDeletePropagationType();
        }

        public final class Builder {
            private final String mPropertyName;
            private String mDescription = "";
            private int mCardinality = 2;
            private int mIndexingType = 0;
            private int mTokenizerType = 0;
            private int mJoinableValueType = 0;
            private int mDeletePropagationType = 0;

            public Builder(String str) {
                this.mPropertyName = (String) Preconditions.checkNotNull(str);
            }

            public Builder setCardinality(int i) {
                Preconditions.checkArgumentInRange(i, 1, 3, "cardinality");
                this.mCardinality = i;
                return this;
            }

            public Builder setIndexingType(int i) {
                Preconditions.checkArgumentInRange(i, 0, 2, "indexingType");
                this.mIndexingType = i;
                return this;
            }

            public Builder setTokenizerType(int i) {
                Preconditions.checkArgumentInRange(i, 0, 3, "tokenizerType");
                this.mTokenizerType = i;
                return this;
            }

            public Builder setJoinableValueType(int i) {
                Preconditions.checkArgumentInRange(i, 0, 1, "joinableValueType");
                this.mJoinableValueType = i;
                return this;
            }

            public StringPropertyConfig build() {
                int i = this.mTokenizerType;
                int i2 = this.mIndexingType;
                if (i == 0) {
                    Preconditions.checkState(i2 == 0, "Cannot set TOKENIZER_TYPE_NONE with an indexing type other than INDEXING_TYPE_NONE.");
                } else {
                    Preconditions.checkState(i2 != 0, "Cannot set TOKENIZER_TYPE_PLAIN with INDEXING_TYPE_NONE.");
                }
                if (this.mDeletePropagationType != 0) {
                    Preconditions.checkState(this.mJoinableValueType == 1, "Cannot set delete propagation without setting JOINABLE_VALUE_TYPE_QUALIFIED_ID.");
                }
                return new StringPropertyConfig(PropertyConfigParcel.createForString(this.mPropertyName, this.mDescription, this.mCardinality, new PropertyConfigParcel.StringIndexingConfigParcel(this.mIndexingType, this.mTokenizerType), new PropertyConfigParcel.JoinableConfigParcel(this.mJoinableValueType, this.mDeletePropagationType)));
            }
        }

        void appendStringPropertyConfigFields(IndentingStringBuilder indentingStringBuilder) {
            int indexingType = getIndexingType();
            if (indexingType == 0) {
                indentingStringBuilder.append("indexingType: INDEXING_TYPE_NONE,\n");
            } else if (indexingType == 1) {
                indentingStringBuilder.append("indexingType: INDEXING_TYPE_EXACT_TERMS,\n");
            } else if (indexingType == 2) {
                indentingStringBuilder.append("indexingType: INDEXING_TYPE_PREFIXES,\n");
            } else {
                indentingStringBuilder.append("indexingType: INDEXING_TYPE_UNKNOWN,\n");
            }
            int tokenizerType = getTokenizerType();
            if (tokenizerType == 0) {
                indentingStringBuilder.append("tokenizerType: TOKENIZER_TYPE_NONE,\n");
            } else if (tokenizerType == 1) {
                indentingStringBuilder.append("tokenizerType: TOKENIZER_TYPE_PLAIN,\n");
            } else if (tokenizerType == 2) {
                indentingStringBuilder.append("tokenizerType: TOKENIZER_TYPE_VERBATIM,\n");
            } else if (tokenizerType == 3) {
                indentingStringBuilder.append("tokenizerType: TOKENIZER_TYPE_RFC822,\n");
            } else {
                indentingStringBuilder.append("tokenizerType: TOKENIZER_TYPE_UNKNOWN,\n");
            }
            int joinableValueType = getJoinableValueType();
            if (joinableValueType == 0) {
                indentingStringBuilder.append("joinableValueType: JOINABLE_VALUE_TYPE_NONE,\n");
            } else if (joinableValueType == 1) {
                indentingStringBuilder.append("joinableValueType: JOINABLE_VALUE_TYPE_QUALIFIED_ID,\n");
            } else {
                indentingStringBuilder.append("joinableValueType: JOINABLE_VALUE_TYPE_UNKNOWN,\n");
            }
            int deletePropagationType = getDeletePropagationType();
            if (deletePropagationType == 0) {
                indentingStringBuilder.append("deletePropagationType: DELETE_PROPAGATION_TYPE_NONE,\n");
            } else if (deletePropagationType == 1) {
                indentingStringBuilder.append("deletePropagationType: DELETE_PROPAGATION_TYPE_PROPAGATE_FROM,\n");
            } else {
                indentingStringBuilder.append("deletePropagationType: DELETE_PROPAGATION_TYPE_UNKNOWN,\n");
            }
        }
    }

    public final class LongPropertyConfig extends PropertyConfig {
        LongPropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            super(propertyConfigParcel);
        }

        public int getIndexingType() {
            PropertyConfigParcel.IntegerIndexingConfigParcel integerIndexingConfigParcel = this.mPropertyConfigParcel.getIntegerIndexingConfigParcel();
            if (integerIndexingConfigParcel == null) {
                return 0;
            }
            return integerIndexingConfigParcel.getIndexingType();
        }

        public final class Builder {
            private final String mPropertyName;
            private String mDescription = "";
            private int mCardinality = 2;
            private int mIndexingType = 0;
            private boolean mScoringEnabled = false;

            public Builder(String str) {
                this.mPropertyName = (String) Preconditions.checkNotNull(str);
            }

            public Builder setCardinality(int i) {
                Preconditions.checkArgumentInRange(i, 1, 3, "cardinality");
                this.mCardinality = i;
                return this;
            }

            public Builder setIndexingType(int i) {
                Preconditions.checkArgumentInRange(i, 0, 1, "indexingType");
                this.mIndexingType = i;
                return this;
            }

            public LongPropertyConfig build() {
                return new LongPropertyConfig(PropertyConfigParcel.createForLong(this.mPropertyName, this.mDescription, this.mCardinality, this.mIndexingType, this.mScoringEnabled));
            }
        }

        void appendLongPropertyConfigFields(IndentingStringBuilder indentingStringBuilder) {
            int indexingType = getIndexingType();
            if (indexingType == 0) {
                indentingStringBuilder.append("indexingType: INDEXING_TYPE_NONE,\n");
            } else if (indexingType == 1) {
                indentingStringBuilder.append("indexingType: INDEXING_TYPE_RANGE,\n");
            } else {
                indentingStringBuilder.append("indexingType: INDEXING_TYPE_UNKNOWN,\n");
            }
        }
    }

    public final class DoublePropertyConfig extends PropertyConfig {
        DoublePropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            super(propertyConfigParcel);
        }

        public final class Builder {
            private final String mPropertyName;
            private String mDescription = "";
            private int mCardinality = 2;
            private boolean mScoringEnabled = false;

            public Builder(String str) {
                this.mPropertyName = (String) Preconditions.checkNotNull(str);
            }

            public Builder setCardinality(int i) {
                Preconditions.checkArgumentInRange(i, 1, 3, "cardinality");
                this.mCardinality = i;
                return this;
            }

            public DoublePropertyConfig build() {
                return new DoublePropertyConfig(PropertyConfigParcel.createForDouble(this.mPropertyName, this.mDescription, this.mCardinality, this.mScoringEnabled));
            }
        }
    }

    public final class BooleanPropertyConfig extends PropertyConfig {
        BooleanPropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            super(propertyConfigParcel);
        }

        public final class Builder {
            private final String mPropertyName;
            private String mDescription = "";
            private int mCardinality = 2;
            private boolean mScoringEnabled = false;

            public Builder(String str) {
                this.mPropertyName = (String) Preconditions.checkNotNull(str);
            }

            public Builder setCardinality(int i) {
                Preconditions.checkArgumentInRange(i, 1, 3, "cardinality");
                this.mCardinality = i;
                return this;
            }

            public BooleanPropertyConfig build() {
                return new BooleanPropertyConfig(PropertyConfigParcel.createForBoolean(this.mPropertyName, this.mDescription, this.mCardinality, this.mScoringEnabled));
            }
        }
    }

    public final class BytesPropertyConfig extends PropertyConfig {
        BytesPropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            super(propertyConfigParcel);
        }
    }

    public final class DocumentPropertyConfig extends PropertyConfig {
        DocumentPropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            super(propertyConfigParcel);
        }

        public String getSchemaType() {
            return (String) Preconditions.checkNotNull(this.mPropertyConfigParcel.getSchemaType());
        }

        public boolean shouldIndexNestedProperties() {
            PropertyConfigParcel.DocumentIndexingConfigParcel documentIndexingConfigParcel = this.mPropertyConfigParcel.getDocumentIndexingConfigParcel();
            if (documentIndexingConfigParcel == null) {
                return false;
            }
            return documentIndexingConfigParcel.shouldIndexNestedProperties();
        }

        public List getIndexableNestedProperties() {
            PropertyConfigParcel.DocumentIndexingConfigParcel documentIndexingConfigParcel = this.mPropertyConfigParcel.getDocumentIndexingConfigParcel();
            if (documentIndexingConfigParcel == null) {
                return Collections.EMPTY_LIST;
            }
            List indexableNestedPropertiesList = documentIndexingConfigParcel.getIndexableNestedPropertiesList();
            if (indexableNestedPropertiesList == null) {
                return Collections.EMPTY_LIST;
            }
            return Collections.unmodifiableList(indexableNestedPropertiesList);
        }

        public final class Builder {
            private final String mPropertyName;
            private final String mSchemaType;
            private String mDescription = "";
            private int mCardinality = 2;
            private boolean mShouldIndexNestedProperties = false;
            private final Set mIndexableNestedPropertiesList = new ArraySet();

            public Builder(String str, String str2) {
                this.mPropertyName = (String) Preconditions.checkNotNull(str);
                this.mSchemaType = (String) Preconditions.checkNotNull(str2);
            }

            public Builder setCardinality(int i) {
                Preconditions.checkArgumentInRange(i, 1, 3, "cardinality");
                this.mCardinality = i;
                return this;
            }

            public Builder setShouldIndexNestedProperties(boolean z) {
                this.mShouldIndexNestedProperties = z;
                return this;
            }

            public DocumentPropertyConfig build() {
                if (this.mShouldIndexNestedProperties && !this.mIndexableNestedPropertiesList.isEmpty()) {
                    Folme$$ExternalSyntheticBUOutline0.m("DocumentIndexingConfig#shouldIndexNestedProperties is required to be false when one or more indexableNestedProperties are provided.");
                    return null;
                }
                return new DocumentPropertyConfig(PropertyConfigParcel.createForDocument(this.mPropertyName, this.mDescription, this.mCardinality, this.mSchemaType, new PropertyConfigParcel.DocumentIndexingConfigParcel(this.mShouldIndexNestedProperties, new ArrayList(this.mIndexableNestedPropertiesList))));
            }
        }

        void appendDocumentPropertyConfigFields(IndentingStringBuilder indentingStringBuilder) {
            indentingStringBuilder.append("shouldIndexNestedProperties: ").append(Boolean.valueOf(shouldIndexNestedProperties())).append(",\n");
            indentingStringBuilder.append("indexableNestedProperties: ").append(getIndexableNestedProperties()).append(",\n");
            indentingStringBuilder.append("schemaType: \"").append(getSchemaType()).append("\",\n");
        }
    }

    public final class EmbeddingPropertyConfig extends PropertyConfig {
        EmbeddingPropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            super(propertyConfigParcel);
        }
    }

    public final class BlobHandlePropertyConfig extends PropertyConfig {
        BlobHandlePropertyConfig(PropertyConfigParcel propertyConfigParcel) {
            super(propertyConfigParcel);
        }
    }
}
