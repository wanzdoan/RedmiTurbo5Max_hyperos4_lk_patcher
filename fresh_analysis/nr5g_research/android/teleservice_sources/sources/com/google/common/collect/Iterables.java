package com.google.common.collect;

import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
public abstract class Iterables {
    static Object[] toArray(Iterable iterable) {
        return castOrCopyToCollection(iterable).toArray();
    }

    private static Collection castOrCopyToCollection(Iterable iterable) {
        if (iterable instanceof Collection) {
            return (Collection) iterable;
        }
        return Lists.newArrayList(iterable.iterator());
    }
}
