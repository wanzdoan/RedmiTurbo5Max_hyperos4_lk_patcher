package com.android.storage.table.reader;

/* JADX INFO: loaded from: classes.dex */
public interface IntValueTable$TableEntry extends Table$TableEntry {
    int getValue();

    static boolean equal(IntValueTable$TableEntry intValueTable$TableEntry, Object obj) {
        if (intValueTable$TableEntry == null && obj == null) {
            return true;
        }
        if ((obj instanceof IntValueTable$TableEntry) && intValueTable$TableEntry != null && intValueTable$TableEntry.getClass() == obj.getClass()) {
            IntValueTable$TableEntry intValueTable$TableEntry2 = (IntValueTable$TableEntry) obj;
            if (intValueTable$TableEntry.getKey() == intValueTable$TableEntry2.getKey() && intValueTable$TableEntry.getValue() == intValueTable$TableEntry2.getValue()) {
                return true;
            }
        }
        return false;
    }

    static String toString(IntValueTable$TableEntry intValueTable$TableEntry) {
        return "TableEntry{key=" + intValueTable$TableEntry.getKey() + ", value=" + intValueTable$TableEntry.getValue() + "}";
    }
}
