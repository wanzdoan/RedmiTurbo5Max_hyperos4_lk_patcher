package com.google.common.collect;

import java.util.Collection;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public interface Multiset extends Collection {

    public interface Entry {
        int getCount();

        Object getElement();
    }

    int count(Object obj);

    Set elementSet();

    Set entrySet();

    @Override // java.util.Collection
    int size();
}
