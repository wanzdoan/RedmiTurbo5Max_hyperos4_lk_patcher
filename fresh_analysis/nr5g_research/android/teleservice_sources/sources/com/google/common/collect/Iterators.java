package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
public abstract class Iterators {
    static UnmodifiableIterator emptyIterator() {
        return emptyListIterator();
    }

    static UnmodifiableListIterator emptyListIterator() {
        return ArrayItr.EMPTY;
    }

    public static int size(Iterator it) {
        long j = 0;
        while (it.hasNext()) {
            it.next();
            j++;
        }
        return Ints.saturatedCast(j);
    }

    public static boolean removeAll(Iterator it, Collection collection) {
        collection.getClass();
        boolean z = false;
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static boolean elementsEqual(Iterator it, Iterator it2) {
        while (it.hasNext()) {
            if (!it2.hasNext() || !Objects.equal(it.next(), it2.next())) {
                return false;
            }
        }
        return !it2.hasNext();
    }

    public static boolean addAll(Collection collection, Iterator it) {
        collection.getClass();
        it.getClass();
        boolean zAdd = false;
        while (it.hasNext()) {
            zAdd |= collection.add(it.next());
        }
        return zAdd;
    }

    public static Iterator transform(Iterator it, final Function function) {
        function.getClass();
        return new TransformedIterator(it) { // from class: com.google.common.collect.Iterators.6
            @Override // com.google.common.collect.TransformedIterator
            Object transform(Object obj) {
                return function.apply(obj);
            }
        };
    }

    public static Object getNext(Iterator it, Object obj) {
        return it.hasNext() ? it.next() : obj;
    }

    static Object pollNext(Iterator it) {
        if (!it.hasNext()) {
            return null;
        }
        Object next = it.next();
        it.remove();
        return next;
    }

    static void clear(Iterator it) {
        it.getClass();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    final class ArrayItr extends AbstractIndexedListIterator {
        static final UnmodifiableListIterator EMPTY = new ArrayItr(new Object[0], 0);
        private final Object[] array;

        ArrayItr(Object[] objArr, int i) {
            super(objArr.length, i);
            this.array = objArr;
        }

        @Override // com.google.common.collect.AbstractIndexedListIterator
        protected Object get(int i) {
            return this.array[i];
        }
    }

    public static UnmodifiableIterator singletonIterator(Object obj) {
        return new SingletonIterator(obj);
    }

    final class SingletonIterator extends UnmodifiableIterator {
        private boolean done;
        private final Object value;

        SingletonIterator(Object obj) {
            this.value = obj;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.done;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.done) {
                throw new NoSuchElementException();
            }
            this.done = true;
            return this.value;
        }
    }

    class PeekingImpl implements PeekingIterator {
        private boolean hasPeeked;
        private final Iterator iterator;
        private Object peekedElement;

        public PeekingImpl(Iterator it) {
            it.getClass();
            this.iterator = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }

        @Override // com.google.common.collect.PeekingIterator, java.util.Iterator
        public Object next() {
            if (!this.hasPeeked) {
                return this.iterator.next();
            }
            Object objUncheckedCastNullableTToT = NullnessCasts.uncheckedCastNullableTToT(this.peekedElement);
            this.hasPeeked = false;
            this.peekedElement = null;
            return objUncheckedCastNullableTToT;
        }

        @Override // java.util.Iterator
        public void remove() {
            Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
            this.iterator.remove();
        }

        @Override // com.google.common.collect.PeekingIterator
        public Object peek() {
            if (!this.hasPeeked) {
                this.peekedElement = this.iterator.next();
                this.hasPeeked = true;
            }
            return NullnessCasts.uncheckedCastNullableTToT(this.peekedElement);
        }
    }

    public static PeekingIterator peekingIterator(Iterator it) {
        if (it instanceof PeekingImpl) {
            return (PeekingImpl) it;
        }
        return new PeekingImpl(it);
    }
}
