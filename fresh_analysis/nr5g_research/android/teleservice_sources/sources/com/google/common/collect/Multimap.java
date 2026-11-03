package com.google.common.collect;

import java.util.Collection;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface Multimap {
    Map asMap();

    void clear();

    boolean containsEntry(Object obj, Object obj2);

    Collection entries();

    Collection get(Object obj);

    boolean remove(Object obj, Object obj2);

    int size();
}
