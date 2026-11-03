package ai.onnxruntime.providers;

import java.util.EnumSet;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public interface OrtFlags {
    int getValue();

    static int aggregateToInt(EnumSet enumSet) {
        Iterator it = enumSet.iterator();
        int value = 0;
        while (it.hasNext()) {
            value |= ((OrtFlags) it.next()).getValue();
        }
        return value;
    }
}
