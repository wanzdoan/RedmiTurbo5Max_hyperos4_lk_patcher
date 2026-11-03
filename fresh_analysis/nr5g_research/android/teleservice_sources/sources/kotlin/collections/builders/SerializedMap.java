package kotlin.collections.builders;

import com.google.common.collect.CompactHashMap$$ExternalSyntheticBUOutline0;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: MapBuilder.kt */
/* JADX INFO: loaded from: classes2.dex */
final class SerializedMap implements Externalizable {
    public static final Companion Companion = new Companion(null);
    private static final long serialVersionUID = 0;
    private Map map;

    public SerializedMap(Map map) {
        map.getClass();
        this.map = map;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.getClass();
        objectOutput.writeByte(0);
        objectOutput.writeInt(this.map.size());
        for (Map.Entry entry : this.map.entrySet()) {
            objectOutput.writeObject(entry.getKey());
            objectOutput.writeObject(entry.getValue());
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws InvalidObjectException {
        objectInput.getClass();
        byte b = objectInput.readByte();
        if (b != 0) {
            CompactHashMap$$ExternalSyntheticBUOutline0.m("Unsupported flags value: ", b);
            return;
        }
        int i = objectInput.readInt();
        if (i < 0) {
            SerializedMap$$ExternalSyntheticBUOutline0.m("Illegal size value: ", i);
            return;
        }
        Map mapCreateMapBuilder = MapsKt.createMapBuilder(i);
        for (int i2 = 0; i2 < i; i2++) {
            mapCreateMapBuilder.put(objectInput.readObject(), objectInput.readObject());
        }
        this.map = MapsKt.build(mapCreateMapBuilder);
    }

    private final Object readResolve() {
        return this.map;
    }

    /* JADX INFO: compiled from: MapBuilder.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
