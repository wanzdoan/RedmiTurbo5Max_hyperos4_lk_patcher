package com.google.common.collect;

import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public interface PeekingIterator extends Iterator {
    @Override // java.util.Iterator
    Object next();

    Object peek();
}
