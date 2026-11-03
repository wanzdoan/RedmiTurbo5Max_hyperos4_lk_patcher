package kotlin.enums;

import java.io.Serializable;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: EnumEntriesSerializationProxy.kt */
/* JADX INFO: loaded from: classes2.dex */
public final class EnumEntriesSerializationProxy implements Serializable {
    private static final Companion Companion = new Companion(null);
    private static final long serialVersionUID = 0;
    private final Class c;

    public EnumEntriesSerializationProxy(Enum[] enumArr) {
        enumArr.getClass();
        Class<?> componentType = enumArr.getClass().getComponentType();
        componentType.getClass();
        this.c = componentType;
    }

    /* JADX INFO: compiled from: EnumEntriesSerializationProxy.kt */
    final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final Object readResolve() {
        Object[] enumConstants = this.c.getEnumConstants();
        enumConstants.getClass();
        return EnumEntriesKt.enumEntries((Enum[]) enumConstants);
    }
}
