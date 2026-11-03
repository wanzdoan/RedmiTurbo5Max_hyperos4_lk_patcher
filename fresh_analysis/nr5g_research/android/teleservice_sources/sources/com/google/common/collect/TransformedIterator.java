package com.google.common.collect;

import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
abstract class TransformedIterator implements Iterator {
    final Iterator backingIterator;

    abstract Object transform(Object obj);

    TransformedIterator(Iterator it) {
        it.getClass();
        this.backingIterator = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.backingIterator.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        return transform(this.backingIterator.next());
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.backingIterator.remove();
    }
}
