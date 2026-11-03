package gov.nist.core;

import java.io.Serializable;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes2.dex */
public abstract class GenericObjectList extends LinkedList implements Serializable, Cloneable {
    public static boolean isMySubclass(Class cls) {
        return GenericObjectList.class.isAssignableFrom(cls);
    }
}
