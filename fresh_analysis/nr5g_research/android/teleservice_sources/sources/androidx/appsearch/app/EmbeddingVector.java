package androidx.appsearch.app;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.appsearch.safeparcel.AbstractSafeParcelable;
import androidx.appsearch.safeparcel.stub.AbstractCreator;
import androidx.core.util.Preconditions;
import java.util.Arrays;
import java.util.Objects;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class EmbeddingVector extends AbstractSafeParcelable {
    public static final Parcelable.Creator CREATOR = new AbstractCreator() { // from class: androidx.appsearch.safeparcel.stub.StubCreators$EmbeddingVectorCreator
        @Override // androidx.appsearch.safeparcel.stub.AbstractCreator, android.os.Parcelable.Creator
        public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return super.createFromParcel(parcel);
        }

        @Override // androidx.appsearch.safeparcel.stub.AbstractCreator, android.os.Parcelable.Creator
        public /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return super.newArray(i);
        }
    };
    private Integer mHashCode;
    private final String mModelSignature;
    private final float[] mValues;

    public EmbeddingVector(float[] fArr, String str) {
        float[] fArr2 = (float[]) Preconditions.checkNotNull(fArr);
        this.mValues = fArr2;
        if (fArr2.length == 0) {
            Folme$$ExternalSyntheticBUOutline0.m("Embedding values cannot be empty.");
            throw null;
        }
        this.mModelSignature = (String) Preconditions.checkNotNull(str);
    }

    public float[] getValues() {
        return this.mValues;
    }

    public String getModelSignature() {
        return this.mModelSignature;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof EmbeddingVector)) {
            return false;
        }
        EmbeddingVector embeddingVector = (EmbeddingVector) obj;
        return Arrays.equals(this.mValues, embeddingVector.mValues) && this.mModelSignature.equals(embeddingVector.mModelSignature);
    }

    public int hashCode() {
        if (this.mHashCode == null) {
            this.mHashCode = Integer.valueOf(Objects.hash(Integer.valueOf(Arrays.hashCode(this.mValues)), this.mModelSignature));
        }
        return this.mHashCode.intValue();
    }
}
