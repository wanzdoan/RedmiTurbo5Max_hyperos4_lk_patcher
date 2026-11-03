package kotlin.collections.builders;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: ListBuilder.kt */
/* JADX INFO: loaded from: classes2.dex */
public final class SerializedCollection implements Externalizable {
    public static final Companion Companion = new Companion(null);
    private static final long serialVersionUID = 0;
    private Collection collection;
    private final int tag;

    public SerializedCollection(Collection collection, int i) {
        collection.getClass();
        this.collection = collection;
        this.tag = i;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.getClass();
        objectOutput.writeByte(this.tag);
        objectOutput.writeInt(this.collection.size());
        Iterator it = this.collection.iterator();
        while (it.hasNext()) {
            objectOutput.writeObject(it.next());
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws InvalidObjectException {
        Collection collectionBuild;
        objectInput.getClass();
        byte b = objectInput.readByte();
        int i = b & 1;
        if ((b & (-2)) != 0) {
            SerializedMap$$ExternalSyntheticBUOutline0.m("Unsupported flags value: ", b);
            return;
        }
        int i2 = objectInput.readInt();
        if (i2 < 0) {
            SerializedMap$$ExternalSyntheticBUOutline0.m("Illegal size value: ", i2);
            return;
        }
        int i3 = 0;
        if (i == 0) {
            List listCreateListBuilder = CollectionsKt.createListBuilder(i2);
            while (i3 < i2) {
                listCreateListBuilder.add(objectInput.readObject());
                i3++;
            }
            collectionBuild = CollectionsKt.build(listCreateListBuilder);
        } else if (i == 1) {
            Set setCreateSetBuilder = SetsKt.createSetBuilder(i2);
            while (i3 < i2) {
                setCreateSetBuilder.add(objectInput.readObject());
                i3++;
            }
            collectionBuild = SetsKt.build(setCreateSetBuilder);
        } else {
            SerializedMap$$ExternalSyntheticBUOutline0.m("Unsupported collection type tag: ", i);
            return;
        }
        this.collection = collectionBuild;
    }

    private final Object readResolve() {
        return this.collection;
    }

    /* JADX INFO: compiled from: ListBuilder.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
